import { Component } from '@angular/core';
import { environment } from '../../../../../../environments/environment.prod';
import { HttpErrorResponse } from '@angular/common/http';
import { MenuItem, MessageService } from 'primeng/api';
import { User } from '../../../../../models/admin/user';
import { ExportExcelService } from '../../../../service/admin/export-excel.service';
import { UserService } from '../../../../service/admin/user.service';
import { RecentActivity } from '../../../../../models/admin/recent-activity';
import { Report } from '../../../../../models/admin/report';
import { RecentActivityService } from '../../../../service/admin/recent-activity-service';
import { DateFormat } from '../../../../service/date-format';
import { Table } from 'primeng/table';
import { SharedUiModule } from '../../../../../../shared-ui';

@Component({
    selector: 'app-user-recent-activity',
    imports: [SharedUiModule],
    templateUrl: './user-recent-activity.component.html',
    styleUrl: './user-recent-activity.component.scss',
    providers: [MessageService]
})
export class UserRecentActivityComponent {
    environment = environment;
    report = new Report();
    date_format: DateFormat = new DateFormat();
    message?: any;
    generateButtonClicked = false;
    selectedActivities: RecentActivity[] = [];
    userActivities: RecentActivity[] = [];
    loading = true;
    errorMessage: string = '';
    data2: any[] = [];
    users: User[] = [];
    breadcrumbText: string = 'User Activities';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    sizes!: any[];
    selectedSize: any = 'normal';
    calendarValue: any = null;

    constructor(
        private messageService: MessageService,
        private exportService: ExportExcelService,
        private userService: UserService,
        private recentactivityService: RecentActivityService
    ) {}

    ngOnInit(): void {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.getUsers();
    }

    generateActivities(): void {
        this.generateButtonClicked = true;
        this.recentactivityService.getRecentActivityAdmin(this.report).subscribe({
            next: (response) => {
                this.loading = false;
                this.userActivities = response;
                // this.generateUserActivity();
            },
            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.message;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching user activities!',
                    detail: ''
                });
                this.loading = false;
            }
        });
    }

    getUsers(): void {
        this.userService.getUsers().subscribe({
            next: (response) => {
                this.users = response;
            },
            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.message;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching users!',
                    detail: ''
                });
                this.loading = false;
            }
        });
    }

    onGlobalFilter(table: Table, event: Event) {
        const input = event.target as HTMLInputElement;
        table.filterGlobal(input.value, 'contains');
    }

    clear(table: Table) {
        table.clear();
    }

    exportPdf() {
        // import('jspdf').then((jsPDF) => {
        //   import('jspdf-autotable').then((x) => {
        //     const doc = new jsPDF.default(0, 0);
        //     doc.autoTable(this.exportColumns, this.audits);
        //     doc.save('products.pdf');
        //   });
        // });
    }

    generateUserActivity(): any[] {
        let data = new Array();
        let i = 0;
        for (const u_activity of this.userActivities) {
            let row: any = {};
            let row2: any = {};
            if (u_activity.user?.roles) {
                for (const uactivity of u_activity.user?.roles) {
                    if (uactivity.name?.includes('ROLE_ADMIN')) {
                        continue;
                    } else {
                        row['number'] = i + 1;
                        row['full_name'] = u_activity.user?.first_name + ' ' + u_activity.user?.last_name;
                        row['message'] = u_activity?.message;
                        row['date_created'] = this.date_format.dateFormatter(u_activity?.created_date);

                        row2['Full Name'] = u_activity.user?.first_name + ' ' + u_activity.user?.last_name;
                        row2['Message'] = u_activity?.message;
                        row2['Date Created'] = this.date_format.dateFormatter(u_activity?.created_date);
                        data[i] = row;
                        this.data2[i] = row2;
                        i++;
                    }
                }
            }
        }
        return data;
    }

    generateExportData(): any[] {
        return this.data2;
    }

    excelExport(): void {
        let reportData = {
            sheet_name: 'User Activities',
            title: 'AFRFMS - User Activities',
            data: this.generateExportData(),
            headers: Object.keys(this.generateExportData()[0])
        };
        this.exportService.exportExcel(reportData);
    }
}
