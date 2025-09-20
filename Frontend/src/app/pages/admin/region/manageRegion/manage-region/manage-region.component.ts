import { Component } from '@angular/core';
import { Region } from '../../../../../models/admin/region';
import { HttpErrorResponse } from '@angular/common/http';
import { ConfirmationService, MessageService, MenuItem } from 'primeng/api';
import { AddRegionComponent } from '../../addRegion/add-region/add-region.component';
import { Table } from 'primeng/table';
import { PaginatorPayLoad } from '../../../../../models/admin/paginator-payload';
import { SharedUiModule } from '../../../../../../shared-ui';
import { RegionService } from '../../../../../service/admin/regionService';
import { StorageService } from '../../../../../service/sharedService/storage.service';
import { ExportExcelService } from '../../../../../service/sharedService/export-excel.service';

@Component({
    selector: 'app-manage-region',
    standalone: true,
    imports: [SharedUiModule, AddRegionComponent],
    templateUrl: './manage-region.component.html',
    styleUrl: './manage-region.component.scss'
})
export class ManageRegionComponent {
    region: Region = new Region();
    region_name_exist_status = false;
    region_code_exist_status = false;
    modalRef: any;
    submitted: boolean = false;
    errorMessage = '';
    reg: Region = new Region();
    regions: Region[] = [];
    selectedRegions: Region[] = [];
    success: boolean = false;

    //used for exporting, Header titles given
    data2 = new Array();
    outputRegion: any[] = [];
    isEditData = false;
    loading = true;
    regionEditDialog = false;
    passRegion = new Region();
    passRegions: Region[] = [];
    sizes!: any[];
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Manage Regions';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    paginatorPayload = new PaginatorPayLoad();

    exportSettings = {
        columnsHeader: true,
        fileName: 'Region Report',
        hiddenColumns: false
    };

    constructor(
        private regionService: RegionService,
        private storageService: StorageService,
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
        const user = this.storageService.getUser();
        this.region.user_id = user.id;
        this.getRegion();
    }

    getRegion(): void {
        this.regionService.getRegions().subscribe({
            next: (response) => {
                this.loading = false;
                if (response.length > 0) {
                    this.regions = response;

                    // Generate a formatted date for the export filename
                    const now = new Date();
                    const date = `${now.getFullYear()}-${now.getMonth() + 1}-${now.getDate()}`;

                    // Export settings for table download
                    this.exportSettings = {
                        columnsHeader: true,
                        fileName: `Awash Bank - Regions ${date}`,
                        hiddenColumns: false
                    };

                    // Update paginator total records
                    this.paginatorPayload.totalRecords = response[0]?.total_records_paginator ?? 0;
                } else {
                    // No regions found
                    this.regions = [];
                    this.paginatorPayload.totalRecords = 0;
                }
            },
            error: (err) => {
                this.loading = false;

                this.messageService.add({
                    severity: 'error',
                    summary: err.status === 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching regions!',
                    detail: ''
                });
            }
        });
    }

    onPage(event: any) {
        this.paginatorPayload.currentPage = event.first / event.rows + 1;
        this.paginatorPayload.pageSize = event.rows;
        this.paginatorPayload.event_length = event.rows;
    }

    openNew() {
        this.outputRegion = [];
        this.region = new Region();
        this.isEditData = false;
        this.outputRegion.push(this.region);
        this.outputRegion.push(this.isEditData);
        this.regionEditDialog = true;
    }

    editRegion(region: Region) {
        this.outputRegion = [];
        this.region = { ...region };
        this.isEditData = true;
        this.outputRegion.push(this.region);
        this.outputRegion.push(this.isEditData);
        this.regionEditDialog = true;
    }

    onGlobalFilter(table: Table, event: Event) {
        const input = event.target as HTMLInputElement;
        table.filterGlobal(input.value, 'contains');
    }

    clear(table: Table) {
        table.clear();
    }

    onDataChange(data: any) {
        if (data[1]) {
            this.getRegion();
            this.regions = [...this.regions];
            this.regionEditDialog = false;
            this.region = new Region();
        } else {
            this.regions[this.findIndexById(data[0].id)] = data[0];
        }
        this.regionEditDialog = false;
    }

    findIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < this.regions.length; i++) {
            if (this.regions[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }

    deleteRegion(region: Region) {
        this.passRegion = region;
        this.passRegions.push(this.passRegion);
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected audit?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.regionService.deleteRegion(this.passRegions).subscribe({
                    next: (response) => {
                        this.getRegion();
                        this.region = new Region();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Region deactivated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deactivating region!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    deleteSelectedRegion() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected regions?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.regionService.deleteRegion(this.selectedRegions).subscribe({
                    next: (response) => {
                        this.getRegion();
                        this.selectedRegions = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Regions Deactivated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deactivating regions!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    activateRegion(region: Region) {
        this.passRegion = region;
        this.passRegions.push(this.passRegion);
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected region?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.regionService.activateRegion(this.passRegions).subscribe({
                    next: (response) => {
                        this.regions = this.regions.filter((val) => val.id !== region.id);
                        this.region = new Region();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Region activated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while activating region!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    activateSelectedRegion() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected regions?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.regionService.activateRegion(this.selectedRegions).subscribe({
                    next: (response) => {
                        this.regions = this.regions.filter((val) => !this.selectedRegions.includes(val));
                        this.selectedRegions = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Regions activated',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.errorMessage = error.message;
                        this.submitted = true;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while activating regions!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    generateData(): any[] {
        let data = new Array();
        let i = 0;
        for (const reg of this.regions) {
            let row: any = {};
            let row2: any = {};

            row['name'] = reg.name;
            row['code'] = reg.code;
            row['id'] = reg.id;
            row['status'] = reg.status;
            data[i] = row;

            row2['Region Name'] = reg.name;
            row2['Region Code'] = reg.code;
            row2['Status'] = reg.status ? 'Active' : 'Inactive';
            this.data2[i] = row2;

            row['count'] = i + 1;
            i++;
        }
        return data;
    }

    generateExportData() {
        return this.data2;
    }
    excelExport(): void {
        let reportData = {
            sheet_name: 'Regions',
            title: 'FCY & Loan Management System - Regions',
            data: this.generateExportData(),
            headers: Object.keys(this.generateExportData()[0])
        };
        this.exportService.exportExcel(reportData);
    }
}
