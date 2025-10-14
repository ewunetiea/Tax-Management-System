
import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedUiModule } from '../../../../../shared-ui';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { FileUpload } from 'primeng/fileupload';
import { Tax } from '../../../../models/maker/tax';
import { TaxService } from '../../../../service/maker/tax-service';
import { TaxCategoriesService } from '../../../../service/maker/tax-categories-service';
import { TaxCategory } from '../../../../models/maker/tax-category';
import { BranchService } from '../../../../service/admin/branchService';
import { Branch } from '../../../../models/admin/branch';
import { TaxFile } from '../../../../models/maker/tax-file';

@Component({
    standalone: true,
    selector: 'app-tax-create-edit',
    imports: [SharedUiModule, FileUpload],
    templateUrl: './tax-create-edit.component.html',
    styleUrl: './tax-create-edit.component.scss'
})
export class TaxCreateEditComponent implements OnInit {
    @Input() visible!: boolean;
    @Output() visibleChange = new EventEmitter<boolean>(); // <-- required for two-way binding

    @Input() tax: Tax = new Tax();
    @Output() saved = new EventEmitter<Tax>(); // emit to parent
    @Output() cancel = new EventEmitter<void>();

    submitting = false;
    taxCategories: TaxCategory[] = [];
    branches: Branch[] = []
    user_branch: String = ''
    selectedBranchId: number | undefined; // Local variable for dropdown binding


    constructor(
        private taxService: TaxService,
        private messageService: MessageService,
        private storageService: StorageService,
        private taxCategoriesService: TaxCategoriesService,
        private branchService: BranchService,
        private cdr: ChangeDetectorRef // Inject ChangeDetectorRef


    ) { }


    ngOnInit(): void {
        this.user_branch = this.storageService.getUser().branch.name

        this.getTaxCategories()
        this.getBranches()



    }






    getTaxCategories() {
        this.taxCategoriesService.getTaxCategories().subscribe({
            next: (data) => {
                this.taxCategories = data;
            },
            error: (error) => {
                console.log(error)
            }
        });
    }



    getBranches() {
        this.branchService.getBranchesDropDown().subscribe({
            next: (data) => {
                this.branches = data
                if (!this.tax.sendTo_) {
                    const defaultBranch = this.branches.find(branch => branch.name === "Finance Management Directorate");
                    if (defaultBranch) {
                        this.selectedBranchId = defaultBranch.id;
                        this.tax.sendTo_ = this.selectedBranchId;
                        this.cdr.markForCheck();
                        this.cdr.detectChanges();
                    } else {
                        console.warn('Default branch "Finance Management Directorate" not found');
                    }
                } else {
                    this.selectedBranchId = Number(this.tax.sendTo_);
                    console.log('sendTo_ already set:', this.tax.sendTo_);
                }
            },
            error: (error) => {
                console.error('Error fetching branches:', error);
            }
        });
    }
    onBranchChange(value: number) {
        this.tax.sendTo_ = value;
        console.log('Branch changed, tax.sendTo_:', this.tax.sendTo_);
    }

    onSave() {
        this.tax.from_ = this.storageService.getUser().branch.id;
        this.tax.sendTo_ = this.selectedBranchId
        this.tax.maker_name = this.storageService.getUser().username;

        // Check if taxFile is available
        if (!this.tax.taxFile || this.tax.taxFile.length === 0) {
            this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'File is not available' });
            return; // Exit the function early if no file is selected
        }

        const formData = new FormData();

        formData.append('tax', new Blob([JSON.stringify(this.tax)], { type: 'application/json' }));

        if (this.tax.taxFile && this.tax.taxFile.length > 0) {
            this.tax.taxFile.forEach((taxFile: TaxFile) => {
                if (taxFile.file && taxFile.fileName) {
                    formData.append('files', taxFile.file, taxFile.fileName.toString());
                }

            });
        }

        this.submitting = true;

        // Send FormData instead of JSON
        this.taxService.createTax(formData).subscribe({
            next: (response) => {
                this.saved.emit(response); // emit created tax

                // close dialog and notify parent
                this.visible = false;
                this.visibleChange.emit(this.visible);

                this.submitting = false;
                this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Tax saved' });
            },
            error: (err: HttpErrorResponse) => {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message });
                this.submitting = false;
            }
        });
    }


    onCancel() {
        this.visible = false;
        this.visibleChange.emit(this.visible);
        this.cancel.emit();
    }


    onFileSelect(event: any) {
        const files: File[] = Array.from(event.files); // convert FileList to array

        if (!this.tax!.taxFile) {
            this.tax!.taxFile = [];
        }

        files.forEach(file => {
            const taxFile: TaxFile = {
                file: file,
                fileName: file.name,
                extension: '.' + file.name.split('.').pop(),
                fileType: file.type
            };
            this.tax!.taxFile!.push(taxFile);
        });

        this.messageService.add({
            severity: 'success',
            summary: 'Upload Successful',
            detail: `${files.length} file(s) added successfully!`
        });
    }



}
