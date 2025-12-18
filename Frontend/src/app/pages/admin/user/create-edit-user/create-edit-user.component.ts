import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Branch } from '../../../../models/admin/branch';
import { JobPosition } from '../../../../models/admin/job-position';
import { Region } from '../../../../models/admin/region';
import { User } from '../../../../models/admin/user';
import { BranchService } from '../../../../service/admin/branchService';
import { RegionService } from '../../../../service/admin/regionService';
import { UserService } from '../../../../service/admin/user.service';
import { SharedUiModule } from '../../../../../shared-ui';
import { catchError, of, switchMap } from 'rxjs';
import { ValidationService } from '../../../../service/sharedService/validationService';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { AuthService } from '../../../../service/sharedService/auth.service';
import { InputSanitizer } from 'app/SQLi-XSS-Prevention/InputSanitizer';

interface AwashId {
    id_no: string;
    year: string;
}

@Component({
    standalone: true,
    selector: 'app-create-edit-user',
    imports: [SharedUiModule],
    templateUrl: './create-edit-user.component.html',
    styleUrl: './create-edit-user.component.scss'
})
export class CreateEditUserComponent {
    user_name_status = false;
    email_status = false;
    phone_number_status = false;
    employee_id_status = false;
    real_region_status = false;
    real_branch_status = false;
    real_ho_status = false;
    real_job_position_status = false;
    employee_id_status_system = false;
    proceed = false;
    email?: string;
    phone_number?: string;
    employee_id?: string;
    regionRadioButton: boolean = false;
    branchRadioButton: boolean = false;
    headOfficeRadioButton: boolean = false;
    selectedBranch: Branch = new Branch();
    selectedRegion: Region = new Region();
    selectedHO: Region = new Region();
    selectedJobPosition: JobPosition = {};
    allBranches: Branch[] = [];
    headOfficeData: Region[] = [];
    regions: Region[] = [];
    headOfficeFilter: Region[] = [];
    regionFilter: Region[] = [];
    errorMessage = '';
    submitted = false;
    showJobPosition = false;
    radio_button_place_validation = true;
    userFromHr: any = {};
    loading: Boolean = false;
    user = new User();
    job_positions: JobPosition[] = [];
    confrimationDialog = false;
    loadLazyTimeout: any;
    loading_dropdown = true;
    jobDropdownOptions: JobPosition[] = [];
    regionDropdownOptions: Region[] = [];
    branchDropdownOptions: Branch[] = [];
    hoDropdownOptions: Branch[] = [];
    radio_value?: string;
    isEditData = false;
    authenthicationOptions: any[] = [];
    ho: Region[] = [];
    hoWorkPlaces: Branch[] = [];
    @Input() passedUser: any[] = [];
    @Output() editedUser: EventEmitter<any> = new EventEmitter();
    radioValue: any = null;
invalidXssFname = false

invalidXssMname = false
invalidXssLname = false
invalidXssPhone = false
invalidXssAwashId = false
invalidXssEmail = false
invalidXssUsername = false

    constructor(
        private branchService: BranchService,
        private regionService: RegionService,
        private validationService: ValidationService,
        private userService: UserService,
        private storageService: StorageService,
        private router: Router,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private authService: AuthService
    ) {}

    ngOnInit(): void {
        this.authenthicationOptions = [
            { icon: 'pi pi-envelope', value: true },
            { icon: 'pi pi-phone', value: false }
        ];

        this.isEditData = this.passedUser[1];
        if (this.isEditData) {
            this.openNew();
        } else {
            this.editAudit(this.passedUser);
        }

        this.jobDropdownOptions = Array.from({ length: 1000 });
        this.regionDropdownOptions = Array.from({ length: 1000 });
        this.branchDropdownOptions = Array.from({ length: 1000 });
        this.hoDropdownOptions = Array.from({ length: 1000 });
        this.getRegions();
        this.getBranches();
        this.getJobPositions();
    }

    editAudit(passedData: any[]) {
        this.user = passedData[0];
        this.proceed = true;
        this.showJobPosition = true;
        if (this.user.region) {
            this.regionRadioButton = true;
            this.radio_value = 'Region';
        } else if (this.user.branch) {
            if (this.user.branch.region?.name?.toLocaleLowerCase().trim() === 'ho' || this.user.branch.region?.name?.toLocaleLowerCase().trim() === 'head office') {
                this.headOfficeRadioButton = true;
                this.radio_value = 'HO';
            } else {
                this.branchRadioButton = true;
                this.radio_value = 'Branch';
            }
        }
        this.userFromHr = this.user.userCopyFromHR;
        this.radio_button_place_validation = false;
    }

