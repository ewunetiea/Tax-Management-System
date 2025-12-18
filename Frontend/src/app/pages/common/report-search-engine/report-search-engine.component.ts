import { Component, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Branch } from 'app/models/admin/branch';
import { PaginatorPayLoad } from 'app/models/admin/paginator-payload';
import { Role } from 'app/models/admin/role';
import { User } from 'app/models/admin/user';
import { TaxableSearchEngine } from 'app/models/common/taxable-search-engine';
import { Tax } from 'app/models/maker/tax';
import { TaxCategory } from 'app/models/maker/tax-category';
import { BranchService } from 'app/service/admin/branchService';
import { ReportService } from 'app/service/common/report-service';
import { TaxCategoriesService } from 'app/service/maker/tax-categories-service';
import { StorageService } from 'app/service/sharedService/storage.service';
import { xssSqlValidator } from 'app/SQLi-XSS-Prevention/xssSqlValidator';
import { MessageService } from 'primeng/api';
import { finalize, catchError, of } from 'rxjs';
import { SharedUiModule } from 'shared-ui';

@Component({
  selector: 'app-report-search-engine',
  imports: [SharedUiModule],
  templateUrl: './report-search-engine.component.html',
  styleUrl: './report-search-engine.component.scss'
})
export class ReportSearchEngineComponent {
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
  isReviewer = false;
  isMaker = false;
  roles: string[] = [];
  status: any[] | undefined;

  // 🔥 Updated: Now emits both data and fetching state
  @Output() generatedTaxes = new EventEmitter<{ data: Tax[]; fetching: boolean }>();

  constructor(
    private fb: FormBuilder,
    private storageService: StorageService,
    private taxCategoriesService: TaxCategoriesService,
    private messageService: MessageService,
    private branchService: BranchService,
    private reportService: ReportService,
  ) { }

  ngOnInit(): void {
    this.user = this.storageService.getUser();
    const users = this.storageService.getUser();
    this.paginatorPayLoad.branch_id = this.user.branch?.id;
    this.paginatorPayLoad.user_id = this.user.id;
    this.roles = users.roles;

    // Normalize roles
    this.isApprover = this.roles.includes('ROLE_APPROVER');
    this.isReviewer = this.roles.includes('ROLE_REVIEWER');
    this.isMaker = this.roles.includes('ROLE_MAKER');

    this.form = this.fb.group({
      branch_id: [''],
      tax_category_id: [''],
      reference_number: ['', [Validators.minLength(3), Validators.maxLength(100), xssSqlValidator]],
      router_status: [''],
      maked_date: [''],
      checked_date: [''],
      approved_date: [''],
      rejected_date: [''],
      document_type: [''],
      user_id: [this.user.id || ''],
      director_id: [''],
      search_by: [this.user.email?.split('@')[0] || '']
    });

    // ✅ Centralized status definitions (single source of truth)
    const STATUS_MAP: Record<string, { name: string; id: number }[]> = {
      ROLE_APPROVER: [
        { name: 'Pending', id: 1 },
        { name: 'Rejected', id: 3 },
        { name: 'Approved', id: 5 }
      ],
      ROLE_REVIEWER: [
        { name: 'Pending', id: 0 },
        { name: 'Sent', id: 1 },
        { name: 'Rejected', id: 2 },
        { name: 'Settled', id: 5 }
      ],
      ROLE_MAKER: [
        { name: 'Drafted', id: 6 },
         { name: 'Waiting', id: 0 },
        { name: 'Sent', id: 1 },
        { name: 'Rejected', id: 2 },
        { name: 'Settled', id: 5 }
      ]
    };

    // ✅ Assign based on role in a single lookup (efficient & readable)
    if (this.isApprover) {
      this.status = STATUS_MAP['ROLE_APPROVER'];
    } else if (this.isReviewer) {
      this.status = STATUS_MAP['ROLE_REVIEWER'];
    } else if (this.isMaker) {
      this.status = STATUS_MAP['ROLE_MAKER'];
    } else {
      this.status = [];
    }

  }


  // ✅ Utility to emit unified response
  private emitData(data: Tax[], fetching: boolean) {
    this.generatedTaxes.emit({ data, fetching });
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

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

  // ✅ Reset form and clear data, also set fetching = false
  onReset(): void {
    this.form.reset();
    this.submitted = false;
    this.taxes = [];
    this.emitData([], false); 
  }

  generateTaxes(): void {
    this.submitted = true;
    this.emitData([], false); 

    const normalizedRoles: Role[] = (this.user?.roles ?? []).map(r =>
      typeof r === 'string' ? ({ name: r } as Role) : r
    );
    const roleNames = normalizedRoles.map(r => r.name ?? '');

    let branchId: number | null = null;
    let directorId: number | null = null;

    if (roleNames.includes('ROLE_APPROVER')) {
      branchId = this.form.value.branch_id ?? null;
      directorId = this.user.branch?.id ?? null;
    } else {
      branchId = this.user.branch?.id ?? null;
      directorId = null;
    }

    this.form.patchValue({
      user_id: this.user.id,
      branch_id: branchId,
      director_id: directorId,
      search_by: this.user.email?.split('@')[0] || ''
    });

    const payload = { ...this.form.value };
    Object.keys(payload).forEach(k => {
      if (payload[k] === '') payload[k] = null;
    });


    let request$;
    if (roleNames.includes('ROLE_APPROVER')) {
      request$ = this.reportService.getTaxesforApprover(payload);
    } else if (roleNames.includes('ROLE_REVIEWER')) {
      request$ = this.reportService.getTaxesForReviewer(payload);
    } else {
      request$ = this.reportService.getTaxesFormaker(payload);
    }

    request$
      .pipe(
        finalize(() => {
          this.submitted = false;
        }),
        catchError((error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to fetch taxes'
          });
          this.emitData([], false); // stop fetching on error
          return of([]);
        })
      )
      .subscribe((data: Tax[]) => {
        this.taxes = data;
        this.emitData(data, true); // ✅ send both data + fetching=false
      });
  }
}
