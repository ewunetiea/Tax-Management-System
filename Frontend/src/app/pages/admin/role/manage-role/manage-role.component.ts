import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { JobPosition } from '../../../../models/admin/job-position';
import { Role } from '../../../../models/admin/role';
import { ValidationService } from '../../../service/admin/validationService';
import { RoleService } from '../../../service/admin/roleService';
import { Table } from 'primeng/table';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { MultiSelectModule } from 'primeng/multiselect';
import { ListboxModule } from 'primeng/listbox';
import { TabViewModule } from 'primeng/tabview';
import { AccordionModule } from 'primeng/accordion';
import { CreateEditRoleComponent } from '../create-edit-role/create-edit-role.component';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { PaginatorPayLoad } from '../../../../models/admin/paginator-payload';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { SelectButtonModule } from 'primeng/selectbutton';
import { Tooltip } from 'primeng/tooltip';

@Component({
    selector: 'app-manage-role',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        InputTextModule,
        DialogModule,
        ConfirmDialogModule,
        FormsModule,
        CardModule,
        ToastModule,
        ToolbarModule,
        MultiSelectModule,
        ListboxModule,
        TabViewModule,
        AccordionModule,
        CreateEditRoleComponent,
        BreadcrumbModule,
        InputIconModule,
        IconFieldModule,
        SelectButtonModule,
        Tooltip
    ],
    providers: [MessageService, ConfirmationService],
    templateUrl: './manage-role.component.html',
    styleUrl: './manage-role.component.scss'
})
export class ManageRoleComponent {
    exportSettings = {
        columnsHeader: true,
        fileName: 'Awash Bank - Job Positions Roles',
        hiddenColumns: false
    };
    selectedRole = new Role();
    data2: any = [];
    jobPositions: JobPosition[] = [];
    updated: Boolean = false;
    errorMessage: String = '';
    loading: boolean = true;
    role = new Role();
    currentRole = new Role();
    hideStatus: boolean = false;
    submitted = false;
    message = '';
    selectedRoles: Role[] = [];
    outputRole: any[] = [];
    isEditData = false;
    roleEditDialog = false;
    passRole = new Role();
    passRoles: Role[] = [];

    // Normal Datatable
    roles: Role[] = [];
    jobDialog = false;
    activeIndex1: number = 0;
    activeState: boolean[] = [true, false, false];
    is_job_position_selected = false;
    events1: any[] = [];
    risk_levels: any[] = [];
    jobPositionsByRole: JobPosition = new JobPosition();
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    paginatorPayload = new PaginatorPayLoad();
    sizes!: any[];
    selectedSize: any = 'normal';
    expandedRows = {};
    breadcrumbText: string = 'Manage Roles';

    constructor(
        private roleRightService: RoleService,
        private validationService: ValidationService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) {}

    ngOnInit(): void {
        this.breadcrumbText = 'Manage Roles';
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.retrieveRoles();
    }

    toggle(index: number) {
        this.activeState[index] = !this.activeState[index];
    }

    onGlobalFilter(table: Table, event: Event) {
        const inputValue = (event.target as HTMLInputElement).value;
        this.paginatorPayload.searchText = inputValue;
        this.paginatorPayload.currentPage = 1;
        this.retrieveRoles();
    }

    clear(table: Table) {
        table.clear();
    }

    openNew() {
        this.outputRole = [];
        this.role = new Role();
        this.isEditData = true;
        this.outputRole.push(this.role);
        this.outputRole.push(this.isEditData);
        this.roleEditDialog = true;
    }

    editRole(role: Role) {
        this.outputRole = [];
        this.role = { ...role };
        this.isEditData = false;
        this.outputRole.push(this.role);
        this.outputRole.push(this.isEditData);
        this.roleEditDialog = true;
    }

    onDataChange(data: any) {
        if (data[1]) {
            this.retrieveRoles();
            this.roles = [...this.roles];
            this.roleEditDialog = false;
            this.role = new Role();
        } else {
            this.roles[this.findIndexById(data[0].id)] = data[0];
        }
        this.roleEditDialog = false;
    }

    findIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < this.roles.length; i++) {
            if (this.roles[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }

    hideDialog() {
        this.jobDialog = false;
    }

    retrieveRoles(): void {
        this.roleRightService.getRoles().subscribe({
            next: (data) => {
                this.roles = data;
                const now = new Date();
                var date = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();
                this.exportSettings = {
                    columnsHeader: true,
                    fileName: `Awash Bank - Job Positions Roles ${date}`,
                    hiddenColumns: false
                };
                this.loading = false;
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while retriving roles !',
                    detail: ''
                });
            }
        });
    }

    generateData(): any[] {
        let data = new Array();
        let i = 0;
        for (const role of this.roles) {
            let row: any = {
                jobPositions: []
            };
            let row2: any = {};

            row['code'] = role.code;
            row['name'] = role.name;
            row['description'] = role.description;
            row['id'] = role.id;
            row['status'] = role.status ? '<span class="badge bg-success">Active</span>' : '<span class="badge bg-danger">Inactive</span>';

            if (role.jobPositions) {
                for (const jobPosition of role.jobPositions) {
                    row['jobPositions'].push(jobPosition);
                    // }
                }
            }

            row2['code'] = role.code;
            row2['name'] = role.name;
            row2['description'] = role.description;
            data[i] = row;
            this.data2[i] = row2;
            i++;
        }
        return data;
    }

    onItemSelect() {
        this.is_job_position_selected = true;
    }

    onItemDeSelect() {
        this.is_job_position_selected = false;
    }

    openModal(role: Role) {
        this.jobDialog = true;
        this.selectedRole = role;
        if (this.selectedRole.jobPositions) {
            for (const position of this.selectedRole.jobPositions) {
                this.jobPositions.push(position);
            }
        }
    }

    getJobPositions() {
        this.validationService.getTotalJobPositions().subscribe({
            next: (data) => {
                this.jobPositions = data;
                this.loading = false;
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching  job position !',
                    detail: ''
                });
            }
        });
    }

    editJobPositions() {
        this.roleRightService.manageJobPositions(this.jobPositionsByRole).subscribe({
            next: (data) => {
                this.jobDialog = false;
                this.updated = true;
                this.messageService.add({
                    severity: 'success',
                    summary: ` Relating role with Job position updated successfully`,
                    detail: ''
                });
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.errorMessage = error.error.message;

                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while updating  job position !',
                    detail: ''
                });
            }
        });
    }

    generateExportData(): any[] {
        return this.data2;
    }

    excelExport(): void {
        const now = new Date();
        var date = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();

        // Create a CSV string
        const headers = Object.keys(this.data2[0]);
        const csvContent = [headers.join(','), ...this.data2.map((row: Record<string, any>) => headers.map((header) => row[header]).join(','))].join('\n');

        // Create and download the file
        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
        const link = document.createElement('a');
        const url = URL.createObjectURL(blob);
        link.setAttribute('href', url);
        link.setAttribute('download', `CMS - System Roles - ${date}.csv`);
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }

    deleteRole(role: Role) {
        this.passRole = role;
        this.passRoles.push(this.passRole);
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected role?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.roleRightService.deleteRole(this.passRoles).subscribe({
                    next: (response) => {
                        this.retrieveRoles();
                        // this.roles = this.roles.filter((val) => val.id !== role.id);
                        this.role = new Role();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Role deactivated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deactivating role!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    deleteSelectedRole() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected roles?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.roleRightService.deleteRole(this.selectedRoles).subscribe({
                    next: (response) => {
                        this.retrieveRoles();
                        this.selectedRoles = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'roles Deactivated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deactivating roles!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    activateRole(role: Role) {
        this.passRole = role;
        this.passRoles.push(this.passRole);
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected role?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.roleRightService.activate_role(this.passRoles).subscribe({
                    next: (response) => {
                        this.roles = this.roles.filter((val) => val.id !== role.id);
                        this.role = new Role();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Role activated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while activating role!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    activateSelectedRole() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected roles?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.roleRightService.activate_role(this.selectedRoles).subscribe({
                    next: (response) => {
                        this.roles = this.roles.filter((val) => !this.selectedRoles.includes(val));
                        this.selectedRoles = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Roles activated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while activating roles!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }
}
