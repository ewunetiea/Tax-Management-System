import { Component, EventEmitter, Input, Output } from '@angular/core';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { TaxCategoriesService } from '../../../../service/maker/tax-categories-service';
import { User } from '../../../../models/admin/user';
import { TaxCategory } from '../../../../models/maker/tax-category';
import { MessageService } from 'primeng/api';
import { SharedUiModule } from '../../../../../shared-ui';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-create-edit-tax-category',
    imports: [SharedUiModule],
    templateUrl: './create-edit-tax-category.component.html',
    styleUrl: './create-edit-tax-category.component.scss'
})
export class CreateEditTaxCategoryComponent {
    user: User = new User();
    isEditData: boolean = false;
    taxCategory: TaxCategory = new TaxCategory();
    submitted = false;
    form!: FormGroup;

    @Input() passedTaxCategory: any[] = [];
    @Output() editedTaxCategory: EventEmitter<any> = new EventEmitter();

    constructor(
        private storageService: StorageService,
        private taxCategoriesService: TaxCategoriesService,
        private messageService: MessageService,
        private fb: FormBuilder
    ) { }

    ngOnInit(): void {
        this.user = this.storageService.getUser();

        this.form = this.fb.group({
            type: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
            description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(200)]],
        });

        this.isEditData = this.passedTaxCategory[1];
        if (this.isEditData) {
            this.editBranch(this.passedTaxCategory);
        } else {
            this.openNew();
        }
    }

    get f(): { [key: string]: AbstractControl } {
        return this.form.controls;
    }


    editBranch(passedData: any[]) {
        this.taxCategory = passedData[0];
    }

    openNew() {
        this.taxCategory = new TaxCategory();
    }

    emitData(data: any[]) {
        this.editedTaxCategory.emit(data);
    }

    onSubmit() {
        this.submitted = true;

        // ✅ Check if the form is invalid
        if (this.form.invalid) {
            return; // stop here if form is invalid
        }

        // ✅ Get form values instead of directly using this.taxCategory
        this.taxCategory = {
            ...this.taxCategory,
            ...this.form.value,
            created_by: this.user.email,
            user_id:this.user.id
        };

        this.taxCategoriesService.createTaxCategory(this.taxCategory).subscribe({
            next: (data) => {
                this.submitted = false;

                if (this.taxCategory.id) {
                    this.messageService.add({
                        severity: 'success',
                        summary: `${this.taxCategory.type} successfully updated`,
                        detail: '',
                        life: 3000
                    });
                } else {
                    this.messageService.add({
                        severity: 'success',
                        summary: `${this.taxCategory.type} successfully created`,
                        detail: '',
                        life: 3000
                    });

                    // ✅ Reset form after success
                    this.form.reset();
                    this.taxCategory = new TaxCategory();
                }

                // ✅ Emit updated data
                this.passedTaxCategory = [this.taxCategory, this.isEditData];
                this.emitData(this.passedTaxCategory);
            },
            error: () => {
                this.submitted = false;
            }
        });
    }


    onReset(): void {
        this.submitted = false;
        this.form.reset();
    }


}
