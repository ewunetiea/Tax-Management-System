import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Branch } from 'app/models/admin/branch';
import { User } from 'app/models/admin/user';
import { Tax } from 'app/models/maker/tax';
import { TaxCategory } from 'app/models/maker/tax-category';
import { BranchService } from 'app/service/admin/branchService';
import { TaxableSearchEngineService } from 'app/service/common/taxable-search-engine-service';
import { TaxCategoriesService } from 'app/service/maker/tax-categories-service';
import { StorageService } from 'app/service/sharedService/storage.service';
import { MessageService } from 'primeng/api';
import { finalize, catchError, of } from 'rxjs';
import { SharedUiModule } from 'shared-ui';

@Component({
  selector: 'app-search-engine',
  imports: [SharedUiModule],
  templateUrl: './search-engine.component.html',
  styleUrls: ['./search-engine.component.scss']
})
export class SearchEngineComponent {
  @Output() generatedTaxes = new EventEmitter<{ data: Tax[]; totalRecords: number; fetching: boolean }>();
  @Input() statusRoute: string = '';

  form!: FormGroup;
  user: User = new User();
  branches: Branch[] = [];
  taxCategories: TaxCategory[] = [];
  status: { name: string, code: string }[] = [];
  submitted = false;
  branchLoading = false;
  taxCategoryLoading = false;
  maxDate = new Date();

  // Pagination state
  currentPage: number = 1;
  pageSize: number = 5;
  totalRecords: number = 0;

  constructor(
    private fb: FormBuilder,
    private storageService: StorageService,
    private taxCategoriesService: TaxCategoriesService,
    private messageService: MessageService,
    private branchService: BranchService,
    private taxableSearchEngineService: TaxableSearchEngineService,
  ) {}

  ngOnInit(): void {
    this.user = this.storageService.getUser();

    this.form = this.fb.group({
      branch_id: [],
      tax_category_id: [],
      reference_number: [],
      router_status: [],
      maked_date: [],
      checked_date: [],
      approved_date: [],
      rejected_date: [],
      user_id: [],
    });

    this.getBranches();
    this.getTaxCategories();

    this.status = [
      { name: 'Drafted', code: '6' },
      { name: 'Submitted', code: '0' },
      { name: 'Sent', code: '1' },
      { name: 'Settled', code: '5' },
      { name: 'Rejected', code: '2' }
    ];
  }

  getBranches(): void {
    this.branchLoading = true;
    this.branchService.getBranches().pipe(
      finalize(() => this.branchLoading = false),
      catchError(() => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load branches' });
        return of([]);
      })
    ).subscribe(data => this.branches = data);
  }

  getTaxCategories(): void {
    this.taxCategoryLoading = true;
    this.taxCategoriesService.getTaxCategories().pipe(
      finalize(() => this.taxCategoryLoading = false),
      catchError(() => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load tax categories' });
        return of([]);
      })
    ).subscribe(data => this.taxCategories = data);
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
    this.currentPage = 1;
    this.emitData([], 0, false);
  }

  private emitData(data: Tax[], totalRecords: number, fetching: boolean) {
    this.generatedTaxes.emit({ data, totalRecords, fetching });
  }

  onLazyLoad(event: any) {
    this.currentPage = (event.first / event.rows) + 1;
    this.pageSize = event.rows;
    this.generateTaxes();
  }

  generateTaxes(): void {
    this.submitted = true;
    this.emitData([], 0, true);

    const payload = { ...this.form.value, currentPage: this.currentPage, pageSize: this.pageSize };
    Object.keys(payload).forEach(k => { if (payload[k] === '') payload[k] = null; });

    this.taxableSearchEngineService.getTaxesforAdmin(payload).pipe(
      finalize(() => this.submitted = false),
      catchError((error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to fetch taxes' });
        this.emitData([], 0, false);
        return of({ data: [], totalRecords: 0 });
      })
    ).subscribe((res: any) => {
      // Expect backend to return { data: Tax[], totalRecords: number }
      console.log('Search Result:', res);
      this.totalRecords = res[0].total_records_paginator || 0;
      console.log('total Length:', this.totalRecords );
      this.emitData(res ?? [], this.totalRecords, true);
    });
  }
}
