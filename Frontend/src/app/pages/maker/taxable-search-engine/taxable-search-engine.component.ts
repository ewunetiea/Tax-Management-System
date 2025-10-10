import { Component, EventEmitter, Output } from '@angular/core';
import { StorageService } from '../../../service/sharedService/storage.service';
import { MessageService } from 'primeng/api';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TaxCategoriesService } from '../../../service/maker/tax-categories-service';
import { TaxableSearchEngine } from '../../../models/maker/taxable-search-engine';
import { User } from '../../../models/admin/user';
import { SharedUiModule } from '../../../../shared-ui';
import { BranchService } from '../../../service/admin/branchService';
import { Branch } from '../../../models/admin/branch';
import { TaxCategory } from '../../../models/maker/tax-category';
import { Tax } from '../../../models/maker/tax';
import { Router } from '@angular/router';
import { PaginatorPayLoad } from '../../../models/admin/paginator-payload';
import { ManageTaxService } from '../../../service/checker/manage_tax_service';
import { catchError, finalize, Observable, of } from 'rxjs';
import { TaxableSearchEngineService } from '../../../service/maker/taxable-search-engine-service';

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
  status: any[] | undefined;
  paginatorPayLoad: PaginatorPayLoad = new PaginatorPayLoad();
  maxDate = new Date();
        

  @Output() generatedTaxes: EventEmitter<any> = new EventEmitter();

  constructor(
    private fb: FormBuilder,
    private storageService: StorageService,
    private taxCategoriesService: TaxCategoriesService,
    private messageService: MessageService,
    private branchService: BranchService,
    private manageTaxService: ManageTaxService,
    private router: Router,
    private taxableSearchEngineService: TaxableSearchEngineService,
  ) { }


  ngOnInit(): void {
    this.user = this.storageService.getUser();
    this.paginatorPayLoad.branch_id = this.user.branch?.id;
    this.paginatorPayLoad.user_id = this.user.id;

    this.status = [
      { name: 'Draft', id: '0' },
      { name: 'Pending', id: '1' },
      { name: 'Rejected', id: '2' },
      { name: 'Not Approved', id: '3' },
      { name: 'Reviewed', id: '4' },
      { name: 'Approved', id: '5' },
    ]

    this.form = this.fb.group({
      branch_id: ['',],
      tax_category_id: [''],
      reference_number: [''],
      status_id: [''],
      maked_date: [''],
      checked_date: [''],
      approved_date: [''],
      rejected_date: [''],
      document_type: [''],
      router_status: [''],
    });
  }

  emitData(data: Tax[]) {
    this.generatedTaxes.emit(data);
  }

  getBranches() {
    this.branchLoading = true;
    this.branchService.getBranches().subscribe({
      next: (data) => {
        this.branches = data;
        this.branchLoading = false;
      },
      error: (error) => {
        this.branchLoading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load branches. Please try again later.',
        });
      },
    });
  }

  getTaxCategories() {
    this.taxCategoryLoading = true;
    this.taxCategoriesService.getTaxCategories().subscribe({
      next: (data) => {
        this.taxCategories = data;
        this.taxCategoryLoading = false;
      },
      error: (error) => {
        this.taxCategoryLoading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load tax categories',
        });
      },
    });
  }

  //    generateTaxes(): void {
  //   this.submitted = true;
  //   const currentRoute = this.router.url;
  //   let request$: Observable<any>;

  //   // Determine the right request based on route
  //   if (currentRoute.includes('pending')) {
  //     request$ = this.manageTaxService.getPendingTaxes(this.paginatorPayLoad);
  //   } else if (currentRoute.includes('rejected')) {
  //     request$ = this.manageTaxService.getRejectedTaxes(this.paginatorPayLoad);
  //   } else {
  //     request$ = this.manageTaxService.getApprovedTaxes(this.paginatorPayLoad);
  //   }

  //   request$
  //     .pipe(
  //       finalize(() => (this.submitted = false)), 
  //       catchError((error) => {
  //         console.error('Error while fetching taxes:', error);
  //         this.messageService.add({
  //           severity: 'error',
  //           summary: 'Error',
  //           detail: 'Failed to load tax records. Please try again later.',
  //         });
  //         return of([]); 
  //       })
  //     )
  //     .subscribe((res) => this.emitData(res));
  // }

  generateTaxes(): void {
    this.submitted = true;
    const currentRoute = this.router.url.toLowerCase();
    let routerStatus = 'pending';
    if (currentRoute.includes('approved')) routerStatus = 'approved';
    else if (currentRoute.includes('rejected')) routerStatus = 'rejected';

    // ✅ Update form correctly
    this.form.patchValue({ router_status: routerStatus });

    // ✅ Construct payload safely
    const searchEngine: TaxableSearchEngine = {
      ...this.form.value,
      search_by: this.user.email?.split('@')[0] || '',
      user_id: this.user.id
    };

    this.taxableSearchEngineService.getTaxes(searchEngine)
      .pipe(
        finalize(() => this.submitted = false),
        catchError(error => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to fetch taxes. Please try again later.'
          });
          return of([]);
        })
      )
      .subscribe((data) => {
        this.taxes = data;
        this.emitData(data);
      });
  }

  onReset(): void {
    this.submitted = false;
    this.taxes = [];
    this.form.reset();
    this.generatedTaxes.emit([]);
  }





}
