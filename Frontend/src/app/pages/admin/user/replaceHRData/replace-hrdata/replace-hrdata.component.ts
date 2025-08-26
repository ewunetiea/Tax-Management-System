import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { StorageService } from '../../../../service/admin/storage.service';
import { UserService } from '../../../../service/admin/user.service';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@Component({
  selector: 'app-replace-hrdata',
  imports: [CommonModule, ToastModule, ButtonModule, MessagesModule, ProgressSpinnerModule],
  templateUrl: './replace-hrdata.component.html',
  styleUrl: './replace-hrdata.component.scss',
  providers: [MessageService, ConfirmationService]
})
export class ReplaceHRDataComponent {
  loading: boolean = false;

  constructor(
    private userService: UserService,
    private storageService: StorageService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  replaceHRData() {
    this.confirmationService.confirm({
      message:
        'Are you certain you want to proceed with updating the HR data for AFRFMS?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.loading = true;
        this.userService.replaceHRData().subscribe({
          next: (response) => {
            // setTimeout(() => {
              this.loading = false;
            // }, 8000);
            this.messageService.add({
              severity: 'success',
              summary: 'Successful',
              detail: 'The HR data has been successfully updated.',
              life: 3000,
            });
          },
          error: (error: HttpErrorResponse) => {
            this.loading = false;
            this.messageService.add({
              severity: 'error',
              summary:
                error.status == 401
                  ? 'You are not permitted to perform this action!'
                  : 'Something went wrong while updating HR Data!',
              detail: '',
            });
          },
        });
      },
    });
  }
}
