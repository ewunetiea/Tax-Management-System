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
    ) {}

    ngOnInit(): void {
        this.user = this.storageService.getUser();

        this.form = this.fb.group({
            type: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
            description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(200)]],
        });

        this.isEditData = this.passedTaxCategory[1];
        if (this.isEditData) {
            this.editTaxCategory(this.passedTaxCategory);
        } else {
            this.openNew();
        }
    }

    get f(): { [key: string]: AbstractControl } {
        return this.form.controls;
    }

    editTaxCategory(passedData: any[]) {
        this.taxCategory = passedData[0];
        this.form.patchValue({
            type: this.taxCategory.type?.trim(),
            description: this.taxCategory.description?.trim()
        });
    }

    openNew() {
        this.taxCategory = new TaxCategory();
    }

    emitData(data: any[]) {
        this.editedTaxCategory.emit(data);
    }

    onSubmit() {
        this.submitted = true;

        // ✅ Focus the first invalid input field if the form is invalid
        if (this.form.invalid) {
            for (const key of Object.keys(this.form.controls)) {
                if (this.form.controls[key].invalid) {
                    const invalidControl = document.querySelector(
                        `[formControlName="${key}"]`
                    );
                    if (invalidControl) {
                        (invalidControl as HTMLElement).focus();
                        // Optionally scroll to it smoothly
                        (invalidControl as HTMLElement).scrollIntoView({ behavior: 'smooth', block: 'center' });
                    }
                    break; // focus only the first invalid field
                }
            }
            this.submitted = false;
            return; // stop submission
        }

        // ✅ Prepare data
        this.taxCategory = {
            ...this.taxCategory,
            ...this.form.value,
            created_by: this.user.email,
            user_id: this.user.id
        };

        this.taxCategoriesService.createTaxCategory(this.taxCategory).subscribe({
            next: (data) => {
                this.submitted = false;

                const message = this.taxCategory.id
                    ? `${this.taxCategory.type} successfully updated`
                    : `${this.taxCategory.type} successfully created`;

                this.messageService.add({
                    severity: 'success',
                    summary: message,
                    detail: '',
                    life: 3000
                });

                // ✅ Reset form after success
                this.form.reset();
                this.taxCategory = new TaxCategory();

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
