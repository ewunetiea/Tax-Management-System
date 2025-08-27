import { Component } from '@angular/core';
import { User } from '../../../models/admin/user';
import { Password } from '../../../models/admin/password';
import { AuthService } from '../../service/admin/auth.service';
import { StorageService } from '../../service/admin/storage.service';
import { Router } from '@angular/router';
import { PasswordService } from '../../service/admin/password.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SharedUiModule } from '../../../../shared-ui';
import { PasswordResetOtpComponent } from '../password-reset-otp/password-reset-otp.component';

@Component({
  selector: 'app-forget-password',
  imports: [SharedUiModule, PasswordResetOtpComponent],
  providers: [MessageService, ConfirmationService],
  templateUrl: './forget-password.component.html',
  styleUrl: './forget-password.component.scss'
})
export class ForgetPasswordComponent {
  form: any = {
    authenthication_media: true,
    email: '',
    phone_number: '',
  };
  errorMessage = '';
  errorMessage1 = '';
  emailSentCheck: boolean = false;
  change: boolean = false;
  userData = new User();

  message = '';
  submitted = false;
  passwordData = new Password();
  loading: boolean = false;
  exception: String[] = [];

  authenthicationOptions = [
    { icon: 'pi pi-envelope', value: true },

    { icon: 'pi pi-phone', value: false },
  ];

  constructor(
    private authService: AuthService,
    private storageService: StorageService,
    private router: Router,
    private passwordService: PasswordService,
    private messageService: MessageService
  ) {}

  onSubmit(): void {
    const userAgent = window.navigator.userAgent;

    this.loading = true;
    let user_agent = window.navigator.userAgent;

    this.passwordService.forgotPassword(user_agent, this.form).subscribe({
      next: (data) => {
        this.loading = false;
        this.emailSentCheck = true;
      },
      error: (err) => {
        this.emailSentCheck = false;
        if (this.form.authenthication_media) {
          this.messageService.add({
            severity: 'error',
            summary:
              'The provided email is not verfied before, or there is interent connection issue !',
            detail: '',
            life: 10000,
          });
        } else {
          this.messageService.add({
            severity: 'error',
            summary:
              'The provided phone number is not verfied before, or there is interent connection issue !',
            detail: '',
            life: 10000,
          });
        }
        this.loading = false;
      },
    });
  }
  relodePage(): void {
    window.location.reload();
  }
  goHome(): void {
    this.router.navigateByUrl('/');
  }

  onDataChange() {
    this.emailSentCheck = false;
    this.loading = false;
  }

}
