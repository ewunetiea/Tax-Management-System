import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { ProductService } from '../../../../service/product.service';
import { SharedUiModule } from '../../../../../shared-ui';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { TaxCreateEditComponent } from '../tax-create-edit/tax-create-edit.component';
import { Tax } from '../../../../models/maker/tax';
import { TaxService } from '../../../../service/maker/tax-service';
import { FileDownloadService } from '../../../../service/maker/file-download-service';
import { ButtonSeverity } from 'primeng/button';
import { MakerSearchEnginePayLoadComponent } from '../search-engine/maker-search-payload';
import { StorageService } from 'app/service/sharedService/storage.service';

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
    imports: [SharedUiModule, TaxCreateEditComponent, MakerSearchEnginePayLoadComponent],
    templateUrl: './manage-tax.component.html',
    styleUrls: ['./manage-tax.component.css'],
    providers: [ConfirmationService, MessageService, ProductService]
})
export class ManageTax implements OnInit {
    expandedRows: { [key: number]: boolean } = {};
    selectedPdf: SafeResourceUrl | null = null; 
    showPdfModal = false;
    taxDialog: boolean = false;
    taxes: Tax[] = [];
    tax: Tax = new Tax();
    selectedTaxes!: Tax[] | null;
    submitted: boolean = false;
    statuses!: any[];
    @ViewChild('dt') dt!: Table;
    exportColumns!: ExportColumn[];
    uploadedFiles: any[] = [];
    isEdit = false;
    activeIndex1: number = 0;
    activeState: boolean[] = [true, false, false, false, false];
    pdfSrc: any;
    sizes!: any[];
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Manage Tax';
    items: MenuItem[] | undefined;
    rowToggles: { [id: number]: { message: boolean; file: boolean } } = {};
    status!: string;
    loading: boolean = true;
    routeControl: string = ''
    isDialogVisible = false;
    roles: String = ''

    constructor(
        private taxService: TaxService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private sanitizer: DomSanitizer,
        private route: ActivatedRoute,
        private fileDownloadService: FileDownloadService,
        private storageService?: StorageService
    ) { }

