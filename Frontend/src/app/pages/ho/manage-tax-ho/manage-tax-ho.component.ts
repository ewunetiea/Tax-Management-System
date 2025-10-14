import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem, ConfirmationService, MessageService } from 'primeng/api';
import { finalize } from 'rxjs';
import { PaginatorPayLoad } from '../../../models/admin/paginator-payload';
import { User } from '../../../models/admin/user';
import { Tax } from '../../../models/maker/tax';
import { StorageService } from '../../../service/sharedService/storage.service';
import { Table } from 'primeng/table';
import { SharedUiModule } from '../../../../shared-ui';
import { RejectCheckerApproverComponent } from '../../checker/reject-checker-approver/reject-checker-approver.component';
import { ManageTaxHoService } from '../../../service/ho/manage-tax-ho-service';
import { TaxCreateEditComponent } from '../../maker/tax/tax-create-edit/tax-create-edit.component';
import { TaxableSearchEngineComponent } from '../../common/taxable-search-engine/taxable-search-engine.component';

@Component({
  selector: 'app-manage-tax-ho',
  providers: [MessageService, ConfirmationService],
  imports: [SharedUiModule, RejectCheckerApproverComponent, TaxableSearchEngineComponent, TaxCreateEditComponent],
  templateUrl: './manage-tax-ho.component.html',
  styleUrl: './manage-tax-ho.component.scss'
})
export class ManageTaxHoComponent {
  sizes!: any[];
  selectedSize: any = 'normal';
  items: MenuItem[] | undefined;
  home: MenuItem | undefined;
  breadcrumbText: string = 'Manage Taxes';
  user: User = new User();
  taxes: Tax[] = [];
  selectedTaxes: Tax[] = [];
  loading = true;
  statusRoute: string = '';
  paginatorPayLoad: PaginatorPayLoad = new PaginatorPayLoad();
  rejectTaxDialog = false;
  outputRejectedTax: any[] = [];
  tax: Tax = new Tax();
  fetching = false;
  taxDialog = false
  isEdit = false;

  constructor(
    private manageTaxHoService: ManageTaxHoService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private storageService: StorageService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.user = this.storageService.getUser();
    this.paginatorPayLoad.branch_id = this.user.branch?.id;
    this.paginatorPayLoad.user_id = this.user.id;

    this.home = { icon: 'pi pi-home', routerLink: '/' };
    this.items = [{ label: this.breadcrumbText }];
    this.sizes = [
      { name: 'Small', value: 'small' },
      { name: 'Normal', value: 'normal' },
      { name: 'Large', value: 'large' }
    ];


    this.setStatusRoute();
    this.router.events.subscribe(() => {
      this.setStatusRoute(); 
    });
  }

  setStatusRoute() {
    const currentRoute = this.router.url.toLowerCase();
    if (currentRoute.includes('approved')) this.statusRoute = 'approved';
    else if (currentRoute.includes('rejected')) this.statusRoute = 'rejected';
    else this.statusRoute = 'pending';

    // 🧹 reset when route changes
    this.taxes = [];
    this.fetching = false;
  }

  onDataGenerated(data: Tax[]) {
    this.taxes = data;
    this.fetching = true;
    this.loading = false;
  }


  /** Clear table filters */
  clear(table: Table): void {
    table.clear();
  }

  /** Global search filter */
  onGlobalFilter(table: Table, event: Event): void {
    const input = event.target as HTMLInputElement;
    table.filterGlobal(input.value, 'contains');
  }

  /** Review multiple selected taxes */
  approveSelectedTaxes(): void {
    this.selectedTaxes.forEach(tax => {
      tax.user_id = this.user.id;
      tax.approver_name = this.user.email?.split('@')[0] || '';
    });
    this.confirmationService.confirm({
      message: 'Are you sure you want to review the selected taxes ?',
      header: 'Confirm Review',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.manageTaxHoService.approveTaxes(this.selectedTaxes).pipe(
          finalize(() => (this.loading = false))
        ).subscribe({
          next: () => {
            this.selectedTaxes = [];
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: 'Selected taxes have been successfully approved',
              life: 3000
            });
          },
          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to approve selected taxes. Try again.',
              life: 3000
            });
          }
        });
      }
    });
  }

  editTax(tax: Tax) {
    this.tax = { ...tax };
    this.taxDialog = true;
    this.isEdit = true;
  }

  hideDialog() {
    this.taxDialog = false;
  }

  onTaxesaved(saveTax: Tax) {
    if (this.isEdit) {
      const index = this.taxes.findIndex(a => a.mainGuid === saveTax.mainGuid);
      if (index !== -1) {
        this.taxes[index] = saveTax;
      }
    } else {
      this.taxes = [saveTax, ...this.taxes];
    }

    this.taxDialog = false;
    this.tax = {} as Tax;
  }

  /** Review single tax record */
  approveTax(tax: Tax) {
    tax.user_id = this.user.id;
    tax.approver_name = this.user.email?.split('@')[0] || '';

    this.confirmationService.confirm({
      message: `Are you sure you want to approve the <strong>${tax.reference_number}</strong>?`,
      header: 'Confirm Review',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.manageTaxHoService.approveTaxes([tax]).pipe(
          finalize(() => (this.loading = false))
        ).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: `Tax ${tax.reference_number} has been successfully approved`,
              life: 3000
            });
          },
          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to approve the tax. Please try again later.',
              life: 3000
            });
          }
        });
      }
    });
  }

  rejectTax(tax: Tax) {
    this.outputRejectedTax = [];
    this.tax = { ...tax };
    this.outputRejectedTax.push(this.tax);
    this.rejectTaxDialog = true;
  }

  onDataChange(data: any) {
    if (data[1]) {
      this.taxes[this.findIndexById(data[0].id)] = data[0];
    } else {
      this.taxes = [...this.taxes];
      this.tax = new Tax();
    }
    this.rejectTaxDialog = false;
  }


  findIndexById(id: number): number {
    let index = -1;
    for (let i = 0; i < this.taxes.length; i++) {
      if (this.taxes[i].id === id) {
        index = i;
        break;
      }
    }
    return index;
  }

}
