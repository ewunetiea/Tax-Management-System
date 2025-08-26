import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RippleModule } from 'primeng/ripple';
import { AppFloatingConfigurator } from '../../layout/component/app.floatingconfigurator';
import { AuthService } from '../service/admin/auth.service';
import { StorageService } from '../service/admin/storage.service';
import { Platform } from '@angular/cdk/platform';
import { Password } from '../../models/admin/password';
import { NgIf } from '@angular/common';
import { PasswordService } from '../service/admin/password.service';
import { User } from '../../models/admin/user';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [ButtonModule, CheckboxModule, InputTextModule, PasswordModule, FormsModule, RouterModule, RippleModule, AppFloatingConfigurator, NgIf],
    template: `
        <app-floating-configurator />
        <div class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-[100vw] overflow-hidden">
            <div class="flex flex-col items-center justify-center">
                <div style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%)">
                    <div class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20" style="border-radius: 53px">
                        <div class="text-center mb-8">
                            <img src="assets/img/awashbank.png" alt="Awash Bank Logo" class="mb-8 w-16 mx-auto" />
                            <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">Welcome to tms!</div>
                            <span class="text-muted-color font-medium">Sign in to continue</span>
                        </div>

                        <form #f="ngForm" name="form" (ngSubmit)="onSubmit()" novalidate>
                            <div>
                                <label for="username" class="block text-surface-900 dark:text-surface-0 text-xl font-medium mb-2">Email</label>
                                <input pInputText name="username" type="text" placeholder="Email address" class="w-full md:w-[30rem] mb-2" [(ngModel)]="form.username" required #usernameModel="ngModel" />
                                <div *ngIf="f.submitted && usernameModel.invalid" class="text-red-500 mb-2">
                                    <small *ngIf="usernameModel.errors?.['required']">Email is required.</small>
                                </div>

                                <label for="password1" class="block text-surface-900 dark:text-surface-0 font-medium text-xl mb-2">Password</label>
                                <p-password
                                    name="password"
                                    [(ngModel)]="form.password"
                                    placeholder="Password"
                                    [toggleMask]="true"
                                    styleClass="mb-2"
                                    [feedback]="false"
                                    required
                                    #passwordModel="ngModel"
                                    [style]="{ width: '100%', 'max-width': '30rem' }"
                                    [inputStyle]="{ width: '100%' }"
                                ></p-password>
                                <div *ngIf="f.submitted && passwordModel.invalid" class="text-red-500 mb-2">
                                    <small *ngIf="passwordModel.errors?.['required']">Password is required.</small>
                                </div>

                                <div class="flex items-center justify-between mt-2 mb-8 gap-8">
                                    <div class="flex items-center">
                                        <p-checkbox [(ngModel)]="checked" id="rememberme1" name="rememberme" binary class="mr-2"></p-checkbox>
                                        <label for="rememberme1">Remember me</label>
                                    </div>
                                    <span class="font-medium no-underline ml-2 text-right cursor-pointer text-secondary">Forgot password?</span>
                                </div>
                                <p-button label="Sign In" styleClass="w-full" type="submit" [disabled]="f.invalid"></p-button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    `
})
export class Login {
    form: any = {
        username: null,
        password: null
    };
    checked: boolean = false;
    isLoginFailed = false;
    errorMessage = '';
    errorMessage1 = '';
    passwordChangeCheck: boolean = false;
    change: boolean = false;
    userData = new User();
    message = '';
    submitted = false;
    passwordData = new Password();
    loading: boolean = false;
    exception: String[] = [];
    userAgent?: String;
    is_password_matched: boolean = true;

    constructor(
        private authService: AuthService,
        private storageService: StorageService,
        private passwordService: PasswordService,
        private platform: Platform
    ) {}
    ngOnInit(): void {}

    togglePassword() {
        const passwordInput = document.querySelector('#yourPassword');
        const eye = document.querySelector('#eye');
        eye?.classList.toggle('bi-eye');
        const type = passwordInput?.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput?.setAttribute('type', type);
    }

    onSubmit(): void {
        if (this.platform.BLINK) {
            this.userAgent = 'Chrom(Webkit or old Edge version) device';
        } else {
            if (this.platform.IOS) {
                this.userAgent = 'IOS device';
            } else {
                if (this.platform.FIREFOX) {
                    this.userAgent = 'FIREFOX device';
                } else {
                    if (this.platform.WEBKIT) {
                        this.userAgent = ' WebKit-based browser (Opera) device';
                    } else {
                        if (this.platform.TRIDENT) {
                            this.userAgent = ' IE device';
                        } else {
                            if (this.platform.EDGE) {
                                this.userAgent = ' EDGE device';
                            } else {
                                if (this.platform.SAFARI) {
                                    this.userAgent = ' SAFARI device';
                                } else {
                                    if (this.platform.ANDROID) {
                                        this.userAgent = ' ANDROID  device';
                                    } else {
                                        this.userAgent = ' browser type unknown  device';
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        this.loading = true;
        const { username, password } = this.form;

        this.authService.login(username, password, this.userAgent).subscribe({
            next: (data) => {
                this.loading = false;
                this.isLoginFailed = false;
                this.storageService.saveUser(data);
                const user = this.storageService.getUser();
                window.location.href = '/applayout/';
                // if (user.roles.includes('ROLE_BRANCHM_BFA') || user.roles.includes('ROLE_AUDITEE_INS')) {
                //     const loginUrl = '/afrfms/afrfms-gateway';
                //     window.location.href = loginUrl;
                // } else {
                //     if (user.category == 'BFA') {
                //         const financialUrl = 'https://afrfms.awashbank.com/financial';
                //         window.location.href = financialUrl;
                //     } else if (user.category == 'INS') {
                //         const inspectionUrl = 'https://afrfms.awashbank.com/inspection';
                //         window.location.href = inspectionUrl;
                //     } else this.relodePage();
                // }
            },
            error: (err) => {
                this.loading = false;
                this.errorMessage = err.error.message;
                if (this.errorMessage.includes('password_expired')) {
                    this.change = true;
                    let errorM: any[] = this.errorMessage.split(' ');
                    this.passwordData.id = errorM[1];
                    this.errorMessage = 'User credentials expired.';
                }
                this.isLoginFailed = true;
            }
        });
    }

    relodePage(): void {
        window.location.reload();
    }

    checkPasswordStatus(): void {
        this.passwordChangeCheck = true;
    }

    changePassword(): void {
        if (this.passwordData.oldPassword != this.passwordData.password) {
            this.loading = true;
            this.passwordService.changeMyPassword(this.passwordData).subscribe({
                next: (res) => {
                    this.loading = false;
                    this.message = '';
                    this.submitted = true;
                    this.storageService.clean();
                    window.location.reload();
                },
                error: (e) => {
                    this.loading = false;
                    console.error(e);
                    this.message = 'Incorrect Old Password!';
                }
            });
        } else {
            this.message = "Old password and new password shouldn't be similar!";
        }
    }
    confirmPassword(event: any) {
        let confirmPassword = event.target.value;
        this.is_password_matched = this.passwordData.password == confirmPassword ? true : false;
    }

    alertClosed(status: String): void {
        if (status === 'success') {
            this.submitted = false;
        } else if (status === 'danger') {
            this.message = '';
        }
    }
}
