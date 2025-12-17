import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject} from 'rxjs';
import { Tax } from 'app/models/maker/tax';
import { User } from 'app/models/admin/user';
import { PaginatorPayLoad } from 'app/models/admin/paginator-payload';
import { SharedUiModule } from 'shared-ui';
import { SearchEngineComponent } from '../search-engine/search-engine.component';
import { Table } from 'primeng/table';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { FileDownloadService } from 'app/service/maker/file-download-service';

@Component({
  selector: 'app-manage-tax',
  standalone: true,
  imports: [SearchEngineComponent, SharedUiModule],
  templateUrl: './manage-tax.component.html',
  styleUrls: ['./manage-tax.component.scss']
})
export class ManageTaxComponent implements OnInit, OnDestroy {
  statusRoute: string = '';
  taxes: Tax[] = [];
  fetching = false;
  loading = true;
  sizes!: any[];
  selectedSize: any = 'normal';
  user: User = new User();
  activeIndex1: number = 0;
  activeState: boolean[] = [true, false, false, false, false];
  paginatorPayLoad: PaginatorPayLoad = new PaginatorPayLoad();
  pdfSrc: any;
  selectedPdf: SafeResourceUrl | null = null; 
  showPdfModal = false;
  isDialogVisible = false;
  totalRecords: any = 0;
  expandedRows: { [key: number]: boolean } = {};

  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private fileDownloadService: FileDownloadService,
    private sanitizer: DomSanitizer,
  ) { }

  ngOnInit(): void {
    this.sizes = [
      { name: 'Small', value: 'small' },
      { name: 'Normal', value: 'normal' },
      { name: 'Large', value: 'large' }
    ];

    this.setStatusRoute();
  }

  setStatusRoute() {
    const currentRoute = this.router.url.toLowerCase();
    if (currentRoute.includes('general')) this.statusRoute = 'general';
    else this.statusRoute = 'pending';

    // 🧹 reset when route changes
    this.taxes = [];
    this.fetching = false;
  }

onDataGenerated(event: { data: Tax[], totalRecords: number, fetching: boolean }) {
  this.taxes = event.data;
  this.totalRecords = event.totalRecords;
  this.fetching = event.fetching;
  this.loading = false;
}

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  clear(table: Table): void {
    table.clear();
  }

  /** Global search filter */
  onGlobalFilter(table: Table, event: Event): void {
    const input = event.target as HTMLInputElement;
    table.filterGlobal(input.value, 'contains');
  }

  
  toggle(index: number) {
    this.activeState = this.activeState.map((_, i) => i === index ? !this.activeState[i] : false);
  }

  onImageError(event: any) {
        console.error('Image failed to load', event);
    }

    openDialogImage() {
        this.isDialogVisible = true;
    }
    
    closeModalPDF() {
      this.showPdfModal = false;
      this.selectedPdf = null;
    }

    downloadWord(file: any) {
        try {
            let byteArray: Uint8Array;

            if (typeof file.file === 'string') {
                // Remove data URL prefix if it exists
                const base64 = file.file.includes(',') ? file.file.split(',')[1] : file.file;

                // Decode base64 safely
                const binary = atob(base64.replace(/\s/g, ''));
                byteArray = new Uint8Array(binary.length);

                for (let i = 0; i < binary.length; i++) {
                    byteArray[i] = binary.charCodeAt(i);
                }

                // Debug: Log the decoded binary and byte array


            } else {
                // If it's already a Blob, use it directly
                const blob = new Blob([file.file], { type: file.fileType });


                const link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = file.fileName;
                link.click();
                window.URL.revokeObjectURL(link.href);
                return; // Exit the function
            }

            const blob = new Blob([byteArray as any], { type: file.fileType });
            // Debug: Log the blob size

            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = file.fileName;
            link.click();
            window.URL.revokeObjectURL(link.href);
        } catch (e) {
            console.error('Failed to download Word file', e);
            alert('Cannot download file. The data may be corrupted.');
        }
    }

     downloadPdf(file: any) {
        if (!file.pdfUrl && !file.fileType) return;

        // If we have the blob stored (recommended)
        this.fileDownloadService.fetchTaxFileByFileName(file.fileName).subscribe((blob: Blob) => {
            const link = document.createElement('a');
            const blobUrl = URL.createObjectURL(blob); // create object URL from blob
            link.href = blobUrl;
            link.download = file.fileName || 'document.pdf';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            URL.revokeObjectURL(blobUrl); // cleanup
        });
    }

     previewPdf(file: any) {
    if (file.pdfUrl) {
      this.selectedPdf = file.pdfUrl;
      this.showPdfModal = true;
    }
  }

   onRowCollapse(event: any) {
    const tax = event.data;
    delete this.expandedRows[tax.Id];
  }

  onRowExpand(event: any) {
    const tax = event.data;
    if (!tax.taxFile || tax.taxFile.length === 0) {
      return;
    }

    const fileFetchPromises = tax.taxFile.map((file: any) => {
      if (!file?.fileName) return Promise.resolve(null);

      return this.fileDownloadService.fetchTaxFileByFileName(file.fileName).toPromise()
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
}
