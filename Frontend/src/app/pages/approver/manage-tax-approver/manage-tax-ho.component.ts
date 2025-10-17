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
import { RejectCheckerApproverComponent } from '../../reviewer/reject-checker-approver/reject-checker-approver.component';
import { ManageTaxApproverService } from '../../../service/approver/manage-tax-ho-service';
import { TaxCreateEditComponent } from '../../maker/tax/tax-create-edit/tax-create-edit.component';
import { TaxableSearchEngineComponent } from '../../common/taxable-search-engine/taxable-search-engine.component';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { FileDownloadService } from '../../../service/maker/file-download-service';

@Component({
  selector: 'app-manage-tax-ho',
  providers: [MessageService, ConfirmationService],
  imports: [SharedUiModule, RejectCheckerApproverComponent, TaxableSearchEngineComponent, TaxCreateEditComponent],
  templateUrl: './manage-tax-ho.component.html',
  styleUrl: './manage-tax-ho.component.scss'
})
export class ManageTaxHoComponent {
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
  activeIndex1: number = 0;
  activeState: boolean[] = [true, false, false];
  pdfSrc: any;
  selectedPdf: SafeResourceUrl | null = null; // PDF to preview
  showPdfModal = false;

  constructor(
    private manageTaxHoService: ManageTaxApproverService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private storageService: StorageService,
    private router: Router,
    private fileDownloadService: FileDownloadService,
    private sanitizer: DomSanitizer,
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

  onDataGenerated(event: { data: Tax[]; fetching: boolean }): void {
    this.taxes = event.data;
    this.fetching = event.fetching;
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


  toggle(index: number) {
    this.activeState = this.activeState.map((_, i) => i === index ? !this.activeState[i] : false);
  }


  previewPdf(file: any) {
    if (file.pdfUrl) {
      this.selectedPdf = file.pdfUrl;
      this.showPdfModal = true;
    }
  }



  downloadPdf(file: any) {
    if (!file.pdfUrl && !file.fileType) return;

    // If we have the blob stored (recommended)
    this.fileDownloadService.fetchFileByFileName(file.fileName).subscribe((blob: Blob) => {
      const link = document.createElement('a');
      const blobUrl = URL.createObjectURL(blob); // create object URL from blob
      link.href = blobUrl;
      link.download = file.fileName || 'document.pdf';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(blobUrl);
    });
  }

  closeModal() {
    this.showPdfModal = false;
    this.selectedPdf = null;
  }


  onRowExpand(event: any) {
    const tax = event.data;


    if (!tax.taxFile || tax.taxFile.length === 0) {
      return;
    }

    const fileFetchPromises = tax.taxFile.map((file: any) => {
      if (!file?.fileName) return Promise.resolve(null);

      return this.fileDownloadService.fetchFileByFileName(file.fileName).toPromise()
        .then((blob: Blob | undefined) => {
          if (!blob) {
            console.warn(`No blob returned for file: ${file.fileName}`);
            return null;
          }

          const newFile = { ...file };
          newFile.fileType = blob.type;

          // PDF
          if (blob.type === 'application/pdf') {
            // Revoke previous URL if exists
            if (newFile.pdfUrl) {
              URL.revokeObjectURL(
                (newFile.pdfUrl as any).changingThisBreaksApplicationSecurity
              );
            }
            const blobUrl = URL.createObjectURL(blob);
            newFile.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(blobUrl);
            newFile.file = null;
            return newFile;
          }

          // Image
          if (blob.type.startsWith('image/')) {
            return new Promise((resolve) => {
              const reader = new FileReader();
              reader.onload = (e: any) => {
                newFile.file = e.target.result.split(',')[1]; // base64
                newFile.pdfUrl = null;
                resolve(newFile);
              };
              reader.readAsDataURL(blob);
            });
          }

          // Word or other files
          newFile.file = blob;
          newFile.pdfUrl = null;
          return newFile;
        })
        .catch((error) => {
          console.error('Error fetching file:', error);
          return null;
        });
    });

    Promise.all(fileFetchPromises).then((results) => {
      tax.taxFile = results.filter(file => file !== null);

      // Force change detection for PDFs
      setTimeout(() => {

      }, 0);
    });
  }

  onRowCollapse(event: any) {
    const tax = event.data;
    delete this.expandedRows[tax.Id];
  }

}
