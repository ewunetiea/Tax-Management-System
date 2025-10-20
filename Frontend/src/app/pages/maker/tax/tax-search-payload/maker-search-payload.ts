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
  @Output() searchResults = new EventEmitter<Tax[]>(); // Declare the event emitter

  @Input() routeControl: string = ''; // Accept routeControl from parent

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
      { name: 'Waiting For Approval', id: '0' },
      { name: 'Approve', id: '1' },
      { name: 'Rejected', id: '2' },
      { name: 'Reviewed', id: '4' },
      { name: 'Approved', id: '5' },
    ]

    console.log(this.routeControl)

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
    this.submitted = true;

    this.payload.routeControl = this.routeControl ?? '';
    const user = this.storageService.getUser();
    this.payload.user_id = user ? user.id : 0;
    const makerFormattedDates = this.payload.maker_date?.map(date => {
      const d = new Date(date);
      return new Date(`${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`);
    }) || [];


    const approverFormattedDates = this.payload.checked_date?.map(date => {
      const d = new Date(date);
      return new Date(`${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`);
    }) || [];

    this.payload.maker_date = makerFormattedDates;

     this.payload.checked_date = approverFormattedDates;


    this.taxService.fetchTaxesBasedOnStatus(this.payload).subscribe({
      next: (data) => {
        this.taxes = data;
        this.searchResults.emit(this.taxes);

        if (this.taxes.length > 0) {
          this.messageService.add({
            severity: 'success',
            summary: 'Search Complete',
            detail: `${this.taxes.length} record(s) found.`,
          });
        } else {
          this.messageService.add({
            severity: 'info',
            summary: 'No Results',
            detail: 'No tax data found for your search criteria.',
          });
        }

        this.submitted = false;
        this.payload = new MakerSearchPayload();
      },
      error: (error) => {
        console.error(error);
        this.submitted = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to fetch tax data. Please try again later.',
        });
      },
    });
  }
  onReset(): void {
    this.submitted = false;

    this.payload = new MakerSearchPayload()
  }

}
