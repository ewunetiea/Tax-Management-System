import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, OnInit, Renderer2 } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ScheduleService } from '../../../service/admin/scheduleService';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';

interface Schedule {
  id: number;
  name: string;
  description: string;
  status: string;
}

@Component({
  selector: 'app-schedules',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    CheckboxModule
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './schedules.component.html',
  styleUrl: './schedules.component.scss'
})
export class SchedulesComponent implements OnInit {
  schedules: Schedule[] = [];
  functionalities_status: Record<number, boolean> = {};
  loading: boolean = true;
  functionality_size = 0;

  constructor(
    private scheduleService: ScheduleService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
  ) {}

  ngOnInit(): void {
    this.loadSchedules();
  }

  loadSchedules() {
    this.loading = true;
    this.scheduleService.getSchedules().subscribe(
      (resp: any) => {
        this.schedules = resp;
        this.functionalities_status = {};
        resp.forEach((schedule: Schedule) => {
          this.functionalities_status[schedule.id] = schedule.status !== '0';
        });
        this.loading = false;
      },
      (error: HttpErrorResponse) => {
        this.messageService.add({
          severity: 'error',
          summary: error.status === 401
            ? 'You are not permitted to perform this action!'
            : 'Something went wrong!',
          detail: ''
        });
        this.loading = false;
      }
    );
  }

  onStatusChange(event: any, scheduleId: number) {
    this.functionalities_status[scheduleId] = event.checked;
  }

  updateStatus() {
    this.scheduleService.updateScheduleStatus(this.functionalities_status).subscribe(
      (data: any) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Successfully updated',
          detail: '',
          life: 3000
        });
        this.loadSchedules();
      },
      (error: HttpErrorResponse) => {
        this.messageService.add({
          severity: 'error',
          summary: error.status === 401
            ? 'You are not permitted to perform this action!'
            : 'Something went wrong while updating schedule status!',
          detail: ''
        });
      }
    );
  }
}
