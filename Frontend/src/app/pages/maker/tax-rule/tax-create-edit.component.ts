



import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedUiModule } from '../../../../shared-ui';
import { StorageService } from '../../../service/sharedService/storage.service';
import { FileUpload } from 'primeng/fileupload';
import { Tax } from '../../../models/maker/tax';
import { TaxService } from '../../../service/maker/tax-service';
import { TaxCategoriesService } from '../../../service/maker/tax-categories-service';
import { TaxCategory } from '../../../models/maker/tax-category';
import { BranchService } from '../../../service/admin/branchService';
import { Branch } from '../../../models/admin/branch';
import { TaxFile } from '../../../models/maker/tax-file';

@Component({
    standalone: true,
    selector: 'app-tax-create-edit',
    imports: [SharedUiModule,FileUpload],
    templateUrl: './tax-create-edit.component.html',
    styleUrl: './tax-create-edit.component.scss'
})
export class TaxCreateEditComponent {
    @Input() visible!: boolean;
    @Output() visibleChange = new EventEmitter<boolean>(); // <-- required for two-way binding

    @Input() tax!: Tax;
    @Output() saved = new EventEmitter<Tax>(); // emit to parent
    @Output() cancel = new EventEmitter<void>();

    submitting = false;
    taxCategories: TaxCategory[] = [];
    branches: Branch[] = []

    constructor(
        private taxService: TaxService,
        private messageService: MessageService,
        private storageService: StorageService,
        private taxCategoriesService: TaxCategoriesService,
        private branchService: BranchService,

    ) { }


    ngOnInit(): void {


        this.getTaxCategories()
        this.getBranches()

    }



    getTaxCategories() {
        this.taxCategoriesService.getTaxCategories().subscribe({
            next: (data) => {
                this.taxCategories = data;


            },
            error: (error) => {


            }
        });
    }

    getBranches() {
        this.branchService.getBranchesDropDown().subscribe({
            next: (data) => {
                this.branches = data;

            },
            error: () => {


            }
        });
    }




    //    ,[from_]
    //       ,[sendTo_]


    //       ,[taxCategory]
    //       ,[noOfEmployee]
    //       ,[taxableAmount]
    //       ,[taxWithHold]
    //       ,[incometaxPoolAmount]
    //       ,[graduatetaxPool]
    //       ,[graduatetaxPool1]
    //       ,[graduaTotBasSalary]
    //       ,[graduateTotaEmployee]
    //       ,[graduatetaxWithHold]
    //       ,[taxCategoryList]
    //       ,[Remark]


    // onSave() {

    //     this.tax.from_ = this.storageService.getUser().branch.id;
    //     this.tax.maker_name = this.storageService.getUser().username;

    //     this.submitting = true;

    //     this.taxService.createTax(this.tax).subscribe({
    //         next: (response) => {

    //             this.saved.emit(response); // emit created tax

    //             // close dialog and notify parent
    //             this.visible = false;
    //             this.visibleChange.emit(this.visible);

    //             this.submitting = false;
    //             this.messageService.add({ severity: 'success', summary: 'Success', detail: 'tax saved' });
    //         },
    //         error: (err: HttpErrorResponse) => {
    //             this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message });
    //             this.submitting = false;
    //         }
    //     });
    // }


    onSave() {
    // Add additional fields
    this.tax.from_ = this.storageService.getUser().branch.id;
    this.tax.maker_name = this.storageService.getUser().username;

    const formData = new FormData();

    // Append the Tax object as JSON
    formData.append('tax', new Blob([JSON.stringify(this.tax)], { type: 'application/json' }));

    // Append uploaded files if any
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
            extension: '.'+file.name.split('.').pop(),
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
