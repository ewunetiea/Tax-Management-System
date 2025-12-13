import { Component, EventEmitter, Input, Output } from '@angular/core';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { TaxCategoriesService } from '../../../../service/maker/tax-categories-service';
import { User } from '../../../../models/admin/user';
import { TaxCategory } from '../../../../models/maker/tax-category';
import { MessageService } from 'primeng/api';
import { SharedUiModule } from '../../../../../shared-ui';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InputSanitizer } from 'app/SQLi-XSS-Prevention/InputSanitizer';
import { xssSqlValidator } from 'app/SQLi-XSS-Prevention/xssSqlValidator';

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
    loading = false;
    form!: FormGroup;
    tagFilter = InputSanitizer.attackRegex.source;

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
  type: [
    '',
    [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(50),
      xssSqlValidator
    ]
  ],
  description: [
    '',
    [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(200),
      xssSqlValidator
    ]
  ]
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

    focusFirstInvalidControl() {
        const firstInvalidControl: HTMLElement = document.querySelector('.ng-invalid[formControlName]') as HTMLElement;

        if (firstInvalidControl) {
            firstInvalidControl.scrollIntoView({ behavior: 'smooth', block: 'center' });
            // Small delay ensures the element is ready to receive focus
            setTimeout(() => firstInvalidControl.focus(), 100);
        }
    }

    onSubmit() {
        this.submitted = true;

        // ✅ Focus the first invalid input field if the form is invalid
        // ❌ If form is invalid → auto-focus the first missing field
        if (this.form.invalid) {
            this.focusFirstInvalidControl();
            return;
        }

        const cleanedForm = {
    type: InputSanitizer.cleanInput(this.form.value.type),
    description: InputSanitizer.cleanInput(this.form.value.description)
  };

        this.loading = true;

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
                this.loading = false;

                // ✅ Show success message
                const message = this.taxCategory.id ? `${this.taxCategory.type} successfully updated` : `${this.taxCategory.type} successfully created`;
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
                this.loading = false;

                // ✅ Show error message
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error occurred while saving tax category',
                    detail: '',
                    life: 3000
                });
            }
        });
    }

    onReset(): void {
        this.submitted = false;
        this.form.reset();
    }
}
