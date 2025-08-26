import { Component } from '@angular/core';
import { User } from '../../../../models/admin/user';
import { Functionalities } from '../../../../models/admin/functionalities';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { UserFunctionalityService } from '../../../service/admin/user-functionality-service';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { CheckboxModule } from 'primeng/checkbox';
import { ToastModule } from 'primeng/toast';
import { Toolbar } from 'primeng/toolbar';
import { CardModule } from 'primeng/card';
import { UserSearchEngineComponent } from '../../user/user-search-engine/user-search-engine.component';
import { BreadcrumbModule } from 'primeng/breadcrumb';

@Component({
    selector: 'app-manage-user-permissions',
    imports: [FormsModule, CommonModule, ButtonModule, TableModule, DialogModule, CheckboxModule, ToastModule, Toolbar, CardModule, UserSearchEngineComponent, BreadcrumbModule],
    providers: [MessageService, ConfirmationService],
    templateUrl: './manage-user-permissions.component.html',
    styleUrl: './manage-user-permissions.component.scss'
})
export class ManageUserPermissionsComponent {
    user: User = new User();
    users: User[] = [];
    selectedUsers: User[] = [];
    functionalities: Functionalities[] = [];
    assignedFunctionalities: Functionalities[] = [];
    selectedFunctionalities: Functionalities[] = [];
    loading: boolean = true;
    functionalityDialog: boolean = false;
    functionalityLoading: boolean = false;
    functionalityStatusDialog: boolean = false;
    fetching: boolean = false;
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private userFunctionalityService: UserFunctionalityService
    ) {}

    ngOnInit(): void {
       this.items = [{ label: 'User' }, { label: 'Permissions' }];
       this.home = { icon: 'pi pi-home', routerLink: '/' };
    }

    onViewFunctionalities(user: User): void {
        this.user = user;
        this.functionalityStatusDialog = true;
        this.assignedFunctionalities = this.user.functionalities || [];
    }

    closeDialog(): void {
        this.functionalityDialog = false;
        this.functionalities = [];
    }

    onDataGenerated(data: User[]) {
        this.loading = false;
        if (data != null) {
            this.fetching = true;
            this.users = data;
        }
    }

    revokeFunctionalities(): void {
        this.user.functionalities = this.selectedFunctionalities || [];
        this.confirmationService.confirm({
            message: 'Are you sure you want to revoke selected permissions?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.userFunctionalityService.deactivatePermissions(this.user).subscribe({
                    next: (data) => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Success',
                            detail: 'Permissions revoked successfully!'
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while revoking Permissions!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    grantFunctionalities(): void {
        this.confirmationService.confirm({
            message: 'Are you sure you want to grant selected permissions?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.userFunctionalityService.activatePermissions(this.selectedFunctionalities).subscribe({
                    next: (data) => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Success',
                            detail: 'Permissions granted successfully!'
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while granting Permissions!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }
}
