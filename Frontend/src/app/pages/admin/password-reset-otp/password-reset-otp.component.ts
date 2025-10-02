import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PasswordAndToken } from '../../../models/admin/password-and-token';
import { StorageService } from '../../../service/sharedService/storage.service';
import { Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SharedUiModule } from '../../../../shared-ui';
import { AuthService } from '../../../service/sharedService/auth.service';
import { PasswordService } from '../../../service/admin/password.service';

@Component({
    selector: 'app-password-reset-otp',
    imports: [SharedUiModule],
    
    templateUrl: './password-reset-otp.component.html',
    styleUrl: './password-reset-otp.component.scss'
})
export class PasswordResetOtpComponent {
    count = 0; // 2 minutes in seconds
    otp: string = '';
    loading = false;
    counter = '';
    is_verified = false;
    interval: any;
    is_email = false;
    passwordData = new PasswordAndToken();

    @Input() passedPhoneNumber: any;
    @Input() passedData: any;
    @Output() outputData: EventEmitter<any> = new EventEmitter();

    constructor(
        private authService: AuthService,
        private storageService: StorageService,
        private router: Router,
        private passwordService: PasswordService,
        private messageService: MessageService
    ) {}

    ngOnInit(): void {
        try {
            this.is_email = this.passedData;
        } catch (error) {
            this.is_email = false;
        }
        this.startCounter();
    }

    startCounter() {
        this.count = 120; // 2 minutes in seconds
        this.interval = setInterval(() => {
            const minutes = Math.floor(this.count / 60);
            const seconds = this.count % 60;
            this.counter = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
            this.count--;
            if (this.count < 0) {
                this.emitData();
                clearInterval(this.interval);
            }
        }, 1000);
    }

    emitData() {
        this.outputData.emit();
    }

    verifyOTP(): void {
        this.loading = true;
        this.passwordService.verifyOTP(this.otp).subscribe({
            next: (data) => {
                this.loading = false;
                this.is_verified = true;
                this.otp = data;
                clearInterval(this.interval);
            },
            error: (err) => {
                this.is_verified = false;
                this.messageService.add({
                    severity: 'error',
                    summary: 'The provided verification code is invalid, or there is interent connection issue !',
                    detail: '',
                    life: 10000
                });

                this.loading = false;
            }
        });
    }

    changePassword(): void {
        this.passwordData.token = this.otp;
        this.loading = true;
        this.passwordService.changePasswordWithToken(this.passwordData).subscribe({
            next: (res) => {
                this.loading = false;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Your password successfully changed!',
                    detail: '',
                    life: 10000
                });
                setTimeout(() => {
                    this.router.navigateByUrl('/');
                }, 4000);
            },
            error: (e) => {
                console.error(e);
                this.messageService.add({
                    severity: 'error',
                    summary: 'Something went wrong while changing password!',
                    detail: '',
                    life: 10000
                });
                this.loading = false;
            }
        });
    }
}
