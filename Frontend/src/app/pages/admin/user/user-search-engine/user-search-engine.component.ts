import { Component, EventEmitter, Output } from '@angular/core';
import { Branch } from '../../../../models/admin/branch';
import { JobPosition } from '../../../../models/admin/job-position';
import { Region } from '../../../../models/admin/region';
import { User } from '../../../../models/admin/user';
import { BranchService } from '../../../../service/admin/branchService';
import { RegionService } from '../../../../service/admin/regionService';
import { UserService } from '../../../../service/admin/user.service';
import { SharedUiModule } from '../../../../../shared-ui';
import { ValidationService } from '../../../../service/sharedService/validationService';
import { UserFunctionalityService } from '../../../../service/admin/user-functionality-service';
import { Router } from '@angular/router';
import { InputSanitizer } from 'app/SQLi-XSS-Prevention/InputSanitizer';

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
invalidXssFname = false

invalidXssMname = false
invalidXssLname = false
invalidXssPhone = false
invalidXssAwashId = false
invalidXssEmail = false


    @Output() generatedUsers: EventEmitter<any> = new EventEmitter();

    constructor(
        private branchService: BranchService,
        private regionService: RegionService,
        private validationService: ValidationService,
        private userService: UserService,
        private userFunctionalityService: UserFunctionalityService,
        private router: Router,
    ) { }

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
        this.submitted = true;

        // Check current route
        const currentRoute = this.router.url;

        if (currentRoute.includes('manage_user_permissions')) {
            // Call user permission function
            this.userFunctionalityService.getUsers(this.user).subscribe({
                next: (res) => {
                    this.loading = false;
                    this.emitData(res);
                },
                error: () => {
                    this.loading = false;
                    this.submitted = false;
                }
            });
        } else {
            // Call generate users function
            this.userService.generateUsers(this.user).subscribe({
                next: (res) => {
                    this.loading = false;
                    this.emitData(res);
                },
                error: () => {
                    this.loading = false;
                    this.submitted = false;
                }
            });
        }
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
            error: () => {
                this.loading = false;
                this.submitted = false;
            }
        });
    }

    getJobPositions(): void {
        this.validationService.getJobPositions().subscribe({
            next: (data) => {
                this.job_positions = data;
            },
            error: () => {
                this.loading = false;
                this.submitted = false;
            }
        });
    }
    getRegions() {
        this.regionService.getActiveRegions().subscribe({
            next: (data: any) => {
                this.allRegions = data;
            },
            error: () => {
                this.loading = false;
                this.submitted = false;
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


      // In your component (.ts file)
genericInputValidation(event: any, fieldName: string) {
  const value = event.target.value.trim(); // optional: trim spaces

  let isInvalid = false;

  // Use your existing sanitizer
  if (InputSanitizer.isInvalid(value)) {
    isInvalid = true;
  }

  // Optionally: add extra client-side checks if needed (e.g. length, special chars)
  // Example: if (value.length > 50) isInvalid = true;

  // Dynamically set the corresponding flag
  switch (fieldName) {
    case 'first_name':
      this.invalidXssFname = isInvalid;
      break;
    case 'middle_name':
      this.invalidXssMname = isInvalid;
      break;
    case 'last_name':
      this.invalidXssLname = isInvalid;
      break;
    case 'employee_id':
      this.invalidXssAwashId = isInvalid;
      break;
    case 'email':
      this.invalidXssEmail = isInvalid;
      break;
    case 'phone_number':
      this.invalidXssPhone = isInvalid;
      break;
    default:
      console.warn('Unknown field:', fieldName);
  }
}



}
