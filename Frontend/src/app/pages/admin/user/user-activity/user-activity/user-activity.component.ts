import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { ExportExcelService } from '../../../../service/admin/export-excel.service';
import { UserService } from '../../../../service/admin/user.service';
import { PaginatorPayLoad } from '../../../../../models/admin/paginator-payload';
import { OnlineFailedUsers } from '../../../../../models/admin/online-failed-users';
import { Table } from 'primeng/table';
import { SharedUiModule } from '../../../../../../shared-ui';

@Component({
    standalone: true,
    selector: 'app-user-activity',
    imports: [SharedUiModule],
    templateUrl: './user-activity.component.html',
    styleUrl: './user-activity.component.scss',
    providers: [MessageService, ConfirmationService]
})
export class UserActivityComponent {
    onlineFailedUsers: OnlineFailedUsers[] = [];
    selectedOnlineFailedUsers: OnlineFailedUsers[] = [];
    loading = true;
    active = 0;
    live = true;
    active_count = 0;

    //used for exporting, Header titles given
    data2 = new Array();
    exportSettings: {
        columnsHeader: boolean;
        fileName: string;
        hiddenColumns: boolean;
    } = {
            columnsHeader: true,
            fileName: 'default_filename',
            hiddenColumns: false
        };
    totalRecords: any = 0;
    paginatorPayload = new PaginatorPayLoad();
    breadcrumbText: string = 'User Login Status';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    sizes!: any[];
    selectedSize: any = 'normal';

    constructor(
        private userService: UserService,
        private exportService: ExportExcelService,
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
        this.generateOnlineFailedUsers(this.paginatorPayload);
    }

    generateOnlineFailedUsers(paginatorPayLoad: PaginatorPayLoad) {
        this.userService.getOnlineFailedUsers(paginatorPayLoad).subscribe({
            next: (data) => {
                this.onlineFailedUsers = data;
                if (data.length > 0) {
                    this.onlineFailedUsers = data;
                    this.totalRecords = data[0].total_records_paginator;
                } else {
                    this.onlineFailedUsers = [];
                    this.totalRecords = 0;
                }
                this.loading = false;
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary:
                        error.status == 401
                            ? 'You are not permitted to perform this action!'
                            : 'Something went wrong while fetching users login status !',
                    detail: '',
                });
            },
        });
    }

    onPage(event: any) {
        this.paginatorPayload.currentPage = event.first / event.rows + 1;
        this.paginatorPayload.pageSize = event.rows;
        this.paginatorPayload.event_length = event.rows;
        this.generateOnlineFailedUsers(this.paginatorPayload);
    }

    onGlobalFilter(table: Table, event: Event) {
        const inputValue = (event.target as HTMLInputElement).value;
    }

    clear(table: Table) {
        table.clear();
    }

    generateUserData(): any[] {
        let data = new Array();
        let i = 0;
        let now = new Date().getTime();
        let time: any;
        for (const online of this.onlineFailedUsers) {
            let row: any = {
                region: String,
            };
            let row2: any = {};

            row['user_name'] = online.user_name;
            // row['user_agent'] = online.user_agent;
            row['ip_address'] = online.ip_address;
            row['last_login_time'] = online.login_time;
            row['user_agent'] = online.user_agent;

            row2['User Name'] = online.user_name;
            // row['user_agent'] = online.user_agent;
            row2['Ip Address'] = online.ip_address;
            // row2['Last login time'] = online.login_time;
            row2['User Agent'] = online.user_agent;

            var last = Date.parse(row['last_login_time']);
            time = now - last;
            let diffday = Math.floor(time / 86400000);
            let diffhoure = Math.floor((time % 86400000) / 3600000);
            let diffmin = Math.floor(((time % 86400000) % 3600000) / 60000);

            if (diffday >= 1) {
                row['login_time'] = diffday + ' days ago';
                row2['Login time'] = diffday + ' days ago';
            } else {
                if (diffhoure >= 1) {
                    row['login_time'] = diffhoure + ' hours ago';
                    row2['Login time'] = diffhoure + ' hours ago';
                } else {
                    if (diffmin >= 1) {
                        row['login_time'] = diffmin + ' minutes ago';
                        row2['Login time'] = diffmin + ' minutes ago';
                    } else {
                        row['login_time'] = 'Recently';
                        row2['Login time'] = 'Recently';
                    }
                }
            }
            if (online.status == 1) {
                row['status'] = 'Online';
                row2['Status'] = 'Online';
            } else {
                row['status'] = 'Failed';
                row2['Status'] = 'Failed';
            }
            row['count'] = i + 1;
            data[i] = row;
            this.data2[i] = row2;

            i++;
            this.active_count = row['count'];
        }
        return data;
    }

    generateExportData(): any[] {
        return this.data2;
    }

    excelExport(): void {
        const now = new Date();
        var date =
            now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();
        this.exportSettings = {
            columnsHeader: true,
            fileName: `Awash Bank - Users login status ${date}`,
            hiddenColumns: false,
        };
        this.generateUserData();

        let reportData = {
            sheet_name: 'User Login Status',
            title: 'Audit Management System - User Login Status',
            data: this.generateExportData(),
            headers: Object.keys(this.generateExportData()[0]),
        };
        this.exportService.exportExcel(reportData);
    }

    deleteSelectedLoginStatus() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete selected login statuses?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.userService
                    .updateLoginStatus(this.selectedOnlineFailedUsers)
                    .subscribe({
                        next: (response) => {
                            this.onlineFailedUsers = this.onlineFailedUsers.filter(
                                (val) => !this.selectedOnlineFailedUsers.includes(val)
                            );
                            this.selectedOnlineFailedUsers = [];
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Successful',
                                detail: 'Login Statuses Deleted',
                                life: 3000,
                            });
                        },
                        error: (error: HttpErrorResponse) => {
                            this.loading = false;
                            this.messageService.add({
                                severity: 'error',
                                summary:
                                    error.status == 401
                                        ? 'You are not permitted to perform this action!'
                                        : 'Something went wrong while deleting login status!',
                                detail: '',
                            });
                        },
                    });
            },
        });
    }
}
