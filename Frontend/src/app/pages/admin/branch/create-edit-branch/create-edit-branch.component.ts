import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Branch } from '../../../../models/admin/branch';
import { MessageService } from 'primeng/api';
import { Region } from '../../../../models/admin/region';
import { BranchService } from '../../../../service/admin/branchService';
import { RegionService } from '../../../../service/admin/regionService';
import { PaginatorPayLoad } from '../../../../models/admin/paginator-payload';
import { User } from '../../../../models/admin/user';
import { HttpErrorResponse } from '@angular/common/http';
import { ValidationService } from '../../../../service/sharedService/validationService';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { SharedUiModule } from '../../../../../shared-ui';

@Component({
    selector: 'app-create-edit-branch',
    standalone: true,
    imports: [SharedUiModule],
    templateUrl: './create-edit-branch.component.html',
    styleUrl: './create-edit-branch.component.scss'
})
export class CreateEditBranchComponent {
    branches: Branch = new Branch();
    branch_name: string = '';
    dropList: any[] = [];
    modalRef: any = null;
    regions: Region[] = [];
    branch_code: string = '';
    branch_name_status: boolean = false;
    branch_code_status: boolean = false;
    errorMessage: string = '';
    branch_region: Region = new Region();
    message: boolean = false;
    confrimationDialog: boolean = false;
    loading: boolean = false;
    isEditData: boolean = false;
    regionDropdownOptions: Region[] = [];
    loadLazyTimeout: any = null;
    loading_dropdown: boolean = true;
    paginatorPayload = new PaginatorPayLoad();
    user: User = new User();

    @Input() passedBranch: any[] = [];
    @Output() editedBranch: EventEmitter<any> = new EventEmitter();

    constructor(
        private branchService: BranchService,
        private regionService: RegionService,
        private validationService: ValidationService,
        private storageService: StorageService,
        private messageService: MessageService
    ) {}

    ngOnInit(): void {
        this.user = this.storageService.getUser();
        this.regionDropdownOptions = Array.from({ length: 1000 });
        this.isEditData = this.passedBranch[1];
        if (this.isEditData) {
            this.editBranch(this.passedBranch);
        } else {
            this.openNew();
        }
        this.getAllRegions();
    }

    editBranch(passedData: any[]) {
        this.branches = passedData[0];
    }

    openNew() {
        this.branches = new Branch();
        this.branch_name_status = false;
        this.branch_code_status = false;
    }

    emitData(data: any[]) {
        this.editedBranch.emit(data);
    }

    onDropdownLoad(event: any, identifier: String) {
        this.loading_dropdown = true;
        if (this.loadLazyTimeout) {
            clearTimeout(this.loadLazyTimeout);
        }
        this.loadLazyTimeout = setTimeout(
            () => {
                const { first, last } = event;
                this.regionDropdownOptions = this.regions;
                this.loading_dropdown = false;
            },
            Math.random() * 1000 + 250
        );
    }

    saveBranch() {
        this.loading = true;
        this.branches.user_id = this.user.id;
        this.branchService.createBranch(this.branches).subscribe({
            next: (data) => {
                this.message = true;
                this.loading = false;
                if (this.branches.id) {
                    this.messageService.add({
                        severity: 'success',
                        summary: ` ${this.branches.name} successfully updated`,
                        detail: '',
                        life: 3000
                    });
                } else {
                    this.messageService.add({
                        severity: 'success',
                        summary: ` ${this.branches.name} successfully created`,
                        detail: '',
                        life: 3000
                    });
                    this.branches = new Branch();
                }
                this.passedBranch = [];
                this.passedBranch.push(this.branches);
                this.passedBranch.push(this.isEditData);
                this.emitData(this.passedBranch);
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while creating branch !',
                    detail: ''
                });
                this.errorMessage = error.error.message;
            }
        });
    }

    checkCodeStatus(event: any) {
        this.branch_code = event.target.value;
        this.validationService.checkBranchCode(this.branch_code).subscribe(
            (res) => {
                if (res) {
                    this.branch_code_status = true;
                } else {
                    this.branch_code_status = false;
                }
            },
            (error) => {
                this.errorMessage = error.error.message;
            }
        );
    }

    checkNameStatus(event: any) {
        this.branch_name = event.target.value;
        this.validationService.checkBranchName(this.branch_name).subscribe(
            (res) => {
                if (res) {
                    this.branch_name_status = true;
                } else {
                    this.branch_name_status = false;
                }
            },
            (error) => {
                //
            }
        );
    }

    getAllRegions() {
        this.regionService.getRegions().subscribe({
            next: (data) => {
                this.regions = data;
            },
            error: (error) => {
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching regions !',
                    detail: '',
                    life: 3000
                });
            }
        });
    }

    openModal() {
        this.confrimationDialog = true;
    }

    hideDialog() {
        this.confrimationDialog = false;
        this.loading = false;
    }

    getSubmitButtonText(): string {
        return this.isEditData ? 'Update' : 'Submit';
    }
}
