import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { Role } from '../../../../models/admin/role';
import { ToastModule } from 'primeng/toast';
import { Functionalities } from '../../../../models/admin/functionalities';
import { DialogModule } from 'primeng/dialog';
import { MultiSelectModule } from 'primeng/multiselect';
import { FormsModule } from '@angular/forms';
import { TooltipModule } from 'primeng/tooltip';
import { ToolbarModule } from 'primeng/toolbar';
import { CardModule } from 'primeng/card';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { SelectButton, SelectButtonModule } from 'primeng/selectbutton';
import { PaginatorModule } from 'primeng/paginator';
import { InputTextModule } from 'primeng/inputtext';
import { RoleService } from '../../../../service/admin/roleService';
import { RoleFunctionalityService } from '../../../../service/admin/roleFunctionalityService';

@Component({
    selector: 'app-role-functionalities',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        ConfirmDialogModule,
        ToastModule,
        DialogModule,
        MultiSelectModule,
        FormsModule,
        TooltipModule,
        ToolbarModule,
        CardModule,
        BreadcrumbModule,
        InputIconModule,
        IconFieldModule,
        SelectButtonModule,
        PaginatorModule,
        InputTextModule,
        SelectButton
    ],
    providers: [MessageService, ConfirmationService],
    templateUrl: './role-functionalities.component.html',
    styleUrl: './role-functionalities.component.scss'
})
export class RoleFunctionalitiesComponent implements OnInit {
    role: Role = new Role();
    roles: Role[] = [];
    selectedRoles: Role[] = [];
    functionalities: Functionalities[] = [];
    assignedFunctionalities: Functionalities[] = [];
    selectedFunctionalities: Functionalities[] = [];
    loading: boolean = true;
    functionalityDialog: boolean = false;
    functionalityLoading: boolean = false;
    functionalityStatusDialog: boolean = false;
    breadcrumbText: string = 'Manage Branchs';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    sizes!: any[];
    selectedSize: any = undefined;

    constructor(
        private roleRightService: RoleService,
        private messageService: MessageService,
        private roleFunctionalityService: RoleFunctionalityService
    ) {}

    ngOnInit(): void {
        this.breadcrumbText = 'Manage Role Functionalities';
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.selectedSize = 'normal';
        this.roleRightService.getRoles().subscribe({
            next: (data) => {
                this.loading = false;
                this.roles = data;
            },
             error: () => {
            this.loading = false;
        }
        });
    }

    onAssignFunctionalities(role: Role): void {
        this.role = role;
        this.functionalityDialog = true;
        this.fetchFunctionalitiesByCategory(role);
        this.fetchFunctionalitiesByRole(role);
    }

    onViewFunctionalities(role: Role): void {
        this.role = role;
        this.functionalityStatusDialog = true;
        this.fetchFunctionalitiesByRole(role);
    }

    fetchFunctionalitiesByCategory(role: Role): void {
        this.roleFunctionalityService.getFunctionalitiesByRoleCategory(role).subscribe({
            next: (data) => {
                this.functionalities = data;
                this.loading = false;
            },
             error: () => {
            this.loading = false;
        }
        });
    }

    fetchFunctionalitiesByRole(role: Role): void {
        this.roleFunctionalityService.getFunctionalitiesByRoleId(role.id).subscribe({
            next: (data) => {
                this.assignedFunctionalities = data;
                this.role.functionalities = this.assignedFunctionalities;
                this.functionalityLoading = false;
            },
             error: () => {
            this.loading = false;
        }
        });
    }

    closeDialog(): void {
        this.functionalityDialog = false;
        this.functionalities = [];
    }

    clear(table: Table) {
        table.clear();
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    assignFunctionalities(role: Role): void {
        this.roleFunctionalityService.assignFunctionalities(role).subscribe({
            next: (data) => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Success',
                    detail: 'Functionalities assigned successfully!'
                });
                this.assignedFunctionalities = role.functionalities || [];
                this.functionalityDialog = false;
            },
             error: () => {
            this.loading = false;
        }
        });
    }

    updateFunctionalitiesRoleMapping(status: boolean): void {
        this.selectedFunctionalities = this.selectedFunctionalities.map((func) => {
            return { ...func, status: status };
        });
        this.role.functionalities = this.selectedFunctionalities;

        this.roleFunctionalityService.updateFunctionalitiesRoleMapping(this.role).subscribe({
            next: (data) => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Success',
                    detail: 'Functionalities updated successfully!'
                });
            },
            error: () => {
            this.loading = false;
        }
        });
    }
}
