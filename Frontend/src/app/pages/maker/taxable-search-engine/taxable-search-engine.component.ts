import { Component } from '@angular/core';
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
import { TaxableSearchEngineService } from '../../../service/maker/taxable-search-engine-service';
import { Tax } from '../../../models/maker/tax';

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

  constructor(
    private fb: FormBuilder,
    private storageService: StorageService,
    private taxCategoriesService: TaxCategoriesService,
    private messageService: MessageService,
    private branchService: BranchService,
    private taxableSearchEngineService: TaxableSearchEngineService,
  ) { }


  ngOnInit(): void {
    this.user = this.storageService.getUser();
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
    });
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

  onSubmit() {
    this.submitted = true;

    // ✅ Get form values instead of directly using this.taxCategory
    this.taxableSearchEngine = {
      ...this.form.value,
      user_id: this.user.id
    };

    this.taxableSearchEngineService.getTaxes(this.taxableSearchEngine).subscribe({
      next: (data) => {
        this.taxes = data;
        this.submitted = false;
        this.form.reset();
        this.taxableSearchEngine = new TaxableSearchEngine();
      },
      error: (error) => {
        console.log(error);
        this.submitted = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to submit taxable search enginee',
        });
      },
    });
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
  }




}
