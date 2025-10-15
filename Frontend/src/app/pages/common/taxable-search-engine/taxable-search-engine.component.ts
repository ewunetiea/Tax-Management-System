import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { StorageService } from '../../../service/sharedService/storage.service';
import { MessageService } from 'primeng/api';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TaxCategoriesService } from '../../../service/maker/tax-categories-service';
import { TaxableSearchEngine } from '../../../models/common/taxable-search-engine';
import { User } from '../../../models/admin/user';
import { SharedUiModule } from '../../../../shared-ui';
import { BranchService } from '../../../service/admin/branchService';
import { Branch } from '../../../models/admin/branch';
import { TaxCategory } from '../../../models/maker/tax-category';
import { Tax } from '../../../models/maker/tax';
import { ActivatedRoute } from '@angular/router';
import { PaginatorPayLoad } from '../../../models/admin/paginator-payload';
import { TaxableSearchEngineService } from '../../../service/common/taxable-search-engine-service';
import { Role } from '../../../models/admin/role';
import { finalize, of, catchError } from 'rxjs';

@Component({
  selector: 'app-taxable-search-engine',
  imports: [SharedUiModule],
  templateUrl: './taxable-search-engine.component.html',
  styleUrl: './taxable-search-engine.component.scss'
})
export class TaxableSearchEngineComponent {
  taxableSearchEngine: TaxableSearchEngine = new TaxableSearchEngine();
  user: User = new User();
  form!: FormGroup;
  branches: Branch[] = [];
  submitted = false;
  taxCategories: TaxCategory[] = [];
  taxes: Tax[] = [];
  branchLoading = false;
  taxCategoryLoading = false;
  paginatorPayLoad: PaginatorPayLoad = new PaginatorPayLoad();
  maxDate = new Date();
  isApprover = false;
  roles: string[] = [];

  @Output() generatedTaxes: EventEmitter<Tax[]> = new EventEmitter<Tax[]>();
  @Input() statusRoute: string = '';

  constructor(
    private fb: FormBuilder,
    private storageService: StorageService,
    private taxCategoriesService: TaxCategoriesService,
    private messageService: MessageService,
    private branchService: BranchService,
    private taxableSearchEngineService: TaxableSearchEngineService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.user = this.storageService.getUser();
    const users = this.storageService.getUser();
    this.paginatorPayLoad.branch_id = this.user.branch?.id;
    this.paginatorPayLoad.user_id = this.user.id;
    this.roles = users.roles;
    this.isApprover = this.roles.includes('ROLE_APPROVER');
    

    // ✅ Initialize form controls
    this.form = this.fb.group({
     branch_id: [this.user.branch?.id || ''],
      tax_category_id: [''],
      reference_number: [''],
      router_status: [''],
      maked_date: [''],
      checked_date: [''],
      approved_date: [''],
      rejected_date: [''],
      document_type: [''],
      user_id: [this.user.id || ''],
      search_by: [this.user.email?.split('@')[0] || '']
    });

    // ✅ Automatically detect router status from route param
    this.route.paramMap.subscribe(params => {
      const status = params.get('status')?.toLowerCase() || 'pending';
      this.onReset();
      this.form.get('router_status')?.setValue(status);
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['statusRoute'] && !changes['statusRoute'].firstChange) {
      this.onReset();
    }
  }

  emitData(data: Tax[]) {
    this.generatedTaxes.emit(data);
  }

  // ✅ Load branches dynamically
  getBranches() {
    this.branchLoading = true;
    this.branchService.getBranches().subscribe({
      next: (data) => {
        this.branches = data;
        this.branchLoading = false;
      },
      error: () => {
        this.branchLoading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load branches. Please try again later.'
        });
      }
    });
  }

  // ✅ Load tax categories dynamically
  getTaxCategories() {
    this.taxCategoryLoading = true;
    this.taxCategoriesService.getTaxCategories().subscribe({
      next: (data) => {
        this.taxCategories = data;
        this.taxCategoryLoading = false;
      },
      error: () => {
        this.taxCategoryLoading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load tax categories'
        });
      }
    });
  }

  // ✅ Reset form and clear data
  onReset(): void {
    this.form.reset();
    this.submitted = false;
    this.taxes = [];
    this.generatedTaxes.emit([]); // tell parent to clear table
  }

  generateTaxes(): void {
  this.submitted = true;

  const routerStatus = this.statusRoute?.toLowerCase() || 'pending';

  // ✅ Normalize roles
  const normalizedRoles: Role[] = (this.user?.roles ?? []).map(r =>
    typeof r === 'string' ? ({ name: r } as Role) : r
  );
  const roleNames = normalizedRoles.map(r => r.name ?? '');

  // ✅ Determine branch_id based on role
  let branchId: number | null = null;
  if (roleNames.includes('ROLE_APPROVER')) {
    branchId = this.form.value.branch_id ?? null;

    // ❌ If branch_id not selected, show warning and stop
    if (!branchId) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Warning',
        detail: 'Please select a unit before searching'
      });
      this.submitted = false;
      return; // stop further execution
    }
  } else {
    branchId = this.user.branch?.id ?? null;
  }

  // ✅ Patch values into the form
  this.form.patchValue({
    router_status: routerStatus,
    user_id: this.user.id,
    branch_id: branchId,
    search_by: this.user.email?.split('@')[0] || ''
  });

  // ✅ Construct clean payload
  const payload = { ...this.form.value };

  // Convert empty strings to null
  Object.keys(payload).forEach(k => {
    if (payload[k] === '') payload[k] = null;
  });

  // ✅ Select appropriate API endpoint
  let request$;
  if (roleNames.includes('ROLE_APPROVER')) {
    request$ = this.taxableSearchEngineService.getTaxesforApprover(payload);
  } else if (roleNames.includes('ROLE_REVIEWER')) {
    request$ = this.taxableSearchEngineService.getTaxesForReviewer(payload);
  } else {
    request$ = this.taxableSearchEngineService.getTaxesFormaker(payload);
  }

  // ✅ Handle API result
  request$
    .pipe(
      finalize(() => (this.submitted = false)),
      catchError((error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to fetch taxes'
        });
        return of([]);
      })
    )
    .subscribe((data: Tax[]) => {
      this.taxes = data;
      this.generatedTaxes.emit(data);
    });
}


}
