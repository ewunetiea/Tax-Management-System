import { Component } from '@angular/core';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { JobPosition } from '../../../../models/admin/job-position';
import { Role } from '../../../../models/admin/role';
import { Table } from 'primeng/table';
import { CreateEditRoleComponent } from '../create-edit-role/create-edit-role.component';
import { SharedUiModule } from '../../../../../shared-ui';
import { JobPositionsByRole } from '../../../../models/admin/job-positions-by-role';
import { ExportExcelService } from '../../../../service/sharedService/export-excel.service';
import { RoleService } from '../../../../service/admin/roleService';
import { ValidationService } from '../../../../service/sharedService/validationService';

interface ExportColumn {
    title: string;
    dataKey: string;
}

@Component({
    selector: 'app-manage-role',
    standalone: true,
    imports: [SharedUiModule, CreateEditRoleComponent],
    templateUrl: './manage-role.component.html',
    styleUrl: './manage-role.component.scss'
})
export class ManageRoleComponent {
    exportSettings!: {
        columnsHeader: boolean;
        fileName: string;
        hiddenColumns: boolean;
    };
    selectedRole = new Role();
    data2: any = [];
    jobPositions: JobPosition[] = [];
    updated: Boolean = false;
    errorMessage: String = '';
    loading: boolean = false;
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
    jobPositionsByRole: JobPositionsByRole = new JobPositionsByRole();
    cols: any[] = [];
    exportColumns!: ExportColumn[];
    sizes!: any[];
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Manage Roles';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;

    constructor(
        private roleRightService: RoleService,
        private exportService: ExportExcelService,
        private validationService: ValidationService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) { }

    ngOnInit(): void {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.cols = [
            { field: 'Name', header: 'Name' },
            { field: 'Description', header: 'Description' },
            { field: 'Category', header: 'Category' },
            { field: 'Status', header: 'Status' },
            { field: 'Positions', header: 'Mapped Job Positions' }
        ];
        this.retrieveRoles();
    }

    toggle(index: number) {
        this.activeState[index] = !this.activeState[index];
    }

    onGlobalFilter(table: Table, event: Event) {
        const input = event.target as HTMLInputElement;
        table.filterGlobal(input.value, 'contains');
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
        this.loading = true;
        this.roleRightService.getRoles().subscribe({
            next: (data) => {
                this.roles = data;
                this.loading = false;
            },
            error: () => {
                this.loading = false;
            }
        });
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
            },
            error: () => {
                this.loading = false;
            }
        });
    }

    editJobPositions() {
        const jobPositionsByRole: JobPositionsByRole = { role: this.selectedRole, job_positions: this.selectedRole.jobPositions };
        this.roleRightService.manageJobPositions(jobPositionsByRole).subscribe({
            next: (data) => {
                this.jobDialog = false;
                this.updated = true;
                this.messageService.add({
                    severity: 'success',
                    summary: ` Relating role with Job position updated successfully`,
                    detail: ''
                });
            },
            error: () => {
                this.loading = false;
            }
        });
    }

    generateData(): any[] {
        let data = new Array();
        let i = 0;
        for (const role of this.selectedRoles) {
            let row: any = {}; // ✅ Initialize as an object

            row['Name'] = role.name;
            row['Description'] = role.description;
            row['Category'] = role.role_position;
            row['Status'] = role.status ? 'Active' : 'Inactive';

            let job_positions: string[] = [];
            if (role.jobPositions) {
                for (const jobPosition of role.jobPositions) {
                    // Ensure we push a primitive string (not a String object)
                    job_positions.push(String(jobPosition.title || ''));
                }
            }
            row['Positions'] = job_positions.join(', ');

            data[i] = row;
            i++;
        }
        return data;
    }

    excelExport(): void {
        if (this.selectedRoles.length === 0) {
            this.messageService.add({
                severity: 'warn',
                summary: 'No roles selected',
                detail: 'Please select at least one role to export.'
            });
            return;
        }

        this.exportColumns = this.cols.map((col) => ({
            title: col.header,
            dataKey: col.field
        }));

        let reportData = {
            sheet_name: 'Audit Management System - Roles',
            title: 'Audit Management System - Roles',
            data: this.generateData(),
            headers: this.exportColumns
        };

        this.exportService.exportExcelReport(reportData);
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
                    error: () => {
                        this.loading = false;
                    }
                });
            }
        });
    }

    deleteSelectedRole() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected roles ?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.roleRightService.deleteRole(this.selectedRoles).subscribe({
                    next: (response) => {
                        this.retrieveRoles();
                        // this.roles = this.roles.filter(
                        //   (val) => !this.selectedroles.includes(val)
                        // );
                        this.selectedRoles = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'roles Deactivated',
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

    activateRole(role: Role) {
        this.passRole = role;
        this.passRoles.push(this.passRole);
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected role ?',
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
                    error: () => {
                        this.loading = false;
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
                    error: () => {
                        this.loading = false;
                    }
                });
            }
        });
    }
}
