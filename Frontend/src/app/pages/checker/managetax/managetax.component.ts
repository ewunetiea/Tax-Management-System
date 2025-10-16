import { Component, OnInit } from '@angular/core';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { SharedUiModule } from '../../../../shared-ui';
import { StorageService } from '../../../service/sharedService/storage.service';
import { ManageTaxService } from '../../../service/checker/manage_tax_service';
import { User } from '../../../models/admin/user';
import { Table } from 'primeng/table';
import { Tax } from '../../../models/maker/tax';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { PaginatorPayLoad } from '../../../models/admin/paginator-payload';
import { RejectCheckerApproverComponent } from '../reject-checker-approver/reject-checker-approver.component';
import { TaxCreateEditComponent } from '../../maker/tax/tax-create-edit/tax-create-edit.component';
import { TaxableSearchEngineComponent } from '../../common/taxable-search-engine/taxable-search-engine.component';

@Component({
  selector: 'app-managetax',
  providers: [MessageService, ConfirmationService],
  imports: [SharedUiModule, RejectCheckerApproverComponent, TaxableSearchEngineComponent, TaxCreateEditComponent],
  templateUrl: './managetax.component.html',
  styleUrl: './managetax.component.scss'
})
export class ManagetaxComponent implements OnInit {
  paginatorPayLoad: PaginatorPayLoad = new PaginatorPayLoad();
  expandedRows: { [key: number]: boolean } = {};
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
  rejectTaxDialog = false;
  outputRejectedTax: any[] = [];
  tax: Tax = new Tax();
  fetching = false;
  taxDialog = false
  isEdit = false;

  constructor(
    private manageTaxService: ManageTaxService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private storageService: StorageService,
    private route: ActivatedRoute,
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
      this.setStatusRoute(); // detect route change
    });

    // Watch route param changes dynamically
    // this.route.paramMap.subscribe(params => {
    //   this.statusRoute = params.get('status') || 'pending';
    //   this.getTaxes(); 
    // });
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

  /** Fetch taxes by status type */
  getTaxes(): void {
    this.loading = true;
    this.taxes = [];

    let request$;
    switch (this.statusRoute) {
      case 'pending':
        request$ = this.manageTaxService.getPendingTaxes(this.paginatorPayLoad);
        break;
      case 'rejected':
        request$ = this.manageTaxService.getRejectedTaxes(this.paginatorPayLoad);
        break;
      case 'approved':
        request$ = this.manageTaxService.getApprovedTaxes(this.paginatorPayLoad);
        break;
      default:
        request$ = this.manageTaxService.getPendingTaxes(this.paginatorPayLoad);
        break;
    }

    request$
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (data) => {
          this.taxes = data;
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to fetch tax records. Please try again later.',
            life: 2500
          });
        }
      });
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
  reviewSelectedTaxes(): void {
    this.selectedTaxes.forEach(tax => {
      tax.user_id = this.user.id;
      tax.checker_name = this.user.email?.split('@')[0] || '';
    });
    this.confirmationService.confirm({
      message: 'Are you sure you want to review the selected taxes ?',
      header: 'Confirm Review',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.manageTaxService.reviewTaxes(this.selectedTaxes).pipe(
          finalize(() => (this.loading = false))
        ).subscribe({
          next: () => {
            this.getTaxes();
            this.selectedTaxes = [];
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: 'Selected taxes have been successfully reviewed',
              life: 3000
            });
          },
          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to review selected taxes. Try again.',
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
  reviewTax(tax: Tax) {
    tax.user_id = this.user.id;
    tax.checker_name = this.user.email?.split('@')[0] || '';

    this.confirmationService.confirm({
      message: `Are you sure you want to review the <strong>${tax.reference_number}</strong>?`,
      header: 'Confirm Review',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.manageTaxService.reviewTaxes([tax]).pipe(
          finalize(() => (this.loading = false))
        ).subscribe({
          next: () => {
            this.getTaxes();
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: `Tax ${tax.reference_number} has been successfully reviewed.`,
              life: 3000
            });
          },
          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to review the tax. Please try again later.',
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
      this.getTaxes();
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
