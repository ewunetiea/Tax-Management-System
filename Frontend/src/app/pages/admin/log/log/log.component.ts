import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { MaxFailedAndJwtControlService } from '../../../service/admin/max-failed-and-jwt-control.service';
import { SharedUiModule } from '../../../../../shared-ui';

@Component({
    standalone:true,
  selector: 'app-log',
    imports: [ SharedUiModule ],
         providers: [MessageService, ConfirmationService],
  templateUrl: './log.component.html',
  styleUrls: ['./log.component.css'],
})
export class LogComponent implements OnInit {

  logs: any[] = [];
  selectedLogs: any[] = [];
  passedLog: any;
  passedLogs: any[] = [];
  loading = true;

  constructor(
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private logService: MaxFailedAndJwtControlService
  ) {}

  ngOnInit(): void {
    this.getAllRecords();
  }
  getAllRecords() {
    this.logService.getAllLogRecord().subscribe(
      (data) => {
        this.logs = data;
        this.loading = false;
      },
      (error: HttpErrorResponse) => {
        this.loading = false;
        this.messageService.add({
          severity: 'error',
          summary:
            error.status == 401
              ? 'You are not permitted to perform this action!'
              : 'Something went wrong while fetching logs !',
          detail: '',
        });
      }
    );
  }

  deleteLog(log: any) {
    this.passedLog = log;
    this.passedLogs.push(this.passedLog);
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete selected log?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.logService.deleteLog(this.passedLogs).subscribe({
          next: (response) => {
            this.logs = this.logs.filter((val) => val.id !== log.id);
            this.messageService.add({
              severity: 'success',
              summary: 'Successful',
              detail: 'Log deleted',
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
                  : 'Something went wrong while deleting log!',
              detail: '',
            });
          },
        });
      },
    });
  }

  deleteSelectedLogs() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete selected logs?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.logService.deleteLog(this.selectedLogs).subscribe({
          next: (response) => {
            this.logs = this.logs.filter(
              (val) => !this.selectedLogs.includes(val)
            );
            this.messageService.add({
              severity: 'success',
              summary: 'Successful',
              detail: 'Logs deleted',
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
                  : 'Something went wrong while deleting logs!',
              detail: '',
            });
          },
        });
      },
    });
  }
}