    openNew() {
        this.user = new User();
    }

    emitData(data: any[]) {

        this.editedUser.emit(data);
    }

    onSelectBranch(item: any) {
        if (item.name.trim() == this.userFromHr.unit.trim()) {
            this.real_branch_status = false;
        } else {
            this.real_branch_status = true;
        }
        this.user.region = {};
    }

    onBranchClear() {
        this.real_branch_status = false;
    }

    onSelectJobPosition(item: any) {
        if (item.title.trim() == this.userFromHr.position.trim()) {
            this.real_job_position_status = false;
        } else {
            this.real_job_position_status = true;
        }
    }
    onJobPositionClear() {
        this.real_job_position_status = false;
    }

    onSelectRegion(item: any) {
        // if (this.headOfficeRadioButton) {
        //   if (this.userFromHr.deptLocation == item.name) {
        //     this.real_ho_status = false;
        //   } else {
        //     this.real_ho_status = true;
        //   }
        // } else {
        if (item.name.trim() == this.userFromHr.deptLocation.trim()) {
            this.real_region_status = false;
        } else {
            this.real_region_status = true;
        }
        // }
        this.user.branch = {};
    }

    onSelectHO(item: any) {
        if (this.userFromHr.unit.trim() == item.name.trim()) {
            this.real_ho_status = false;
        } else {
            this.real_ho_status = true;
        }
        this.user.region = {};
    }

    onRegionClear() {
        this.real_region_status = false;
    }
    onHoClear() {
        this.real_ho_status = false;
    }

    addUser(): void {
        this.loading = true;
        this.user.admin_id = this.storageService.getUser().id;
        this.authService.signup(this.user).subscribe({
            next: (res) => {
                this.loading = false;
                this.confrimationDialog = false;
                this.submitted = true;
                this.errorMessage = '';

                if (this.user.id) {
                    this.messageService.add({
                        severity: 'success',
                        summary: ` ${this.user.first_name}  ${this.user.middle_name} information is successfully updated`,
                        detail: '',
                        life: 6000
                    });
                } else {
                    if (this.user.authenthication_media) {
                        this.messageService.add({
                            severity: 'success',
                            summary: `User ${this.user.first_name} ${this.user.middle_name}  ${this.user.last_name}is created successfully!`,
                            detail: `Your password is sent to your email at ${this.user.email}. Please check on your email to proceed.`,
                            life: 6000
                        });
                    } else {
                        this.messageService.add({
                            severity: 'success',
                            summary: `User ${this.user.first_name} ${this.user.middle_name}  ${this.user.last_name}is created successfully!`,
                            detail: `Your password is sent to your phone number at ${this.user.phone_number}. Please check on your password to proceed.`,
                            life: 6000
                        });
                    }

                    this.user = new User();
                }
                this.passedUser = [];
                this.passedUser.push(this.user);
                this.passedUser.push(this.isEditData);

                this.emitData(this.passedUser);
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.submitted = false;
                this.errorMessage = error.error.message;

                this.messageService.add({
                    severity: 'error',
                    summary: 'Something went wrong while signup!',
                    detail: ''
                });
            }
        });
    }

    reloadPage(): void {
        window.location.reload();
    }

