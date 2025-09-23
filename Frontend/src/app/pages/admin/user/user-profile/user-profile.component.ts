import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { environment } from '../../../../../environments/environment.prod';
import { User } from '../../../../models/admin/user';
import { Password } from '../../../../models/admin/password';
import { UserService } from '../../../../service/admin/user.service';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { ImageSnippet } from '../../../../../helpers/image-snippet';
import { SharedUiModule } from '../../../../../shared-ui';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { PasswordService } from '../../../../service/admin/password.service';
import { ValidationService } from '../../../../service/sharedService/validationService';
import { AuthService } from '../../../../service/sharedService/auth.service';

@Component({
    selector: 'app-user-profile',
    imports: [SharedUiModule],
    
    templateUrl: './user-profile.component.html',
    styleUrl: './user-profile.component.scss'
})
export class UserProfileComponent {
    environment = environment;
    isLoggedIn = false;
    private roles: string[] = [];
    userData = new User();
    username?: string;
    title?: string;
    submitted = false;
    passwordData = new Password();
    selectedFile!: ImageSnippet;
    admin = false;
    approver = false;
    followup_officer = false;
    reviewer = false;
    auditee = false;
    auditee_division = false;
    auditor = false;
    errorMessage: string = '';
    user_job_position: string = '';
    id_login_tracker: number = 0;
    is_password_matched: boolean = true;
    email: string = '';
    phone_number: string = '';
    email_status = false;
    phone_number_status = false;
    breadcrumbText: string = 'My Profile';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;

    constructor(
        private storageService: StorageService,
        private userService: UserService,
        private passwordService: PasswordService,
        private validationService: ValidationService,
        private authService: AuthService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) { }

    ngOnInit(): void {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.isLoggedIn = this.storageService.isLoggedIn();
        if (this.isLoggedIn) {
            const user = this.storageService.getUser();
            this.roles = user.roles;
            this.admin = this.roles.includes('ROLE_ADMIN');
            this.approver = this.roles.includes('ROLE_APPROVER');
            this.reviewer = this.roles.includes('ROLE_REVIEWER');
            this.followup_officer = this.roles.includes('ROLE_FOLLOWUP_OFFICER');
            this.auditor = this.roles.includes('ROLE_AUDITOR');
            this.auditee = this.roles.includes('ROLE_AUDITEE');
            this.auditee_division = this.roles.includes('ROLE_AUDITEE_DIVISION');
            this.username = user.email;
            this.title = user.title;
            this.id_login_tracker = user.id_login_tracker;
            this.retrieveUser(user.id);
        }
    }

    retrieveUser(id: any): void {
        this.userService.getUserById(id).subscribe({
            next: (data) => {
                this.userData = data;
                const pattern = /^(AB|AIB)\/(\d{1,7})\/(19|20|21)(\d{2})$/;
                const awash_id = {
                    id_no: this.userData.employee_id?.match(pattern)![2],
                    year: this.userData.employee_id!.match(pattern)![3] + this.userData.employee_id!.match(pattern)![4]
                };

                this.validationService.checkUserEmployeeId(awash_id).subscribe({
                    next: (res: any) => {
                        if (res) {
                            this.user_job_position = res.position;
                        } else {
                            this.user_job_position = '';
                        }
                    },
                    error: (error: HttpErrorResponse) => {
                        this.errorMessage = error.error.message;
                    }
                });
            },
            error: (e) => {
                console.error(e);
            }
        });
    }

    addUser(): void {
        if (this.selectedFile) {
            this.userService.addUser(this.userData, this.selectedFile.file).subscribe({
                next: (res) => {
                    this.submitted = true;
                    this.messageService.add({
                        severity: 'success',
                        summary: `Your profile info is updated successfully.`,
                        detail: ``
                    });
                },
                error: (e) => {
                    console.error(e);
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Something went wrong while changing profile info!',
                        detail: ``
                    });
                }
            });
        } else {
            this.userService.addUser(this.userData).subscribe({
                next: (res) => {
                    this.submitted = true;
                    this.messageService.add({
                        severity: 'success',
                        summary: `Your profile info is updated successfully.`,
                        detail: ``
                    });
                },
                error: (e) => {
                    console.error(e);
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Something went wrong while changing profile info!',
                        detail: ``
                    });
                }
            });
        }
    }

    confirmPassword(event: any) {
        let confirmPassword = event.target.value;
        this.is_password_matched = this.passwordData.password == confirmPassword ? true : false;
    }

    changePassword(): void {
        this.passwordData.id = this.userData.id;
        this.passwordService.changeMyPassword(this.passwordData).subscribe({
            next: (res) => {
                this.submitted = true;
                this.messageService.add({
                    severity: 'success',
                    summary: `Your password is updated successfully.`,
                    detail: ``,
                    life: 6000
                });
                this.storageService.clean();
                window.location.reload();
            },
            error: (e) => {
                console.error(e);
                this.messageService.add({
                    severity: 'error',
                    summary: 'Something went wrong while changing password!',
                    detail: `The old password might be incorrect or the old password and new password are similar.`
                });
            }
        });

        if (this.submitted) {
            this.authService.logout(this.id_login_tracker).subscribe({
                next: (res) => {
                    this.storageService.clean();
                    window.location.reload();
                },
                error: (err) => { }
            });
        }
    }

    processFile(imageInput: any) {
        const file: File = imageInput.files[0];
        const reader = new FileReader();

        reader.addEventListener('load', (event: any) => {
            this.selectedFile = new ImageSnippet(event.target.result, file);
        });

        reader.readAsDataURL(file);
    }

    removeImage() {
        this.userData.photoUrl = '';
        this.addUser();
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
}
