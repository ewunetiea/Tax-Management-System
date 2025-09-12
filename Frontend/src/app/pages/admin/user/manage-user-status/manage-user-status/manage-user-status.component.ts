import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { User } from '../../../../../models/admin/user';
import { UserService } from '../../../../service/admin/user.service';


import { UserSearchEngineComponent } from '../../user-search-engine/user-search-engine.component';
import { SharedUiModule } from '../../../../../../shared-ui';
import { AddUserSecurityComponent } from "../../security/add-user-security/add-user-security.component";
import { Table } from 'primeng/table';

@Component({
    selector: 'app-manage-user-status',
    imports: [UserSearchEngineComponent, SharedUiModule, AddUserSecurityComponent],
    templateUrl: './manage-user-status.component.html',
    styleUrl: './manage-user-status.component.scss',
    providers: [MessageService, ConfirmationService]
})
export class ManageUserStatusComponent implements OnInit {
    selectedUser: User = new User();
    outputUser: any[] = [];
    isEditData = false;
    userStatusDialog = false;
    selectedUsers: User[] = [];
    users: User[] = [];
    updated: Boolean = false;
    errorMessage: String = '';
    loading: boolean = true;
    fetching: boolean = false;

    sizes!: any[];

    clonedUsers: { [s: string]: User } = {};
    users2: User[] = [];

    today: Date = new Date();
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Generate and Manage Status of Users';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    constructor(
        private userService: UserService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) { }
    ngOnInit(): void {

        this.home = { icon: 'pi pi-user', routerLink: '/user-status' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];

    }

  onGlobalFilter(table: Table, event: Event) {
        const input = event.target as HTMLInputElement;
        table.filterGlobal(input.value, 'contains');
    }

     clear(table: Table) {
        table.clear();
    }

    onDataGenerated(data: User[]) {
        this.loading = false;
        if (data != null) {
            this.fetching = true;
            this.users = data;
        }
    }

    onRowEditInit(user: User) {
        console.log()
        if (user.id) {
            this.clonedUsers[user.id] = { ...user };
        }
    }

    onRowEditSave(user: User, index: number) {
        this.confirmationService.confirm({
            message:
                'Are you sure you want to update user ' +
                user.first_name +
                ' ' +
                user.middle_name +
                ' status?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.userService.unlockUserAccount(user).subscribe({
                    next: (res) => {
                        this.loading = false;
                        this.updated = true;
                        this.messageService.add({
                            severity: 'success',
                            summary: ` ${user.first_name + '  ' + ' ' + user.middle_name
                                }  updated successfully`,
                            detail: '',
                        });
                        this.onRowEditCancel(user, index);
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.updated = false;
                        this.messageService.add({
                            severity: 'error',
                            summary:
                                error.status == 401
                                    ? 'You are not permitted to perform this action!'
                                    : 'Something went wrong while update user status !',
                            detail: '',
                        });
                    },
                });
            },
        });
    }

    onRowEditCancel(user: User, index: number) {
        if (user.id) {
            this.users2[index] = this.clonedUsers[user.id];
            delete this.clonedUsers[user.id];
        }

        user.editing = true; // Mark this user as being edited


    }


    updatePasswordModifiedDate(value: Date) {
        // You can handle any additional logic here if needed
        console.log('Selected date:', value);
    }

    openNew() {
        if (
            this.selectedUsers.length == 1 &&
            !this.selectedUsers[0].user_security
        ) {
            this.outputUser = [];
            this.isEditData = true;
            this.outputUser.push(this.selectedUsers[0]);
            this.outputUser.push(this.isEditData);
            this.userStatusDialog = true;
        } else {
            this.messageService.add({
                severity: 'error',
                summary:
                    'Kindly identify a user who lacks the necessary security configuration to update their user status.',
                detail: 'Kindly choose a single user.',
                life: 10000,
            });
        }
    }

    onDataChange(data: any) {
        this.users[this.findIndexById(data[0].id)] = data[0];
        this.userStatusDialog = false;
    }

    findIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < this.users.length; i++) {
            if (this.users[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }
}
