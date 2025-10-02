import { Component, EventEmitter, Input, Output } from '@angular/core';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { TaxCategoriesService } from '../../../../service/maker/tax-categories-service';
import { User } from '../../../../models/admin/user';
import { TaxCategory } from '../../../../models/maker/tax-category';
import { MessageService } from 'primeng/api';

@Component({
    selector: 'app-create-edit-tax-category',
    imports: [],
    templateUrl: './create-edit-tax-category.component.html',
    styleUrl: './create-edit-tax-category.component.scss'
})
export class CreateEditTaxCategoryComponent {
    user: User = new User();
    isEditData: boolean = false;
    taxCategory: TaxCategory = new TaxCategory();
    loading = false;

    @Input() passedTaxCategory: any[] = [];
    @Output() editedTaxCategory: EventEmitter<any> = new EventEmitter();

    constructor(
        private storageService: StorageService,
        private taxCategoriesService: TaxCategoriesService,
        private messageService: MessageService
    ) {}

    ngOnInit(): void {
        this.user = this.storageService.getUser();
        this.isEditData = this.passedTaxCategory[1];
        if (this.isEditData) {
            this.editBranch(this.passedTaxCategory);
        } else {
            this.openNew();
        }
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

    saveTaxCategory() {
        this.loading = true;
        // this.taxCategory.created_by = this.user.email;
        this.taxCategoriesService.createTaxCategory(this.taxCategory).subscribe({
            next: (data) => {
                this.loading = false;
                if (this.taxCategory.id) {
                    this.messageService.add({
                        severity: 'success',
                        summary: ` ${this.taxCategory.name} successfully updated`,
                        detail: '',
                        life: 3000
                    });
                } else {
                    this.messageService.add({
                        severity: 'success',
                        summary: ` ${this.taxCategory.name} successfully created`,
                        detail: '',
                        life: 3000
                    });
                    this.taxCategory = new TaxCategory();
                }
                this.passedTaxCategory = [];
                this.passedTaxCategory.push(this.taxCategory);
                this.passedTaxCategory.push(this.isEditData);
                this.emitData(this.passedTaxCategory);
            },
             error: () => {
            this.loading = false;
        }
        });
    }

    
}
