import { Component } from '@angular/core';
import { SharedUiModule } from '../../../../../shared-ui';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { TaxCategory } from '../../../../models/maker/tax-category';
import { TaxCategoriesService } from '../../../../service/maker/tax-categories-service';
import { Table } from 'primeng/table';
import { CreateEditTaxCategoryComponent } from '../create-edit-tax-category/create-edit-tax-category.component';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { User } from '../../../../models/admin/user';

@Component({
    selector: 'app-manage-tax-category',
    providers: [MessageService, ConfirmationService],
    imports: [SharedUiModule, CreateEditTaxCategoryComponent],
    templateUrl: './manage-tax-category.component.html',
    styleUrl: './manage-tax-category.component.scss'
})
export class ManageTaxCategoryComponent {
    sizes!: any[];
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Manage Tax Categories';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    selectedTaxCategories: TaxCategory[] = [];
    loading = true;
    taxCategories: TaxCategory[] = [];
    outputTaxCategory: any[] = [];
    taxCategory: TaxCategory = new TaxCategory();
    isEditData = false;
    taxCategoryDialog = false;
    passCategory = new TaxCategory();
    passCategories: TaxCategory[] = [];
    user: User = new User();

    constructor(
      private taxCategoriesService: TaxCategoriesService,
      private confirmationService: ConfirmationService,
      private messageService: MessageService,
      private storageService: StorageService,
    ) {}

    ngOnInit(): void {
        this.user = this.storageService.getUser();
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.getTaxCategories();
    }

    getTaxCategories() {
        this.taxCategoriesService.getTaxCategories().subscribe({
            next: (data) => {
                this.loading = false;
                this.taxCategories = data;
            },
            error: () => {
                this.loading = false;
            }
        });
    }

    clear(table: Table) {
        table.clear();
    }

    onGlobalFilter(table: Table, event: Event) {
        const input = event.target as HTMLInputElement;
        table.filterGlobal(input.value, 'contains');
    }

    openNew() {
      this.outputTaxCategory = [];
      this.taxCategory = new TaxCategory();
      this.isEditData = false;
      this.outputTaxCategory.push(this.taxCategory);
      this.outputTaxCategory.push(this.isEditData);
      this.taxCategoryDialog = true;
  }

  editTaxCategory(taxCategory: TaxCategory) {
    this.outputTaxCategory = [];
    this.taxCategory = { ...taxCategory };
    this.isEditData = true;
    this.outputTaxCategory.push(this.taxCategory);
    this.outputTaxCategory.push(this.isEditData);
    this.taxCategoryDialog = true;
}

onDataChange(data: any) {
  if (data[1]) {
      this.taxCategories[this.findIndexById(data[0].id)] = data[0];
  } else {
    this.getTaxCategories();
      this.taxCategories = [...this.taxCategories];
      this.taxCategory = new TaxCategory();
  }
  this.taxCategoryDialog = false;
}

findIndexById(id: number): number {
  let index = -1;
  for (let i = 0; i < this.taxCategories.length; i++) {
      if (this.taxCategories[i].id === id) {
          index = i;
          break;
      }
  }
  return index;
}

deleteSelectedTaxCategories() {
     this.selectedTaxCategories.forEach(tax => {
    tax.user_id = this.user.id;
  });
  this.confirmationService.confirm({
      message: 'Are you sure you want to deactivate selected tax categories?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
          this.taxCategoriesService.deleteTaxCategories(this.selectedTaxCategories).subscribe({
              next: (response) => {
                this.getTaxCategories();
                  this.selectedTaxCategories = [];
                  this.messageService.add({
                      severity: 'success',
                      summary: 'Successful',
                      detail: 'Tax Category Deleted',
                      life: 3000
                  });
              },
              error: () => {
                  this.loading = false;
              }
          });
      }
  });
}


deleteTaxCategory(tax: TaxCategory) {
    tax.user_id = this.user.id;
    this.passCategory = tax;
    this.passCategories.push(this.passCategory)
    this.confirmationService.confirm({
        message: 'Are you sure you want to delete the selected tax category ?',
        header: 'Confirm',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
            this.taxCategoriesService.deleteTaxCategories(this.passCategories).subscribe({
                next: (response) => {
                    this.getTaxCategories();
                    this.taxCategory = new TaxCategory();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Tax Category Deleted',
                        life: 3000
                    });
                },
                error: () => {
                    this.loading = false;
                }
            });
        }
    });
}



}
