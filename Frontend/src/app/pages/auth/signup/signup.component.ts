import { Component } from '@angular/core';
import { SharedUiModule } from '../../../../shared-ui';
import { Region } from '../../../models/admin/region';
import { Branch } from '../../../models/admin/branch';
import { User } from '../../../models/admin/user';
import { JobPosition } from '../../../models/admin/job-position';
import { BranchService } from '../../service/admin/branchService';
import { RegionService } from '../../service/admin/regionService';
import { ValidationService } from '../../service/admin/validationService';
import { ConfirmationService, MessageService } from 'primeng/api';
import { AuthService } from '../../service/admin/auth.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { catchError, of, switchMap } from 'rxjs';
import { AppFloatingConfigurator } from "../../../layout/component/app.floatingconfigurator";

@Component({
    selector: 'app-signup',
    imports: [SharedUiModule, AppFloatingConfigurator],
    providers: [MessageService, ConfirmationService],
    templateUrl: './signup.component.html',
    styleUrl: './signup.component.scss'
})
export class SignupComponent {
    email_status = false;
    user_name_status = false;
    phone_number_status = false;
    employee_id_status = false;
    employee_id_status_system = false;
    proceed = false;
    email: string = '';
    phone_number: string = '';
    employee_id: string = '';
    allBranches: Branch[] = [];
    regions: Region[] = [];
    errorMessage = '';
    submitted = false;
    radio_button_place_validation = true;
    userFromHr: any = {};
    loading: Boolean = false;
    user = new User();
    job_positions: JobPosition[] = [];
    confrimationDialog = false;
    radio_value: string = '';
    authenthicationOptions?: any[];
    account_created = false;

    constructor(
        private branchService: BranchService,
        private regionService: RegionService,
        private validationService: ValidationService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private authService: AuthService,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.authenthicationOptions = [
            { icon: 'pi pi-envelope', value: true },
            { icon: 'pi pi-phone', value: false }
        ];

        this.getBranches();
        this.getJobPositions();
        this.getRegions();
    }

    getBranches(): void {
        this.branchService.getActiveBranchesList().subscribe({
            next: (data) => {
                this.allBranches = data;
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
            },

            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.error.message;
            }
        });
    }

    reloadPage(): void {
        window.location.reload();
    }

    checkEmailStatus(event: any) {
        this.email = event.target.value;
        this.validationService.checkUserEmail(this.email).subscribe({
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
        this.validationService.checkUserPhoneNumber(this.phone_number).subscribe({
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
                        console.log('User already exists in the system');
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

    addUser(): void {
        this.loading = true;
        this.authService.signup(this.user).subscribe({
            next: (res) => {
                this.loading = false;
                this.confrimationDialog = false;
                this.submitted = true;
                this.errorMessage = '';
                // this.messageService.add({
                //   severity: 'success',
                //   summary: `User ${this.user.first_name} ${this.user.middle_name}  ${this.user.last_name}is created successfully!`,
                //   detail: `Your password is sent to your email at ${this.user.email}. Please check on your email to proceed.`,
                // });
                if (this.user.authenthication_media) {
                    this.messageService.add({
                        severity: 'success',
                        summary: `User account for ${this.user.first_name} ${this.user.middle_name}  ${this.user.last_name} is created successfully.`,
                        detail: `Your password is sent to your email at ${this.user.email}. Please check on your email to proceed.`,
                        life: 6000
                    });
                } else {
                    this.messageService.add({
                        severity: 'success',
                        summary: `User account for ${this.user.first_name} ${this.user.middle_name}  ${this.user.last_name} is created successfully.`,
                        detail: `Verification code is sent to your phone number at ${this.user.phone_number}. Please check on to proceed for password ammendement.`,
                        life: 6000
                    });
                    setTimeout(() => {
                        this.account_created = true;
                    }, 4000);
                }
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

    onDataChange() {
        this.router.navigateByUrl('/');
        this.loading = false;
    }

    scrollTop() {
        window.scroll({
            top: 0,
            left: 0,
            behavior: 'smooth'
        });
    }
}
