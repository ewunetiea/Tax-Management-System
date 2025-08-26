import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, MenuItem, MessageService, SelectItem } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { RoleFunctionalityService } from '../../../service/admin/roleFunctionalityService';
import { CommonModule } from '@angular/common';
import { Table, TableModule } from 'primeng/table';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Functionalities } from '../../../../models/admin/functionalities';
import { RoleService } from '../../../service/admin/roleService';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelect } from 'primeng/multiselect';
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
    providers: [MessageService, ConfirmationService],
    templateUrl: './manage-role-functionalities.component.html',
    styleUrl: './manage-role-functionalities.component.scss'
})
export class ManageRoleFunctionalitiesComponent implements OnInit {
    @ViewChild('dt') dt!: Table;
    functionalities: Functionalities[] = [];
    functionality: Functionalities = new Functionalities();
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
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    sizes!: any[];
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Manage Permissions';
    outputFunctionality: any[] = [];
    isEditData = false;

    constructor(
        private roleFunctionalityService: RoleFunctionalityService,
        private roleRightService: RoleService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder
    ) {
        this.functionalityForm = this.fb.group({
            id: [null],
            name: ['', [Validators.required]],
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
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.handleError(error.status === 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching functionalities!', error);
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
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.handleError(error.status === 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching roles!', error);
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
    //     this.functionalityForm.patchValue({
    //         ...functionality,
    //         categories: functionality.category?.split(', ')
    //     });
    //     this.getRolesCode();
    //     this.functionalityDialog = true;
    // }

    editFunctionality(region: Functionalities) {
            this.outputFunctionality = [];
            this.nameExists = false;
            this.functionality = { ...region };
            this.isEditData = true;
            this.outputFunctionality.push(this.functionality);
            this.outputFunctionality.push(this.isEditData);
            this.getRolesCode();
            this.functionalityDialog = true;
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


    openNew(): void {
        this.functionalityForm.reset();
        this.nameExists = false;
        this.nameTouched = false;
        this.getRolesCode();
        this.functionalityDialog = true;
        this.outputFunctionality.push(this.functionalityForm);
        this.outputFunctionality.push(this.isEditData);
    }

    clear(table: Table) {
        table.clear();
    }

    onGlobalFilter(table: Table, event: Event) {
        const inputValue = (event.target as HTMLInputElement).value;
        table.filterGlobal(inputValue, 'contains');
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
            error: (error) => {
                this.handleError('Error saving functionality', error);
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
            error: (error: HttpErrorResponse) => {
                this.handleError(error.status === 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while updating permissions!', error);
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
            error: (error: HttpErrorResponse) => {
                this.handleError(error.status === 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while updating permissions!', error);
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
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deleting selected permissions!',
                            detail: ''
                        });
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