    checkEmailStatus(event: any) {
        this.email = event.target.value;
        this.validationService.checkUserEmail(this.email as any).subscribe({
            next: (res: any) => {
                if (res) {
                    this.email_status = true;
                } else {
                    this.email_status = false;
                }
            },
            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.error.message;
            }
        });
    }

    checkUsernameStatus(event: any) {
        this.validationService.checkUsername(event.target.value).subscribe({
            next: (res: any) => {
                if (res) {
                    this.user_name_status = true;
                } else {
                    this.user_name_status = false;
                }
            },
            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.error.message;
            }
        });
    }
    checkPhoneNumberStatus(event: any) {
        this.phone_number = event.target.value;
        this.validationService.checkUserPhoneNumber(this.phone_number ?? '').subscribe({
            next: (res: any) => {
                if (res) {
                    this.phone_number_status = true;
                } else {
                    this.phone_number_status = false;
                }
            },
            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.error.message;
            }
        });
    }

    checkAwashBankIdStatus(event: any) {
        this.employee_id = event.target.value;

        if (!this.employee_id || this.employee_id.length < 11) {
            return;
        }

        const pattern = /^(AB|AIB)\/(\d{1,7})\/(19|20|21)(\d{2})$/;
        const match = this.employee_id.match(pattern);

        if (!match) {
            return;
        }

        const awash_id = {
            id_no: match[2],
            year: match[3] + match[4]
        };

        this.validationService
            .checkEmployeeIdSystem(awash_id)
            .pipe(
                switchMap((firstRes: any) => {
                    if (firstRes && firstRes.id != null) {
                        this.employee_id_status_system = true;
                        this.proceed = false;

                        return of(null); // Return empty observable to complete the chain
                    } else {
                        this.employee_id_status_system = false;
                        // Only make second call if first response indicates no user in system
                        return this.validationService.checkUserEmployeeId(awash_id);
                    }
                }),
                catchError((error: HttpErrorResponse) => {
                    this.errorMessage = error.error.message;
                    return of(null);
                })
            )
            .subscribe((secondRes: any) => {
                if (secondRes) {
                    this.userFromHr = secondRes;
                    this.processUserData();
                    this.employee_id_status = false;
                    this.proceed = true;
                } else if (secondRes === null) {
                    // This means user exists in system (first call returned data)
                    // No action needed as we already handled it
                } else {
                    // Second call returned no data (user doesn't exist in HR either)
                    this.employee_id_status = true;
                    this.user = new User();
                    this.user.employee_id = this.employee_id;
                    this.proceed = false;
                }
            });
    }

    processUserData() {
        this.user.first_name = this.userFromHr.empName.split(' ')[0];
        this.user.middle_name = this.userFromHr.empName.split(' ')[1] || '';
        this.user.last_name = this.userFromHr.empName.split(' ').slice(2).join(' ') || this.user.middle_name;
        this.user.email = this.userFromHr.email;
        this.user.jobPosition = this.job_positions.find((job) => job.title === this.userFromHr.position);
        this.user.branch = this.allBranches.find((branch) => branch.name === this.userFromHr.unit);
        this.user.region = this.regions.find((region) => region.name === this.userFromHr.deptLocation);
    }

    openModal() {
        this.confrimationDialog = true;
    }

    hideDialog() {
        this.confrimationDialog = false;
    }

    getBranches(): void {
        this.branchService.getActiveBranchesList().subscribe({
            next: (data) => {
                this.allBranches = data.filter((branch) => branch.region?.id != this.ho[0].id);
                this.hoWorkPlaces = data.filter((branch) => branch.region?.id === this.ho[0].id);
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
                this.regions = data;
                // for (const region of this.regions) {
                //   if (
                //     region.name?.includes('Head Office') ||
                //     region.name?.includes('HO')
                //   ) {
                //     this.headOfficeFilter.push(region);
                //   } else {
                //     this.regionFilter.push(region);
                //   }
                // }
                this.regionFilter = this.regions.filter((region) => region.name?.toLocaleLowerCase().trim() != 'ho' || region.name?.toLocaleLowerCase().trim() != 'head office');

                this.ho = this.regions.filter((region) => region.name?.toLocaleLowerCase().trim() === 'ho' || region.name?.toLocaleLowerCase().trim() === 'head office');
            },

            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.error.message;
            }
        });
    }

    chooseRegion(data: any) {
        this.user.jobPosition = {};
        this.showJobPosition = true;
        this.regionRadioButton = true;
        this.branchRadioButton = false;
        this.headOfficeRadioButton = false;
        this.real_ho_status = false;
        this.real_branch_status = false;
        this.radio_button_place_validation = false;
        this.user.branch = {};
        this.user.region = {};
    }
    chooseHO(data: any) {
        this.user.jobPosition = {};
        this.showJobPosition = true;
        this.headOfficeRadioButton = true;
        this.regionRadioButton = false;
        this.branchRadioButton = false;
        this.real_region_status = false;
        this.real_branch_status = false;
        this.real_job_position_status = false;
        this.radio_button_place_validation = false;
        this.user.branch = {};
        this.user.region = {};
    }

    chooseBranch(data: any) {
        this.selectedJobPosition = {};
        this.showJobPosition = true;
        this.branchRadioButton = true;
        this.headOfficeRadioButton = false;
        this.regionRadioButton = false;
        this.real_ho_status = false;
        this.real_region_status = false;
        this.radio_button_place_validation = false;
        this.user.region = {};
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
                    this.regionDropdownOptions = this.regionFilter;
                } else if (identifier == 'Branch') {
                    this.branchDropdownOptions = this.allBranches;
                } else if (identifier == 'HO') {
                    this.hoDropdownOptions = this.hoWorkPlaces;
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
       case 'user_name':
      this.invalidXssUsername = isInvalid;
      break;
    default:
      console.warn('Unknown field:', fieldName);
  }
}


}
