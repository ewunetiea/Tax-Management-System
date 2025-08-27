import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Table } from 'primeng/table';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { ExportExcelService } from '../../../service/admin/export-excel.service';
import { BranchService } from '../../../service/admin/branchService';
import { Branch } from '../../../../models/admin/branch';
import { CreateEditBranchComponent } from '../create-edit-branch/create-edit-branch.component';
import { PaginatorPayLoad } from '../../../../models/admin/paginator-payload';
import { SharedUiModule } from '../../../../../shared-ui';

@Component({
    selector: 'app-managebranch',
    standalone: true,
    imports: [SharedUiModule, CreateEditBranchComponent],
    providers: [MessageService, ConfirmationService],
    templateUrl: './managebranch.component.html',
    styleUrl: './managebranch.component.scss'
})
export class ManagebranchComponent {
    exportSettings = {
        columnsHeader: true,
        fileName: 'Awash Bank - Branches',
        hiddenColumns: false
    };
    branches: Branch[] = [];
    selectedBranches: Branch[] = [];
    loading = true;
    outputBranch: any[] = [];
    isEditData = false;
    branchEditDialog = false;
    passBranch = new Branch();
    passBranches: Branch[] = [];
    branch: Branch = new Branch();
    errorMessage = '';
    submitted = false;
    data2 = new Array();
    sizes!: any[];
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Manage Branchs';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    paginatorPayload = new PaginatorPayLoad();

    constructor(
        private branchService: BranchService,
        private exportService: ExportExcelService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) {}

    ngOnInit(): void {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.getBranches();
        // this.getBranches(this.paginatorPayload);
    }

    clear(table: Table) {
        table.clear();
    }

    onGlobalFilter(table: Table, event: Event) {
        const inputValue = (event.target as HTMLInputElement).value;
        this.paginatorPayload.searchText = inputValue;
        this.paginatorPayload.currentPage = 1;
        // this.getBranches(this.paginatorPayload);
    }

    

    openNew() {
        this.outputBranch = [];
        this.branch = new Branch();
        this.isEditData = false;
        this.outputBranch.push(this.branch);
        this.outputBranch.push(this.isEditData);
        this.branchEditDialog = true;
    }

    editBranch(branch: Branch) {
        this.outputBranch = [];
        this.branch = { ...branch };
        this.isEditData = true;
        this.outputBranch.push(this.branch);
        this.outputBranch.push(this.isEditData);
        this.branchEditDialog = true;
    }

    onDataChange(data: any) {
        if (data[1]) {
            // this.getBranches(this.paginatorPayload);
            this.getBranches();
            this.branches = [...this.branches];
            this.branchEditDialog = false;
            this.branch = new Branch();
        } else {
            this.branches[this.findIndexById(data[0].id)] = data[0];
        }
        this.branchEditDialog = false;
    }

    findIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < this.branches.length; i++) {
            if (this.branches[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }

    hideDialog() {
        this.branchEditDialog = false;
    }

    private getBranches() {
        this.branchService.getBranches().subscribe({
            next: (data) => {
                this.loading = false;
                if (data.length > 0) {
                    this.branches = data;
                    const now = new Date();
                    var date = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();
                    this.exportSettings = {
                        columnsHeader: true,
                        fileName: `Awash Bank - Branches ${date}`,
                        hiddenColumns: false
                    };
                  this.paginatorPayload.totalRecords = data[0].total_records_paginator ?? 0;
                } else {
                    this.branches = [];
                    this.paginatorPayload.totalRecords = 0;
                }
            },
            error: (error) => {
                this.loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching branches!',
                    detail: ''
                });
            }
        });
    }

    onPage(event: any) {
        this.paginatorPayload.currentPage = event.first / event.rows + 1;
        this.paginatorPayload.pageSize = event.rows;
        this.paginatorPayload.event_length = event.rows;
        // this.getBranches(this.paginatorPayload);
    }

    generateData(): any[] {
        let data = new Array();
        let i = 0;
        for (const branch of this.branches) {
            let row: any = {
                regionName: String
            };
            // For exporting
            let row2: any = {};

            row['branch'] = branch.name;
            row['branchCode'] = branch.code;
            row['regionName'] = branch.region?.name;
            row['regionCode'] = branch.region?.code;
            row['status'] = branch.status;
            row['id'] = branch.id;
            row['count'] = i + 1;
            data[i] = row;

            //Export Purpose
            row2['Branch Name'] = branch.name;
            row2['Branch Code'] = branch.code;
            row2['Region Name'] = branch.region?.name;
            row2['Region Code'] = branch.region?.code;
            row2['status'] = branch.status ? 'Active' : 'Inactive';
            this.data2[i] = row2;
            i++;
        }
        return data;
    }

    generateExportData() {
        return this.data2;
    }

    excelExport(): void {
        let reportData = {
            sheet_name: 'Branches',
            title: 'FCY & Loan Management System - Branches',
            data: this.generateExportData(),
            headers: Object.keys(this.generateExportData()[0])
        };
        this.exportService.exportExcel(reportData);
    }

    deleteBranch(branch: Branch) {
        this.passBranch = branch;
        this.passBranches.push(this.passBranch);
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected branch?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.branchService.deleteBranches(this.passBranches).subscribe({
                    next: (response) => {
                        this.getBranches();
                        // this.getBranches(this.paginatorPayload);
                        // this.branchs = this.branchs.filter((val) => val.id !== branch.id);
                        this.branch = new Branch();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Branch deactivated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deactivating branch!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    deleteSelectedBranches() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected branches?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.branchService.deleteBranches(this.selectedBranches).subscribe({
                    next: (response) => {
                        this.getBranches();
                        // this.getBranches(this.paginatorPayload);
                        // this.branchs = this.branchs.filter(
                        //   (val) => !this.selectedbranchs.includes(val)
                        // );
                        this.selectedBranches = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Branches Deactivated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deactivating branches!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    activateBranch(branch: Branch) {
        this.passBranch = branch;
        this.passBranches.push(this.passBranch);
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected branch?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.branchService.activateBranches(this.passBranches).subscribe({
                    next: (response) => {
                        this.branches = this.branches.filter((val) => val.id !== branch.id);
                        this.branch = new Branch();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Branch activated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while activating branch!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    activateSelectedBranches() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected branches?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.branchService.activateBranches(this.selectedBranches).subscribe({
                    next: (response) => {
                        this.branches = this.branches.filter((val) => !this.selectedBranches.includes(val));
                        this.selectedBranches = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Branches activated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while activating branches!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }
}
