import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';

import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { HttpErrorResponse } from '@angular/common/http';
import { AccountData } from '../../../models/maker/account-data';
import { AccountService } from '../../../service/maker/account.service';
import { SharedUiModule } from '../../../../shared-ui';
import { StorageService } from '../../../service/sharedService/storage.service';

interface Column {
    field: string;
    header: string;
    customExportHeader?: string;
}

interface ExportColumn {
    title: string;
    dataKey: string;
}

@Component({
    standalone: true,
    selector: 'app-account-data',
    
    imports: [

        DialogModule,
        ConfirmDialogModule,
        SharedUiModule
    ],
    templateUrl: './account-data.component.html',
    styleUrl: './account-data.component.scss'
})
export class AccountDataComponent {

    accountTypes: any;

    accountDialog: boolean = false;
    accounts: AccountData[] = [];
    account!: AccountData;
    selectedAccounts!: AccountData[] | null;
    submitted: boolean = false;
    statuses!: any[];
    @ViewChild('dt') dt!: Table;
    exportColumns!: ExportColumn[];
    cols!: Column[];
    uploadedFiles: any[] = [];
    items: MenuItem[] | undefined;
    link: MenuItem | undefined;
    breadcrumbText: string = 'Manage Accounts';

    constructor(
        private accountService: AccountService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private storageService: StorageService
    ) { }

    ngOnInit() {
        this.link = { icon: 'pi pi-wallet', routerLink: '/applayout/maker/account' };
        this.items = [{ label: this.breadcrumbText }];
        this.loadAccounts();
        this.accountTypes = [
            { label: 'Savings', value: 'Savings' },
            { label: 'Current', value: 'Current' },
            { label: 'Loan', value: 'Loan' }
        ];

    }
    loadAccounts() {
        this.accountService.fetchAccount().subscribe(
            (response) => {
                if (Array.isArray(response)) {
                    this.accounts = response.map(account => ({
                        ...account
                    }));
                }
            },
            (error: HttpErrorResponse) => {
                this.messageService.add({
                    severity: 'error',
                    summary:
                        error.status === 401
                            ? 'You are not permitted to perform this action!'
                            : 'Something went wrong while fetching accounts!',
                    detail: '',
                });
            }
        );
    }



    exportCSV() {
        this.dt.exportCSV();
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.account = {};
        this.submitted = false;
        this.accountDialog = true;
    }

    editAccount(account: AccountData) {
        this.account = { ...account };
        this.accountDialog = true;
    }

    deleteselectedAccounts() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete the selected accounts?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {



                this.accounts = this.accounts.filter(
                    (val) => !this.selectedAccounts?.includes(val)
                );
                this.selectedAccounts = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'accounts Deleted',
                    life: 3000
                });
            }
        });
    }


    hideDialog() {
        this.accountDialog = false;
        this.submitted = false;
    }

    deleteAccount(account: AccountData) {
        this.confirmationService.confirm({
            message: `Are you sure you want to delete ${account.accountName}?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.accountService.deleteAccount(account.id!).subscribe({
                    next: () => {
                        this.accounts = this.accounts.filter(p => p.id !== account.id);
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Account Deleted',
                            life: 3000
                        });
                        this.account = {};
                    },
                    error: (err: HttpErrorResponse) => {
                        this.messageService.add({
                            severity: 'error',
                            summary: err.status === 401
                                ? 'You are not permitted to perform this action!'
                                : 'Something went wrong while deleting the account!',
                            detail: '',
                        });
                    }
                });
            }
        });
    }


    findIndexById(id: any): number {
        return this.accounts.findIndex((p) => p.id === id);
    }


    createId(): string {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        return Array.from({ length: 5 }, () => chars.charAt(Math.floor(Math.random() * chars.length))).join('');
    }

    getSeverity(status: string) {
        switch (status) {
            case 'INSTOCK': return 'success';
            case 'LOWSTOCK': return 'warn';
            case 'OUTOFSTOCK': return 'danger';
            default: return 'info';
        }
    }




    saveAccount() {
        this.submitted = true;
        this.account.user_id = this.storageService.getUser().id

        const _accounts = this.accounts;

        this.accountService.createAccount(this.account).subscribe({
            next: (accountResponse) => {

                // Add imageSrc field
                const updatedAccount = {
                    ...accountResponse
                };

                if (this.account.id) {
                    _accounts[this.findIndexById(this.account.id)] = updatedAccount;
                    this.accounts = [..._accounts];
                    this.messageService.add({
                        severity: "success",
                        summary: "Successful",
                        detail: "Audit Updated",
                        life: 3000,
                    });
                    this.accountDialog = false;

                } else {
                    this.accounts = [...this.accounts, updatedAccount];
                    this.messageService.add({
                        severity: "success",
                        summary: "Successful",
                        detail: "Audit Created",
                        life: 3000,
                    });
                    this.accountDialog = false;
                    this.account = {} as AccountData;
                }
            },
            error: (error: HttpErrorResponse) => {
                this.messageService.add({
                    severity: "error",
                    summary: "Error",
                    detail:
                        error.status == 401
                            ? "You are not permitted to perform this action!"
                            : "Something went wrong while either creating or updating finding!",
                    life: 3000,
                });
            },
        });
    }


    onFileSelect(event: any): void {
        const file: File = event.files[0];

        const reader = new FileReader();
        reader.onload = () => {
            const arrayBuffer = reader.result as ArrayBuffer;
            const byteArray = new Uint8Array(arrayBuffer);

            this.messageService.add({
                severity: 'success',
                summary: 'Upload Successful',
                detail: 'Image uploaded as byte array!'
            });

        };
        reader.readAsArrayBuffer(file);
    }





}
