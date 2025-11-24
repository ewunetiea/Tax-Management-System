import { Component, Output, EventEmitter, SimpleChanges, Input } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Branch } from 'app/models/admin/branch';
import { PaginatorPayLoad } from 'app/models/admin/paginator-payload';
import { Role } from 'app/models/admin/role';
import { User } from 'app/models/admin/user';
import { TaxableSearchEngine } from 'app/models/common/taxable-search-engine';
import { Tax } from 'app/models/maker/tax';
import { TaxCategory } from 'app/models/maker/tax-category';
import { BranchService } from 'app/service/admin/branchService';
import { TaxableSearchEngineService } from 'app/service/common/taxable-search-engine-service';
import { TaxCategoriesService } from 'app/service/maker/tax-categories-service';
import { StorageService } from 'app/service/sharedService/storage.service';
import { MessageService } from 'primeng/api';
import { finalize, of, catchError } from 'rxjs';
import { SharedUiModule } from 'shared-ui';

@Component({
  selector: 'app-taxable-searchengine-approver',
  imports: [SharedUiModule],
  templateUrl: './taxable-searchengine-approver.component.html',
  styleUrl: './taxable-searchengine-approver.component.scss'
})

export class TaxableSearchengineApproverComponent {
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

 @Output() generatedTaxes = new EventEmitter<{ data: Tax[]; fetching: boolean }>();
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
      user_id: [''],
      director_id: [''],
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

   private emitData(data: Tax[], fetching: boolean) {
    this.generatedTaxes.emit({ data, fetching });
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
    this.emitData([], false);
  }

  generateTaxes(): void {
  this.submitted = true;
  this.emitData([], false);
  const routerStatus = this.statusRoute?.toLowerCase() || 'pending';

  // ✅ Normalize roles
  const normalizedRoles: Role[] = (this.user?.roles ?? []).map(r =>
    typeof r === 'string' ? ({ name: r } as Role) : r
  );
  const roleNames = normalizedRoles.map(r => r.name ?? '');

  // ✅ Determine branch_id and director_id based on role
  let branchId: number | null = null;
  let directorId: number | null = null;

  if (roleNames.includes('ROLE_APPROVER')) {
    // Approver can choose a branch or leave it null
    branchId = this.form.value.branch_id ?? null;
    directorId = this.user.branch?.id ?? null;
  } else {
    // Maker or reviewer — branch_id fixed to user’s own branch
    branchId = this.user.branch?.id ?? null;
    directorId = null;
  }

  // ✅ Patch values into the form
  this.form.patchValue({
    router_status: routerStatus,
    user_id: this.user.id,
    branch_id: branchId,
    director_id: directorId,
    search_by: this.user.email?.split('@')[0] || ''
  });

  // ✅ Construct clean payload
  const payload = { ...this.form.value };

  // Convert empty strings to null
  Object.keys(payload).forEach(k => {
    if (payload[k] === '') payload[k] = null;
  });

  // ✅ Select appropriate API endpoint
  let  request$ = this.taxableSearchEngineService.getTaxesforApprover(payload);
 
 

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
        this.emitData([], false); 
        return of([]);
      })
    )
    .subscribe((data: Tax[]) => {
      this.emitData(data, true);
    });
}



}
