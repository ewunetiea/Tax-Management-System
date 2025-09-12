import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Output } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Branch } from '../../../../models/admin/branch';
import { JobPosition } from '../../../../models/admin/job-position';
import { Region } from '../../../../models/admin/region';
import { User } from '../../../../models/admin/user';
import { BranchService } from '../../../service/admin/branchService';
import { RegionService } from '../../../service/admin/regionService';
import { UserService } from '../../../service/admin/user.service';
import { ValidationService } from '../../../service/admin/validationService';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { RadioButtonModule } from 'primeng/radiobutton';
import { SkeletonModule } from 'primeng/skeleton';
import { InputTextModule } from 'primeng/inputtext';
import { CheckboxModule } from 'primeng/checkbox';
import { FluidModule } from 'primeng/fluid';
import { SharedUiModule } from '../../../../../shared-ui';

@Component({
    selector: 'app-user-search-engine',
    imports: [SharedUiModule],
    templateUrl: './user-search-engine.component.html',
    styleUrl: './user-search-engine.component.scss'
})
export class UserSearchEngineComponent {
    selectedBranch: Branch = new Branch();
    selectedRegion: Region = new Region();
    selectedHO: Region = new Region();
    selectedJobPosition: JobPosition = {};
    allBranches: Branch[] = [];
    users: User[] = [];
    filteredBranches: Branch[] = [];
    allRegions: Region[] = [];
    errorMessage = '';
    submitted = false;
    loading: boolean = false;
    user = new User();
    job_positions: JobPosition[] = [];
    confrimationDialog = false;
    loadLazyTimeout: any;
    loading_dropdown = true;
    jobDropdownOptions: JobPosition[] = [];
    regionDropdownOptions: Region[] = [];
    branchDropdownOptions: Branch[] = [];
    hoDropdownOptions: Branch[] = [];
    isEditData = false;
    @Output() generatedUsers: EventEmitter<any> = new EventEmitter();

    constructor(
        private branchService: BranchService,
        private regionService: RegionService,
        private validationService: ValidationService,
        private userService: UserService,
        private messageService: MessageService
    ) {}

    ngOnInit(): void {
        this.jobDropdownOptions = Array.from({ length: 1000 });
        this.regionDropdownOptions = Array.from({ length: 1000 });
        this.branchDropdownOptions = Array.from({ length: 1000 });
        this.hoDropdownOptions = Array.from({ length: 1000 });
        this.getRegions();
        this.getBranches();
        this.getJobPositions();
    }

    emitData(data: User[]) {
        this.generatedUsers.emit(data);
    }

    onSelectRegion(item: any) {
        this.filteredBranches = this.allBranches.filter((branch) => branch.region?.id == item.id);
    }

    generateUsers(): void {
        this.loading = true;
        this.userService.generateUsers(this.user).subscribe({
            next: (res) => {
                this.loading = false;
                this.emitData(res);
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.submitted = false;
                this.errorMessage = error.error.message;

                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'Something went wrong while searching users'
                });
            }
        });
    }

    reloadPage(): void {
        window.location.reload();
    }

    getBranches(): void {
        this.branchService.getActiveBranchesList().subscribe({
            next: (data) => {
                this.allBranches = data;
                this.filteredBranches = data;
            },
            error: (e) => console.error(e)
        });
    }

    getJobPositions(): void {
        this.validationService.getJobPositions().subscribe({
            next: (data) => {
                this.job_positions = data;
            },
            error: (e) => {}
        });
    }
    getRegions() {
        this.regionService.getActiveRegions().subscribe({
            next: (data: any) => {
                this.allRegions = data;
            },
            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.error.message;
            }
        });
    }

    onDropdownLoad(event: any, identifier: String) {
        this.loading_dropdown = true;
        if (this.loadLazyTimeout) {
            clearTimeout(this.loadLazyTimeout);
        }
        this.loadLazyTimeout = setTimeout(
            () => {
                const { first, last } = event;
                if (identifier == 'Job') {
                    this.jobDropdownOptions = this.job_positions;
                } else if (identifier == 'Region') {
                    this.regionDropdownOptions = this.allRegions;
                } else if (identifier == 'Branch') {
                    this.branchDropdownOptions = this.allBranches;
                }
                this.loading_dropdown = false;
            },
            Math.random() * 1000 + 250
        );
    }
}
