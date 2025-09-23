import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, MenuItem, MessageService, SelectItem } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { CommonModule } from '@angular/common';
import { Table, TableModule } from 'primeng/table';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Functionalities } from '../../../../models/admin/functionalities';
import { RoleService } from '../../../../service/admin/roleService';
import { DropdownModule } from 'primeng/dropdown';
import { DialogModule } from 'primeng/dialog';
import { Toolbar } from 'primeng/toolbar';
import { CardModule } from 'primeng/card';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { InputTextModule } from 'primeng/inputtext';
import { SelectButton } from 'primeng/selectbutton';
import { Tooltip } from 'primeng/tooltip';
import { CreateEditFunctionalitiesComponent } from '../create-edit-functionalities/create-edit-functionalities.component';
import { RoleFunctionalityService } from '../../../../service/admin/roleFunctionalityService';

@Component({
    selector: 'app-manage-role-functionalities',
    standalone: true,
    imports: [
        CommonModule,
        ToastModule,
        TableModule,
        CheckboxModule,
        ButtonModule,
        FormsModule,
        ReactiveFormsModule,
        DropdownModule,
        DialogModule,
        Toolbar,
        CardModule,
        BreadcrumbModule,
        InputIconModule,
        IconFieldModule,
        InputTextModule,
        SelectButton,
        Tooltip,
        CreateEditFunctionalitiesComponent
    ],
    
    templateUrl: './manage-role-functionalities.component.html',
    styleUrl: './manage-role-functionalities.component.scss'
})
export class ManageRoleFunctionalitiesComponent implements OnInit {
    @ViewChild('dt') dt!: Table;
    functionalities: Functionalities[] = [];
    selectedFunctionalities: Functionalities[] = [];
    functionalityDialog: boolean = false;
    loading: boolean = true;
    globalFilter: string = '';
    functionalityForm: FormGroup;
    nameTouched: boolean = false;
    functionalityLoading: boolean = false;
    categories: SelectItem[] = [];
    methods: SelectItem[] = [
        { label: 'GET', value: 'GET' },
        { label: 'POST', value: 'POST' },
        { label: 'PUT', value: 'PUT' },
        { label: 'DELETE', value: 'DELETE' }
    ];
    nameExists: boolean = false;
    functionality: Functionalities = new Functionalities();
    outputFunctionality: any[] = [];
    isEditData = false;
    sizes!: any[];
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Manage Role Functionalities';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;

    constructor(
        private roleFunctionalityService: RoleFunctionalityService,
        private roleRightService: RoleService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder
    ) {
        this.functionalityForm = this.fb.group({
            id: [null],
            name: ['', [Validators.required]], // Removed async validator
            description: ['', [Validators.required]],
            categories: [[], [Validators.required]],
            method: ['', [Validators.required]],
            status: [false]
        });
    }

    ngOnInit(): void {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.loadFunctionalities();
    }

    loadFunctionalities(): void {
        this.loading = true;
        this.roleFunctionalityService.getAllRoleFunctionalities().subscribe({
            next: (resp: any) => {
                this.functionalities = resp;
                this.loading = false;
            },
             error: () => {
            this.loading = false;
        }
        });
    }

    getRolesCode(): void {
        this.roleRightService.getCommonRoles().subscribe({
            next: (roles: any) => {
                this.categories = roles.map((role: any) => ({
                    label: role.code,
                    value: role.code
                }));
                this.categories.push({
                    label: 'ALL',
                    value: 'ALL'
                });
                this.categories.push({
                    label: 'ALL_INS',
                    value: 'ALL_INS'
                });
                this.categories.push({
                    label: 'ALL_BFA',
                    value: 'ALL_BFA'
                });

                this.categories.push({
                    label: 'ALL_MGT',
                    value: 'ALL_MGT'
                });

                this.categories.push({
                    label: 'ALL_IS_MGT',
                    value: 'ALL_IS_MGT'
                });
            },
            error: () => {
            this.loading = false;
        }
        });
    }

    checkNameExists() {
        this.nameTouched = true;
        const name = this.functionalityForm.get('name')?.value;
        const currentId = this.functionalityForm.get('id')?.value;

        if (name && name.trim() !== '') {
            this.nameExists = this.functionalities.some((f) => f.name?.toLowerCase() === name.toLowerCase() && f.id !== currentId && f.method === this.functionalityForm.get('method')?.value);
        } else {
            this.nameExists = false;
        }
    }

    // editFunctionality(functionality: Functionalities): void {
    //     this.nameExists = false;
    //     this.functionalityForm.patchValue({ ...functionality, categories: functionality.category?.split(', ') });
    //     this.getRolesCode();
    //     this.functionalityDialog = true;
    // }

    //   openNew(): void {
    //     this.functionalityForm.reset();
    //     this.nameExists = false;
    //     this.nameTouched = false;
    //     this.getRolesCode();
    //     this.functionalityDialog = true;
    //   }

