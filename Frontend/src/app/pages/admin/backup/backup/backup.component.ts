import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { StorageService } from '../../../service/admin/storage.service';
import { Backup } from '../../../../models/admin/backup';
import { BackupService } from '../../../service/admin/backup-service';
import { SharedUiModule } from '../../../../../shared-ui';

@Component({
  selector: 'app-backup',
  imports: [SharedUiModule],
  templateUrl: './backup.component.html',
  styleUrl: './backup.component.scss',
  providers: [MessageService]
})
export class BackupComponent {
  items: MenuItem[] | undefined;
  home: MenuItem | undefined;
  sizes!: any[];
  selectedSize: any = 'normal';
  breadcrumbText: string = 'Manage Backup';
  backup = new Backup();
  loading = false;

  constructor(
    private storageService: StorageService,
    private backupService: BackupService,
    private messageService: MessageService
  ) { }
  
  ngOnInit(): void {
    this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
    const user = this.storageService.getUser();
    this.backup.user_id = user.id;
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
    this.backupService.getBackupByUserId(user_id).subscribe(
      (data) => {
        if (data == null) {
          this.backup.flag = true;
          this.backup.user_id = user_id;
        } else {
          this.backup = data;
        }
      },
      (error) => {
        let errorMessage = 'Oops! something is wrong while backup database ';
        this.messageService.add({
          severity: 'error',
          summary: errorMessage,
          detail: '',
        });
      }
    );
  }
}
