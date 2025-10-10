


import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';

import { HttpErrorResponse } from '@angular/common/http';
import { ProductService } from '../../../service/product.service';
import { SharedUiModule } from '../../../../shared-ui';

import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { TaxCreateEditComponent } from '../tax-rule/tax-create-edit.component';
import { Tax } from '../../../models/maker/tax';
import { TaxService } from '../../../service/maker/tax-service';
import { StorageService } from '../../../service/sharedService/storage.service';
import { FileDownloadService } from '../../../service/maker/file-download-service';



interface Column {
    field: string;
    header: string;
    customExportHeader?: string;
}

interface ExportColumn {
    title: string;
    dataKey: string;
}


@Component({
    selector: 'app-manage-tax',
    standalone: true,
    imports: [
        SharedUiModule, TaxCreateEditComponent
    ],
    templateUrl: './manage-tax.component.html',
    styleUrls: ['./manage-tax.component.css'],

    providers: [ConfirmationService, MessageService, ProductService]
})
export class ManageTax implements OnInit {

    expandedRows: { [key: number]: boolean } = {};
    selectedPdf: SafeResourceUrl | null = null; // PDF to preview
    showPdfModal = false;
    taxDialog: boolean = false;
    taxes: Tax[] = [];
    tax!: Tax;
    selectedTaxes!: Tax[] | null;
    submitted: boolean = false;
    statuses!: any[];
    @ViewChild('dt') dt!: Table;
    exportColumns!: ExportColumn[];
    cols!: Column[];
    uploadedFiles: any[] = [];

    maker_name: String = ''
    isEdit = false;
    activeIndex1: number = 0;
    activeState: boolean[] = [true, false, false];
    pdfSrc: any;

    sizes!: any[];
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Manage Tax';
    items: MenuItem[] | undefined;

    rowToggles: { [id: number]: { message: boolean; file: boolean } } = {};

    status!: string;
    loading: boolean = true;

    constructor(
        private taxService: TaxService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private sanitizer: DomSanitizer,
        private route: ActivatedRoute,
        private staorageService: StorageService,
        private fileDownloadService: FileDownloadService,
    ) { }

    ngOnInit(): void {

        this.maker_name = this.staorageService.getUser().username
        // this.route.snapshot.data['status'];

        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];

        this.loadTaxes(this.maker_name);
    }


    loadTaxes(maker_name: String) {
        this.taxService.fetchTaxes(maker_name).subscribe(
            (response) => {


                console.log(response)
                this.taxes = (response as any).map((announcement: any) => {
                    // Detect file type from base64
                    const fileType = this.getFileType(announcement.image);
                    announcement.fileType = fileType;

                    // Prepare PDF blob URL if PDF
                    if (fileType === 'application/pdf') {
                        const byteCharacters = atob(announcement.image);
                        const byteNumbers = new Array(byteCharacters.length);
                        for (let i = 0; i < byteCharacters.length; i++) {
                            byteNumbers[i] = byteCharacters.charCodeAt(i);
                        }
                        const byteArray = new Uint8Array(byteNumbers);
                        const blob = new Blob([byteArray], { type: 'application/pdf' });

                        const url = URL.createObjectURL(blob);
                        announcement.pdfUrl = url;
                        announcement.safePdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url) as SafeResourceUrl;
                    }
                    this.loading = false;


                    return announcement;
                });
            },
            (error: HttpErrorResponse) => {
                this.messageService.add({
                    severity: 'error',
                    summary:
                        error.status === 401
                            ? 'You are not permitted to perform this action!'
                            : 'Something went wrong while fetching announcements!',
                    detail: '',
                });
            }
        );
    }


    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.tax = { created_date: new Date() } as Tax; // default date avoids null crash

        this.submitted = false;
        this.taxDialog = true;
        this.isEdit = false
    }

    editTax(tax: Tax) {
        this.tax = { ...tax };
        this.taxDialog = true;

        this.isEdit = true;
    }

    deleteTaxes(taxes: Tax | Tax[] | null) {
        // Normalize to array
        const itemsToDelete = Array.isArray(taxes) ? taxes : [taxes];

        this.confirmationService.confirm({
            message: `Are you sure you want to delete ${itemsToDelete.length} tax(es)?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.taxService.deleteSelectedTaxes(itemsToDelete as any).subscribe({
                    next: () => {
                        // Remove deleted items from local list
                        this.taxes = this.taxes.filter(
                            val => !itemsToDelete.includes(val)
                        );
                        // Clear selection if it was a bulk delete
                        if (Array.isArray(taxes)) {
                            this.selectedTaxes = null;
                        }
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'taxes Deleted',
                            life: 3000
                        });
                    },
                    error: (err) => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error',
                            detail: 'Failed to delete taxes',
                            life: 3000
                        });
                        console.error(err);
                    }
                });
            }
        });
    }



    hideDialog() {
        this.taxDialog = false;
        this.submitted = false;
    }



    findIndexById(mainGuid: String): number {
        return this.taxes.findIndex((p) => p.mainGuid === mainGuid);
    }




    getSeverity(status: string) {
        switch (status) {
            case 'INSTOCK': return 'success';
            case 'LOWSTOCK': return 'warn';
            case 'OUTOFSTOCK': return 'danger';
            default: return 'info';
        }
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


    getFileType(base64: string): string {
        if (!base64) return '';

        // Common base64 prefixes
        if (base64.startsWith('/9j/')) return 'image/jpeg'; // JPEG
        if (base64.startsWith('iVBOR')) return 'image/png'; // PNG
        if (base64.startsWith('JVBER')) return 'application/pdf'; // PDF
        if (base64.startsWith('UEsDB')) return 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'; // DOCX
        if (base64.startsWith('0M8R4')) return 'application/msword'; // DOC

        return 'unknown';
    }



    toggle(index: number) {
        this.activeState = this.activeState.map((_, i) => i === index ? !this.activeState[i] : false);
    }


    clear(table: Table) {
        table.clear();
    }


    onRowExpand(event: any) {
        const tax = event.data;
        const file = tax.taxFile?.[0];

        if (!file?.fileName) {
            return;
        }

        this.fileDownloadService.fetchFileByFileName(file.fileName).subscribe((blob: Blob) => {
            console.log('Received Blob:', blob);

            const newFile = { ...file };
            newFile.fileType = blob.type;

            if (blob.type === 'application/pdf') {
                console.log('Blob type is PDF, processing for PDF.');
                newFile.file = null; // clear image
                const blobUrl = URL.createObjectURL(blob);
                newFile.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(blobUrl); // ✅ sanitize
            } else if (blob.type.startsWith('image/')) {
                const reader = new FileReader();
                reader.onload = (e: any) => {
                    newFile.file = e.target.result.split(',')[1]; // base64
                    newFile.pdfUrl = null;


                    tax.taxFile = [newFile];
                };
                reader.readAsDataURL(blob);
                return; // exit early since assignment happens in onload
            } else {
                newFile.file = blob; // Word or other types
                newFile.pdfUrl = null;
            }


            tax.taxFile = [newFile]; // replace array to trigger change detection
        }, error => {
            console.error('Error fetching file:', error);
        });
    }



    onRowCollapse(event: any) {
        const tax = event.data;
        delete this.expandedRows[tax.Id];
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
            URL.revokeObjectURL(blobUrl); // cleanup
        });
    }



    closeModal() {
        this.showPdfModal = false;
        this.selectedPdf = null;
    }

}
