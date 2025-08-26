import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { MessageService } from 'primeng/api';
import { StorageService } from '../../../service/admin/storage.service';
import { Backup } from '../../../../models/admin/backup';
import { BackupService } from '../../../service/admin/backup-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-backup',
  imports: [CommonModule, FormsModule, ButtonModule, ToastModule, CardModule],
  templateUrl: './backup.component.html',
  styleUrl: './backup.component.scss',
  providers: [MessageService]
})
export class BackupComponent {
  backup = new Backup();
  loading = false;

  constructor(
    private storageService: StorageService,
    private backupService: BackupService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loading = true;
    const user = this.storageService.getUser();
    this.backup.user_id = user.id;
    this.loading = false;
  }

  createBackup(): void {
    if (this.backup.flag) {
      this.backup.filepath = '';
    }

    this.loading = true;
    this.backupService.createBackup(this.backup).subscribe({
      next: (data: any) => {
        this.loading = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Database Backup Completed !',
          detail: '',
        });
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false;
        if (error.status == 401) {
          this.messageService.add({
            severity: 'error',
            summary: 'You are not permitted to perform this action!',
            detail: '',
          });
        } else {
          this.messageService.add({
            severity: 'success',
            summary: 'Database Backup Completed !',
            detail: '',
          });
        }
      },
    });
  }

  getBackupByUserId(user_id: any): void {
    this.loading = true;
    this.backupService.getBackupByUserId(user_id).subscribe({
      next: (data) => {
        this.loading = false;
        if (data == null) {
          this.backup.flag = true;
          this.backup.user_id = user_id;
        } else {
          this.backup = data;
        }
      },
      error: (error) => {
        this.loading = false;
        let errorMessage = 'Oops! something is wrong while backup database ';
        this.messageService.add({
          severity: 'error',
          summary: errorMessage,
          detail: '',
        });
      }
    });
  }
}
