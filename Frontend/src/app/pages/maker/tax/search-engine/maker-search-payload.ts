import { Component, EventEmitter, Input, Output } from '@angular/core';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { MessageService } from 'primeng/api';
import { TaxCategoriesService } from '../../../../service/maker/tax-categories-service';
import { User } from '../../../../models/admin/user';
import { SharedUiModule } from '../../../../../shared-ui';
import { BranchService } from '../../../../service/admin/branchService';
import { Branch } from '../../../../models/admin/branch';
import { TaxCategory } from '../../../../models/maker/tax-category';
import { Tax } from '../../../../models/maker/tax';
import { TaxService } from '../../../../service/maker/tax-service';
import { MakerSearchPayload } from '../../../../models/payload/maker-search-payload';
import { InputSanitizer } from 'app/SQLi-XSS-Prevention/InputSanitizer';

@Component({
  standalone: true,
  selector: 'app-maker-search-payload',
  imports: [SharedUiModule],
  templateUrl: './maker-search-payload.component.html',
  styleUrl: './maker-search-payload.component.scss'
})


export class MakerSearchEnginePayLoadComponent {
  payload: MakerSearchPayload = new MakerSearchPayload()


  user: User = new User();
  branches: Branch[] = [];
  submitted = false;
  taxCategories: TaxCategory[] = [];
  taxes: Tax[] = [];

  branchLoading = false;
  taxCategoryLoading = false;
  status: any[] | undefined;
  // @Output() searchResults = new EventEmitter<Tax[]>(); // Declare the event emitter

  @Output() searchPayload = new EventEmitter<MakerSearchPayload>();

@Output() loadingState = new EventEmitter<boolean>(); // NEW

  @Input() routeControl: string = ''; // Accept routeControl from parent
  @Input() loading: boolean = false; // <-- parent controls this
    invalidXss = false;

  constructor(
    private storageService: StorageService,
    private taxCategoriesService: TaxCategoriesService,
    private messageService: MessageService,
    private branchService: BranchService,
    private taxService: TaxService

  ) { }


  ngOnInit(): void {
    this.status = [
      { name: 'Drafted', id: '6' },
      { name: 'Waiting ', id: '0' },
      { name: 'Sent', id: '1' },
      { name: 'Rejected', id: '2' },
      { name: 'Settled', id: '5' },
    ]

    this.payload.branch_name = this.storageService.getUser().branch.name
    this.payload.branch_id = this.storageService.getUser().branch.id

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

    
        if (this.invalidXss) {
            return; // stop if form is invalid
        }
  this.submitted = true;

  const user = this.storageService.getUser();
  this.payload.user_id = user ? user.id : 0;
  this.payload.routeControl = this.routeControl ?? '';

  // Keep your existing formatting logic
  const draftedFormattedDates = (this.payload.drafted_date || [])
    .filter(date => !!date)
    .map(date => {
      const d = new Date(date);
      return new Date(`${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`);
    });

  const makerFormattedDates = (this.payload.maker_date || [])
    .filter(date => !!date)
    .map(date => {
      const d = new Date(date);
      return new Date(`${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`);
    });

  const approverFormattedDates = (this.payload.checked_date || [])
    .filter(date => !!date)
    .map(date => {
      const d = new Date(date);
      return new Date(`${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`);
    });

  // Duplicate if only one date is selected
  this.payload.drafted_date = draftedFormattedDates.length === 1 ? [draftedFormattedDates[0], draftedFormattedDates[0]] : draftedFormattedDates;
  this.payload.maker_date = makerFormattedDates.length === 1 ? [makerFormattedDates[0], makerFormattedDates[0]] : makerFormattedDates;
  this.payload.checked_date = approverFormattedDates.length === 1 ? [approverFormattedDates[0], approverFormattedDates[0]] : approverFormattedDates;


this.searchPayload.emit(this.payload);

  // const serviceCall =  this.taxService.fetchTaxesBasedOnStatus(this.payload);

  // serviceCall.subscribe({
  //   next: (data) => {
  //     this.taxes = data;
  //     this.searchResults.emit(this.taxes);

  //     this.messageService.add({
  //       severity: data.length > 0 ? 'success' : 'info',
  //       summary: data.length > 0 ? 'Search Complete' : 'No Results',
  //       detail: data.length > 0 ? `${data.length} record(s) found.` : 'No tax data found for your search criteria.',
  //     });

  //     this.submitted = false;
  //   },
  //   error: (error) => {
  //     console.error(error);
  //     this.submitted = false;
  //     this.messageService.add({
  //       severity: 'error',
  //       summary: 'Error',
  //       detail: 'Failed to fetch tax data. Please try again later.',
  //     });
  //   },
  // });
}

  
  onReset(): void {
    this.submitted = false;

    this.payload.maker_date = []
    this.payload.checked_date = []
    this.payload.rejected_date = []
    this.payload.reference_number = ''
    this.payload.tax_category_id = 0



  }



    onRemarkChange(event: any) {
          const value = event.target.value;
  
          // Check if value contains XSS
          this.invalidXss = InputSanitizer.isInvalid(value);
  
          // Only update model if valid
          if (!this.invalidXss) {
              this.payload.reference_number = value;
          }
  
      }

}
