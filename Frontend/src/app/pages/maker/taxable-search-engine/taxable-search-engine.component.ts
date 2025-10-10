import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
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
  @Input() statusRoute: string = '';

  constructor(
    private fb: FormBuilder,
    private storageService: StorageService,
    private taxCategoriesService: TaxCategoriesService,
    private messageService: MessageService,
    private branchService: BranchService,
    private router: Router,
    private taxableSearchEngineService: TaxableSearchEngineService,
  ) { }


  ngOnInit(): void {
    this.user = this.storageService.getUser();
    this.paginatorPayLoad.branch_id = this.user.branch?.id;
    this.paginatorPayLoad.user_id = this.user.id;

    this.form = this.fb.group({
      branch_id: ['',],
      tax_category_id: [''],
      reference_number: [''],
      router_status: [{ value: 'statusRoute', disabled: true }],
      maked_date: [''],
      checked_date: [''],
      approved_date: [''],
      rejected_date: [''],
      document_type: [''],
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


  ngOnChanges(changes: SimpleChanges): void {
    if (changes['statusRoute'] && !changes['statusRoute'].firstChange) {
      this.onReset();
    }
  }

  onReset(): void {
    this.form.reset();
    this.submitted = false;
    this.taxes = [];
    this.generatedTaxes.emit([]); // tell parent to clear table
  }

  generateTaxes(): void {
    this.submitted = true;

    const routerStatus = this.statusRoute?.toLowerCase() || 'pending';
    this.form.patchValue({ router_status: routerStatus });

    const payload = { ...this.form.value };

    // clean payload (empty strings → null)
    Object.keys(payload).forEach(k => {
      if (payload[k] === '') payload[k] = null;
    });

    this.taxableSearchEngineService.getTaxes(payload).subscribe({
      next: (data) => {
        this.taxes = data;
        this.generatedTaxes.emit(data);
        this.submitted = false;
      },
      error: () => {
        this.submitted = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to fetch taxes',
        });
      }
    });
  }





}