    openNew() {
        this.outputFunctionality = [];
        this.functionality = new Functionalities();
        this.outputFunctionality.push(this.functionality);
        this.outputFunctionality.push(this.isEditData);
        this.functionalityDialog = true;
        this.getRolesCode();
    }

    editFunctionality(functionality: Functionalities) {
        this.outputFunctionality = [];
        this.functionality = { ...functionality };
        this.isEditData = true;
        this.outputFunctionality.push(this.functionality);
        this.outputFunctionality.push(this.isEditData);
        this.functionalityDialog = true;
        this.getRolesCode();
    }

    onDataChange(data: any) {
        if (data[1]) {
            this.loadFunctionalities();
            this.functionalities = [...this.functionalities];
            this.functionalityDialog = false;
            this.functionality = new Functionalities();
        } else {
            this.functionalities[this.findIndexById(data[0].id)] = data[0];
        }
        this.functionalityDialog = false;
    }

    findIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < this.functionalities.length; i++) {
            if (this.functionalities[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }

    saveFunctionality(): void {
        this.checkNameExists();
        if (this.functionalityForm.invalid || this.nameExists) {
            this.functionalityForm.markAllAsTouched();
            return;
        }

        const formValue = this.functionalityForm.value;
        const functionality: Functionalities = {
            ...formValue,
            category: formValue.categories?.join(', ')
        };

        this.functionalityLoading = true;
        this.roleFunctionalityService.savePermissions(functionality).subscribe({
            next: (data) => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Success',
                    detail: functionality.id ? 'Functionality updated' : 'Functionality created'
                });
                this.functionalityDialog = false;
                this.functionalityLoading = false;
                this.loadFunctionalities();
            },
             error: () => {
            this.loading = false;
        }
        });
    }

    confirmActivateSelected(): void {
        const selectedInactive = this.selectedFunctionalities.filter((f) => !f.status);
        if (!selectedInactive.length) {
            this.showNoSelectionWarning();
            return;
        }

        this.confirmationService.confirm({
            message: 'Are you sure you want to activate the selected functionalities?',
            header: 'Confirm Activation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.activatePermissions(selectedInactive)
        });
    }

    confirmDeactivateSelected(): void {
        const selectedActive = this.selectedFunctionalities.filter((f) => f.status);
        if (!selectedActive.length) {
            this.showNoSelectionWarning();
            return;
        }

        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate the selected functionalities?',
            header: 'Confirm Deactivation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.deactivatePermissions(selectedActive)
        });
    }

    activatePermissions(selectedActive: Functionalities[]): void {
        this.roleFunctionalityService.activatePermissions(selectedActive).subscribe({
            next: (data: any) => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successfully Updated!',
                    detail: ''
                });
                this.loadFunctionalities();
                this.selectedFunctionalities = [];
            },
            error: () => {
            this.loading = false;
        }
        });
    }

    deactivatePermissions(selectedActive: Functionalities[]): void {
        this.roleFunctionalityService.deactivatePermissions(selectedActive).subscribe({
            next: (data: any) => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successfully Updated!',
                    detail: ''
                });
                this.loadFunctionalities();
                this.selectedFunctionalities = [];
            },
             error: () => {
            this.loading = false;
        }
        });
    }

    deletePermissions() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to permanently delete selected permissions?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.roleFunctionalityService.deletePermissions(this.selectedFunctionalities).subscribe({
                    next: (response) => {
                        this.functionalities = this.functionalities.filter((val) => !this.selectedFunctionalities.includes(val));
                        this.selectedFunctionalities = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Permissions are deleted successfully',
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

    exportCSV(): void {
        this.dt.exportCSV();
    }

    clearFilter(): void {
        this.globalFilter = '';
        this.dt.clear();
    }

    onGlobalFilter(table: Table, event: Event) {
        const input = event.target as HTMLInputElement;
        table.filterGlobal(input.value, 'contains');
    }

    clear(table: Table) {
        table.clear();
    }

    private handleError(summary: string, error: any): void {
        this.messageService.add({
            severity: 'error',
            summary: summary,
            detail: error.message || ''
        });
    }

    private showNoSelectionWarning(): void {
        this.messageService.add({
            severity: 'warn',
            summary: 'No Functionalities Selected',
            detail: 'Please select at least one functionality to update.'
        });
    }

    // Helper methods for template
    isFieldInvalid(field: string): boolean {
        const formField = this.functionalityForm.get(field);
        return formField ? formField.invalid && (formField.dirty || formField.touched) : false;
    }

    statusBodyTemplate(rowData: Functionalities): string {
        return rowData.status ? 'Active' : 'Inactive';
    }

    statusSeverity(rowData: Functionalities): string {
        return rowData.status ? 'success' : 'warning';
    }

    hideDialog(): void {
        this.functionalityDialog = false;
        this.nameTouched = false;
        this.functionalityForm.reset();
    }
}
