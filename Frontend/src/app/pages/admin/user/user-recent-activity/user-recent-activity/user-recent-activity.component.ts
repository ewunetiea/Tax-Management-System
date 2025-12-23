import { Component } from '@angular/core';
import { environment } from '../../../../../../environments/environment.prod';
import { HttpErrorResponse } from '@angular/common/http';
import { MenuItem, MessageService } from 'primeng/api';
import { User } from '../../../../../models/admin/user';
import { RecentActivity } from '../../../../../models/admin/recent-activity';
import { Report } from '../../../../../models/admin/report';
import { Table } from 'primeng/table';
import { SharedUiModule } from '../../../../../../shared-ui';
import { DateFormat } from '../../../../../service/date-format';
import { ExportExcelService } from '../../../../../service/sharedService/export-excel.service';
import { UserService } from '../../../../../service/admin/user.service';
import { RecentActivityService } from '../../../../../service/sharedService/recent-activity-service';
import { StorageService } from 'app/service/sharedService/storage.service';
import { InputSanitizer } from 'app/SQLi-XSS-Prevention/InputSanitizer';
import * as ExcelJS from 'exceljs';
import { imgBase64 } from 'helpers/logo';
import { saveAs } from 'file-saver';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

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
    user: User = new User();
    breadcrumbText: string = 'User Activities';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    sizes!: any[];
    selectedSize: any = 'normal';
    calendarValue: any = null;
    isAdmin = false;
    roles: string[] = [];
    invalidXss = false;
    selectedTitleForReport: string = "User Recent Activity - Report";

    constructor(
        private messageService: MessageService,
        private exportService: ExportExcelService,
        private userService: UserService,
        private recentactivityService: RecentActivityService,
        private storageService: StorageService,
    ) { }

    ngOnInit(): void {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.user = this.storageService.getUser();
        const users = this.storageService.getUser();
        this.roles = users.roles;
        this.isAdmin = this.roles.includes('ROLE_ADMIN');
        this.getUsers();
    }



    generateActivities(): void {
        this.generateButtonClicked = true;
        this.loading = true;


        if (this.isAdmin) {

            if (this.report.action_date && this.report.action_date.length) {
                this.report.action_date = this.report.action_date.map(d => {
                    if (d) {
                        // Convert to start-of-day UTC Date object
                        return new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()));
                    }
                    return null;
                }) as Date[]; // tell TS this is Date[]
            }


            // Admin activity fetch
            this.recentactivityService.getRecentActivityAdmin(this.report).subscribe({
                next: (response) => {
                    this.userActivities = response;
                    this.loading = false;
                },
                error: (error: HttpErrorResponse) => this.handleError(error)
            });
        }

        else {


            if (this.report.action_date && this.report.action_date.length) {
                this.report.action_date = this.report.action_date.map(d => {
                    if (d) {
                        // Convert to start-of-day UTC Date object
                        return new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()));
                    }
                    return null;
                }) as Date[]; // tell TS this is Date[]
            }

            this.report.user_id = this.user?.id;

            this.recentactivityService.getActivityByDateAndContent(this.report).subscribe({
                next: (response) => {
                    this.userActivities = response;
                    this.loading = false;
                },
                error: (error: HttpErrorResponse) => this.handleError(error)
            });

        }
    }

    // Centralized error handling
    private handleError(error: HttpErrorResponse): void {
        this.errorMessage = error.message;
        this.messageService.add({
            severity: 'error',
            summary: error.status === 401
                ? 'You are not permitted to perform this action!'
                : 'Something went wrong while fetching user activities!',
            detail: ''
        });
        this.loading = false;
    }

    resetActivities() {
        this.report = new Report();
        this.loading = false;
        this.generateButtonClicked = false;
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

    onDescriptionChange(value: string) {
        // Check if value contains XSS/SQL patterns
        this.invalidXss = InputSanitizer.isInvalid(value);

        if (!this.invalidXss) {
            this.report.content = value;
        }
    }

    async exportExcel() {
        if (!this.userActivities || this.userActivities.length === 0) {
            this.messageService.add({
                severity: "warn",
                summary: "No Data",
                detail: "There is no data to export!",
                life: 2000,
            });
            return;
        }

        try {
            const workbook = new ExcelJS.Workbook();
            const worksheet = workbook.addWorksheet("User Recent Activity Report");

            // ==========================
            // COLUMN DEFINITIONS
            // ==========================
            worksheet.columns = [
                { header: "User First Name", key: "first_name", width: 20 },
                { header: "User Middle Name", key: "middle_name", width: 20 },
                { header: "Email", key: "email", width: 28 },
                { header: "Message", key: "message", width: 40 },
                { header: "Created Date", key: "created_date", width: 22 },
            ];

            // ==========================
            // LOGO
            // ==========================
            worksheet.mergeCells("A1:B1");
            const imageId = workbook.addImage({
                base64: imgBase64,
                extension: "png",
            });
            worksheet.addImage(imageId, {
                tl: { col: 0.2, row: 0.2 },
                ext: { width: 120, height: 40 },
            });

            // ==========================
            // TITLE
            // ==========================
            worksheet.mergeCells("C1:D1");
            const titleCell = worksheet.getCell("C1");
            titleCell.value = "User Recent Activity Report";
            titleCell.font = { size: 20, bold: true };
            titleCell.alignment = { horizontal: "center", vertical: "middle" };

            // ==========================
            // DATE
            // ==========================
            const today = new Date().toISOString().split("T")[0];
            const dateCell = worksheet.getCell("E1");
            dateCell.value = `Printed Date: ${today}`;
            dateCell.font = { italic: true, bold: true };
            dateCell.alignment = { horizontal: "right", vertical: "middle" };
            worksheet.addRow([]);

            // ==========================
            // HEADER ROW
            // ==========================
            const headerRow = worksheet.addRow(
                worksheet.columns.map(col => col.header)
            );

            headerRow.eachCell(cell => {
                cell.font = { bold: true, color: { argb: "FFFFFFFF" } };
                cell.fill = {
                    type: "pattern",
                    pattern: "solid",
                    fgColor: { argb: "4472C4" },
                };
                cell.alignment = { horizontal: "center", vertical: "middle" };
                cell.border = {
                    top: { style: "thin" },
                    left: { style: "thin" },
                    bottom: { style: "thin" },
                    right: { style: "thin" },
                };
            });

            // ==========================
            // DATA ROWS
            // ==========================
            this.userActivities.forEach(activity => {
                const row = worksheet.addRow({
                    first_name: activity.user?.first_name ?? "",
                    middle_name: activity.user?.middle_name ?? "",
                    email: activity.user?.email ?? "",
                    message: activity.message ?? "",
                    created_date: activity.created_date ? new Date(activity.created_date).toLocaleString("en-CA") : "",
                });

                row.eachCell((cell, colNumber) => {
                    cell.border = {
                        top: { style: "thin" },
                        left: { style: "thin" },
                        bottom: { style: "thin" },
                        right: { style: "thin" },
                    };

                    // Message column left-aligned
                    if (colNumber === 4) {
                        cell.alignment = { horizontal: "left", vertical: "middle", wrapText: true };
                    } else {
                        cell.alignment = { horizontal: "center", vertical: "middle" };
                    }
                });
            });

            // ==========================
            // FREEZE HEADER
            // ==========================
            worksheet.views = [{ state: "frozen", ySplit: 3 }];

            // ==========================
            // FOOTER
            // ==========================
            worksheet.addRow([]);
            worksheet.addRow([]);
            const footerRow = worksheet.addRow(["Generated from https://tms.awashbank.com/",]);
            footerRow.getCell(1).font = {
                italic: true,
                size: 11,
                color: { argb: "FF666666" },
            };
            footerRow.getCell(1).alignment = {
                horizontal: "center",
                vertical: "middle",
            };
            worksheet.mergeCells(
                footerRow.number,
                1,
                footerRow.number,
                worksheet.columnCount
            );

            // ==========================
            // EXPORT FILE
            // ==========================
            const buffer = await workbook.xlsx.writeBuffer();
            saveAs(new Blob([buffer]), `user_recent_activity_${today}.xlsx`);

            this.messageService.add({
                severity: "success",
                summary: "Success",
                detail: "Excel report exported successfully!",
                life: 2000,
            });
        } catch (error) {
            console.error("Error exporting Excel:", error);
            this.messageService.add({
                severity: "error",
                summary: "Export Failed",
                detail: "An error occurred while exporting the file.",
                life: 3000,
            });
        }
    }

    async exportWord() {
        if (!this.userActivities || this.userActivities.length === 0) {
            this.messageService.add({
                severity: "warn",
                summary: "No Data",
                detail: "There is no data to export!",
                life: 2000,
            });
            return;
        }

        try {
            // ==========================
            // COLUMN DEFINITIONS
            // ==========================
            const wordColumns = [
                { header: "User First Name", key: "first_name" },
                { header: "User Middle Name", key: "middle_name" },
                { header: "Email", key: "email" },
                { header: "Message", key: "message" },
                { header: "Created Date", key: "created_date" },
            ];

            // ==========================
            // DATE FORMAT
            // ==========================
            const d = new Date();
            const formattedDate = `${d.getFullYear()}-${(d.getMonth() + 1)
                .toString()
                .padStart(2, "0")}-${d.getDate().toString().padStart(2, "0")}`;

            // ==========================
            // IMAGE FIX (BASE64)
            // ==========================
            const logoBase64 = imgBase64.startsWith("data:image")
                ? imgBase64
                : `data:image/png;base64,${imgBase64}`;

            // ==========================
            // WORD HTML CONTENT
            // ==========================
            const htmlContent = `
<html xmlns:o="urn:schemas-microsoft-com:office:office"
      xmlns:w="urn:schemas-microsoft-com:office:word"
      xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta charset="utf-8">
<title>${this.selectedTitleForReport}</title>

<style>
@page Section1 {
  size: 841.95pt 595.35pt;
  mso-page-orientation: landscape;
  margin: 1in;
}
div.Section1 { page: Section1; }

body {
  font-family: Calibri, sans-serif;
  font-size: 11px;
}

table {
  border-collapse: collapse;
  width: 100%;
}

th, td {
  border: 1px solid #000;
  padding: 6px;
  font-size: 10px;
  vertical-align: middle;
}

th {
  background-color: #D9E1F2;
  font-weight: bold;
  text-align: center;
}

td.message {
  word-break: break-word;
}

.header-table td {
  border: none;
}

.title {
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  color: #0085A3;
}

.date {
  text-align: right;
  font-size: 12px;
}

.footer {
  margin-top: 20px;
  text-align: center;
  font-weight: bold;
}
</style>
</head>

<body>
<div class="Section1">

<table class="header-table">
<tr>
  <td width="25%">
    <img src="${logoBase64}" width="120" height="30"/>
  </td>
  <td width="50%" class="title">
    ${this.selectedTitleForReport}
  </td>
  <td width="25%" class="date">
    Export Date: ${formattedDate}
  </td>
</tr>
</table>

<br/>

<table>
<thead>
<tr>
${wordColumns.map(col => `<th>${col.header}</th>`).join("")}
</tr>
</thead>
<tbody>
${this.userActivities
                    .map(activity => `
<tr>
  <td>${activity.user?.first_name ?? ""}</td>
  <td>${activity.user?.middle_name ?? ""}</td>
  <td>${activity.user?.email ?? ""}</td>
  <td class="message">${activity.message ?? ""}</td>
  <td>${activity.created_date
                            ? new Date(activity.created_date).toLocaleString("en-CA")
                            : ""
                        }</td>
</tr>
`)
                    .join("")}
</tbody>
</table>

<div class="footer">
  Generated from https://tms.awashbank.com/tms/
</div>

</div>
</body>
</html>
`;

            // ==========================
            // DOWNLOAD WORD FILE
            // ==========================
            const blob = new Blob([htmlContent], {
                type: "application/msword;charset=utf-8",
            });

            const url = URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = url;
            link.download = `${this.selectedTitleForReport}_${formattedDate}.doc`;
            link.click();
            URL.revokeObjectURL(url);

            this.messageService.add({
                severity: "success",
                summary: "Success",
                detail: "Word report exported successfully!",
                life: 2000,
            });
        } catch (error) {
            console.error("Word export error:", error);
            this.messageService.add({
                severity: "error",
                summary: "Error",
                detail: "Error generating Word document",
                life: 3000,
            });
        }
    }

    async exportPDF() {
        if (!this.userActivities || this.userActivities.length === 0) {
            this.messageService.add({
                severity: "warn",
                summary: "No Data",
                detail: "There is no data to export!",
                life: 2000,
            });
            return;
        }

        try {
            const pdf = new jsPDF("l", "pt", "a4");
            const pageWidth = pdf.internal.pageSize.getWidth();
            const pageHeight = pdf.internal.pageSize.getHeight();

            // ==========================
            // DATE
            // ==========================
            const d = new Date();
            const formattedDate = `${d.getFullYear()}-${(d.getMonth() + 1)
                .toString()
                .padStart(2, "0")}-${d.getDate().toString().padStart(2, "0")}`;

            // ==========================
            // LOGO
            // ==========================
            const logoBase64 = imgBase64.startsWith("data:image") ? imgBase64 : `data:image/png;base64,${imgBase64}`;
            const imgProps = pdf.getImageProperties(logoBase64);
            const imgWidth = 120;
            const imgHeight = (imgProps.height * imgWidth) / imgProps.width;
            pdf.addImage(logoBase64, "PNG", 40, 20, imgWidth, imgHeight);

            // ==========================
            // TITLE
            // ==========================
            pdf.setFontSize(16);
            pdf.setTextColor("#0085A3");
            pdf.text(this.selectedTitleForReport, pageWidth / 2, 40, {
                align: "center",
            });

            // ==========================
            // DATE (RIGHT)
            // ==========================
            pdf.setFontSize(12);
            pdf.setTextColor(0);
            pdf.text(`Date: ${formattedDate}`, pageWidth - 40, 40, {
                align: "right",
            });

            // ==========================
            // TABLE
            // ==========================
            autoTable(pdf, {
                startY: 70 + imgHeight,
                head: [[
                    "First Name",
                    "Middle Name",
                    "Email",
                    "Message",
                    "Created Date",
                ]],
                body: this.userActivities.map(activity => [
                    String(activity.user?.first_name ?? ""),
                    String(activity.user?.middle_name ?? ""),
                    String(activity.user?.email ?? ""),
                    String(activity.message ?? ""),
                    activity.created_date
                        ? new Date(activity.created_date).toLocaleString("en-CA")
                        : "",
                ]),
                theme: "grid",
                styles: {
                    fontSize: 10,
                    cellPadding: 5,
                    overflow: "linebreak",
                    valign: "middle",
                },
                headStyles: {
                    fillColor: [68, 114, 196],
                    textColor: 255,
                    halign: "center",
                },
                columnStyles: {
                    0: { cellWidth: 100 },
                    1: { cellWidth: 110 },
                    2: { cellWidth: 200 },
                    3: { cellWidth: 260 }, // Message column
                    4: { cellWidth: 120 },
                },
                didDrawPage: () => {
                    pdf.setFontSize(10);
                    pdf.setTextColor(100);
                    pdf.text("Generated from https://tms.awashbank.com/tms/",
                        pageWidth / 2,
                        pageHeight - 20,
                        { align: "center" }
                    );
                },
            });

            // ==========================
            // SAVE FILE
            // ==========================
            pdf.save(`${this.selectedTitleForReport}_${formattedDate}.pdf`);

            this.messageService.add({
                severity: "success",
                summary: "Success",
                detail: "PDF report exported successfully!",
                life: 2000,
            });
        } catch (error) {
            console.error("PDF export error:", error);
            this.messageService.add({
                severity: "error",
                summary: "Error",
                detail: "Error generating PDF document",
                life: 3000,
            });
        }
    }


}
