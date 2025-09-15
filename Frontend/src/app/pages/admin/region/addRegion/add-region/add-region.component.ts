import { Component, EventEmitter, Input, Output } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { Region } from '../../../../../models/admin/region';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { User } from '../../../../../models/admin/user';
import { RegionService } from '../../../../../service/admin/regionService';
import { StorageService } from '../../../../../service/sharedService/storage.service';

@Component({
    selector: 'app-add-region',
    standalone: true,
    imports: [ConfirmDialogModule, FormsModule, CommonModule, DialogModule, ButtonModule, InputTextModule],
    templateUrl: './add-region.component.html',
    styleUrl: './add-region.component.scss'
})
export class AddRegionComponent {
    region: Region = new Region();
    user: User = new User();
    region_name_exist_status = false;
    region_code_exist_status = false;
    submitted: boolean = false;
    errorMessage = '';
    success: boolean = false;
    loading = false;
    isEditData = false;
    originalName: string = '';
    originalCode: string = '';

    @Input() passedRegion: any[] = [];
    @Output() editedRegion: EventEmitter<any> = new EventEmitter();

    constructor(
        private regionService: RegionService,
        private storageService: StorageService,
        private messageService: MessageService
    ) {}

    ngOnInit(): void {
        this.user = this.storageService.getUser();

        // Check if we're in edit mode
        if (this.passedRegion && this.passedRegion.length > 0) {
            this.isEditData = this.passedRegion[1];
            if (this.isEditData) {
                this.editRegion(this.passedRegion[0]);
            } else {
                this.openNew();
            }
        } else {
            this.openNew();
        }
    }

    editRegion(regionData: Region) {
        this.region = { ...regionData };
        this.region_name_exist_status = false;
        this.region_code_exist_status = false;
        this.submitted = false;
        this.originalName = this.region.name || '';
        this.originalCode = this.region.code || '';
    }

    openNew() {
        this.region = new Region();
        this.region_name_exist_status = false;
        this.region_code_exist_status = false;
    }

    emitData(data: any[]) {
        this.editedRegion.emit(data);
    }

    saveRegion() {
        this.region.user_id = this.user.id;
        this.loading = true;
        this.regionService.saveRegion(this.region).subscribe({
            next: (data) => {
                this.loading = false;
                if (this.region.id) {
                    this.messageService.add({
                        severity: 'success',
                        summary: ` ${this.region.name} successfully updated`,
                        detail: '',
                        life: 3000
                    });
                } else {
                    this.messageService.add({
                        severity: 'success',
                        summary: ` ${this.region.name} successfully created`,
                        detail: '',
                        life: 3000
                    });
                    this.region = new Region();
                }
                this.passedRegion = [];
                this.passedRegion.push(this.region);
                this.passedRegion.push(this.isEditData);
                this.emitData(this.passedRegion);
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while creating region !',
                    detail: ''
                });
                this.errorMessage = error.error.message;
            }
        });
    }

    checkRegionNameExist() {
        if (this.isEditData && this.region.name === this.originalName) {
            this.region_name_exist_status = false;
            return;
        }

        this.regionService.checkRegionNameExist(this.region).subscribe({
            next: (data) => {
                this.region_name_exist_status = data;
            },
            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.error.message;
            }
        });
    }

    checkRegionCodeExist() {
        if (this.isEditData && this.region.code === this.originalCode) {
            this.region_code_exist_status = false;
            return;
        }

        this.regionService.checkRegionCodeExist(this.region).subscribe({
            next: (data) => {
                this.region_code_exist_status = data;
            },
            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.error.message;
            }
        });
    }

    // Method to get submit button text based on edit mode
    getSubmitButtonText(): string {
        return this.isEditData ? 'Update' : 'Submit';
    }
}