    ngOnInit(): void {
        this.roles = this.storageService?.getUser().roles
        this.routeControl = this.route.snapshot.data['status'];
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
    }


    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }



    onSearchResults(taxes: Tax[]) { //  search results are passed from child to parent
        this.taxes = taxes;
        

        
        this.loading = false;

    }

    onTaxesaved(saveTax: Tax) { // newly created tax are passed from parent to child

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



    openNew() {
        this.tax = { created_date: new Date() } as Tax;
        this.submitted = false;
        this.taxDialog = true;
        this.isEdit = false
    }

    editTax(tax: Tax) {
        this.tax = { ...tax };
        this.tax.drafted_date = new Date(this.tax.drafted_date as any);
        this.taxDialog = true;
        this.tax.previouseTaxFile = tax.taxFile
        this.tax.taxFile = [];
        this.isEdit = true;
    }

    submitToBranchManager(taxes: Tax | Tax[] | null) {
        const itemsToSubmit = Array.isArray(taxes) ? taxes : [taxes];
        this.confirmationService.confirm({
            message: `Are you sure you want to submit ${itemsToSubmit.length} tax(es)?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.taxService.submitToBranchManager(itemsToSubmit as any).subscribe({
                    next: () => {
                        this.taxes = this.taxes.filter(val => !itemsToSubmit.includes(val));
                        if (Array.isArray(taxes)) {
                            this.selectedTaxes = null;
                        }
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'selected tax (es) submited',
                            life: 3000
                        });
                    },
                    error: (err) => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error',
                            detail: 'Failed to submit tax (es)',
                            life: 3000
                        });
                        console.error(err);
                    }
                });
            }
        });
    }

    backToDraftedState(taxes: Tax | Tax[] | null) {
        const itemsToBack = Array.isArray(taxes) ? taxes : [taxes];

        this.confirmationService.confirm({
            message: `Are you sure you want to back to drafted  state  ${itemsToBack.length} tax(es)?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.taxService.backtoDraftState(itemsToBack as any).subscribe({
                    next: () => {
                        this.taxes = this.taxes.filter(
                            val => !itemsToBack.includes(val)
                        );
                        if (Array.isArray(taxes)) {
                            this.selectedTaxes = null;
                        }
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'taxes back to drafted',
                            life: 3000
                        });
                    },
                    error: (err) => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error',
                            detail: 'Failed to back tax(es)',
                            life: 3000
                        });
                        console.error(err);
                    }
                });
            }
        });
    }

    deleteTaxes(taxes: Tax | Tax[] | null) {
        const itemsToDelete = Array.isArray(taxes) ? taxes : [taxes];

        this.confirmationService.confirm({
            message: `Are you sure you want to delete ${itemsToDelete.length} tax(es)?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.taxService.deleteSelectedTaxes(itemsToDelete as any).subscribe({
                    next: () => {
                        this.taxes = this.taxes.filter(
                            val => !itemsToDelete.includes(val)
                        );
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






    findIndexById(mainGuid: String): number {
        return this.taxes.findIndex((p) => p.mainGuid === mainGuid);
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


        if (!tax.taxFile || tax.taxFile.length === 0) {
            return;
        }

        const fileFetchPromises = tax.taxFile.map((file: any) => {
            if (!file?.fileName) return Promise.resolve(null);

            return this.fileDownloadService.fetchFileByFileName(file.fileName).toPromise()
                .then((blob: Blob | undefined) => {
                    // Log the fetched blob
                    if (!blob) {
                        console.warn(`No blob returned for file: ${file.fileName}`);
                        return null;
                    }

                    const newFile = { ...file };
                    const fileExtension = file.fileName.split('.').pop().toLowerCase();

                    // Set fileType based on the filename extension
                    const typeMapping: Record<string, string> = {
                        jpg: 'image/jpeg',
                        jpeg: 'image/jpeg',
                        png: 'image/png',
                        gif: 'image/gif',
                        pdf: 'application/pdf',
                        // Add more mappings as needed
                    };

                    newFile.fileType = typeMapping[fileExtension] || blob.type;

                    // PDF handling
                    if (newFile.fileType === 'application/pdf') {
                        if (newFile.pdfUrl) {
                            URL.revokeObjectURL(newFile.pdfUrl);
                        }
                        const blobUrl = URL.createObjectURL(blob);
                        newFile.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(blobUrl);
                        newFile.file = null;
                        return newFile;
                    }

                    // Image handling
                    if (newFile.fileType.startsWith('image/')) {
                        return new Promise((resolve) => {
                            const reader = new FileReader();
                            reader.onload = (e: any) => {
                                // Log the base64 result
                                newFile.file = e.target.result.split(',')[1]; // base64
                                // Log the file type
                                newFile.pdfUrl = null;
                                resolve(newFile);
                            };
                            reader.onerror = (error) => {
                                console.error('FileReader error:', error);
                                resolve(null); // Resolve with null on error
                            };
                            reader.readAsDataURL(blob); // Ensure this is called
                        });
                    }

                    // Other files handling (if needed)
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


    onImageError(event: any) {
        console.error('Image failed to load', event);
    }

    onRowCollapse(event: any) {
        const tax = event.data;
        delete this.expandedRows[tax.Id];
    }



    private statusMap: { [key: number]: { label: string; severity: ButtonSeverity } } = {
        6: { label: "Not Submited", severity: "help" as ButtonSeverity },
        0: { label: "waiting for Review", severity: "primary" as ButtonSeverity },
        1: { label: "Checker Sent", severity: "help" as ButtonSeverity },
        2: { label: "Checker Rejected", severity: "danger" as ButtonSeverity },
        3: { label: "Approver Rejected", severity: "danger" as ButtonSeverity },
        // 4: { label: "Reviewed", severity: "primary" as ButtonSeverity },
        5: { label: "Settled", severity: "success" as ButtonSeverity }
    };

    getStatusInfo(status: number): { label: string; severity: ButtonSeverity } {
        return this.statusMap[status] || { label: "Unknown status", severity: "info" as ButtonSeverity };
    }





    taxCategoryMap: { [key: number]: string } = {
        1: 'Income Tax',
        2: 'Withholding',
        3: 'VAT',
        4: '5(%) in Saving',
        5: 'Pension',
        6: 'Stamp Duty'
    };




    openDialogImage() {
        this.isDialogVisible = true;
    }

        closeModalPDF() {
        this.showPdfModal = false;
        this.selectedPdf = null;
    }
    
    hideDialogCrateEdit() {
        this.taxDialog = false;
        this.submitted = false;
    }



}