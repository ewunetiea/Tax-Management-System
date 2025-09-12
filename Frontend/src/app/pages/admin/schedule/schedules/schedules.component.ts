import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MessageService, ConfirmationService, MenuItem} from 'primeng/api';
import { ScheduleService } from '../../../service/admin/scheduleService';
import { SharedUiModule } from '../../../../../shared-ui';
import { Table } from 'primeng/table';

interface Schedule {
    id: number;
    name: string;
    description: string;
    status: string;
}

@Component({
    selector: 'app-schedules',
    standalone: true,
    imports: [SharedUiModule],
    providers: [MessageService, ConfirmationService],
    templateUrl: './schedules.component.html',
    styleUrl: './schedules.component.scss'
})
export class SchedulesComponent implements OnInit { 
  schedules: any[] = [];
  selectedSchedules: any[] = [];
  loading: boolean = true;
   breadcrumbText: string = 'Manage Schedules';
  items: MenuItem[] | undefined;
  home: MenuItem | undefined;
  sizes!: any[];
  selectedSize: any = 'normal';

  constructor(
    private scheduleService: ScheduleService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
  ) { }

  ngOnInit(): void {
    this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
    this.fetchSchedules();
  }

  onGlobalFilter(table: Table, event: Event) {
    const input = event.target as HTMLInputElement;
    table.filterGlobal(input.value, 'contains');
  }
  
  clear(table: Table) {
    table.clear();
  }

  fetchSchedules() {
    this.scheduleService.getSchedules().subscribe({
      next: (data) => {
        this.loading = false
        this.schedules = data;
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false

        this.messageService.add({
          severity: 'error',
          summary:
            error.status == 401
              ? 'You are not permitted to perform this action!'
              : 'Something went wrong while retrieving schedules!',
          detail: '',
        });
      },
    });
  }

  updateStatus(status: string) {
    this.selectedSchedules[0].status = status == 'Activate' ? 1 : 0;
    this.confirmationService.confirm({
      message: `Are you sure you want to ${status} selected schedule?`,
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.scheduleService.updateScheduleStatus(this.selectedSchedules).subscribe({
          next: (response) => {
            this.fetchSchedules();
            this.selectedSchedules = [];
            this.messageService.add({
              severity: 'success',
              summary: 'Successful',
              detail: 'Schedules status are successfully update !',
              life: 3000,
            });
          },
          error: (error: HttpErrorResponse) => {
            this.messageService.add({
              severity: 'error',
              summary:
                error.status == 401
                  ? 'You are not permitted to perform this action!'
                  : 'Something went wrong while updating schedules!',
              detail: '',
            });
          },
        });
      },
    }
  );
  }
}
