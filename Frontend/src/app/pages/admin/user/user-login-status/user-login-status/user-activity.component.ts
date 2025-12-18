import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { PaginatorPayLoad } from '../../../../../models/admin/paginator-payload';
import { OnlineFailedUsers } from '../../../../../models/admin/online-failed-users';
import { Table } from 'primeng/table';
import { UserService } from '../../../../../service/admin/user.service';
import { ExportExcelService } from '../../../../../service/sharedService/export-excel.service';
import * as ExcelJS from 'exceljs';
import { saveAs } from 'file-saver';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { SharedUiModule } from 'shared-ui';
import { imgBase64 } from 'helpers/logo';

@Component({
    standalone: true,
    selector: 'app-user-activity',
    imports: [SharedUiModule],
    templateUrl: './user-activity.component.html',
    styleUrl: './user-activity.component.scss',
    providers: [MessageService, ConfirmationService]
})
export class UserActivityComponent {
    onlineFailedUsers: OnlineFailedUsers[] = [];
    selectedOnlineFailedUsers: OnlineFailedUsers[] = [];
    loading = true;
    active = 0;
    live = true;
    active_count = 0;

    //used for exporting, Header titles given
    data2 = new Array();
    exportSettings: {
        columnsHeader: boolean;
        fileName: string;
        hiddenColumns: boolean;
    } = {
            columnsHeader: true,
            fileName: 'default_filename',
            hiddenColumns: false
        };
    totalRecords: any = 0;
    paginatorPayload = new PaginatorPayLoad();
    breadcrumbText: string = 'User Login Status';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    sizes!: any[];
    selectedSize: any = 'normal';
    selectedTitleForReport: string = "User Login Status - Report";

    constructor(
        private userService: UserService,
        private exportService: ExportExcelService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) { }

    ngOnInit(): void {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.generateOnlineFailedUsers(this.paginatorPayload);
    }

    generateOnlineFailedUsers(paginatorPayLoad: PaginatorPayLoad) {
        this.userService.getOnlineFailedUsers(paginatorPayLoad).subscribe({
            next: (data) => {
                this.onlineFailedUsers = data;
                if (data.length > 0) {
                    this.onlineFailedUsers = data;
                    this.totalRecords = data[0].total_records_paginator;
                } else {
                    this.onlineFailedUsers = [];
                    this.totalRecords = 0;
                }
                this.loading = false;
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching users login status !',
                    detail: ''
                });
            }
        });
    }

    onPage(event: any) {
        this.paginatorPayload.currentPage = event.first / event.rows + 1;
        this.paginatorPayload.pageSize = event.rows;
        this.paginatorPayload.event_length = event.rows;
        this.generateOnlineFailedUsers(this.paginatorPayload);
    }

    onGlobalFilter(table: Table, event: Event) {
        const inputValue = (event.target as HTMLInputElement).value;
    }

    clear(table: Table) {
        table.clear();
    }

    generateUserData(): any[] {
        let data = new Array();
        let i = 0;
        let now = new Date().getTime();
        let time: any;
        for (const online of this.onlineFailedUsers) {
            let row: any = {
                region: String
            };
            let row2: any = {};

            row['user_name'] = online.user_name;
            // row['user_agent'] = online.user_agent;
            row['ip_address'] = online.ip_address;
            row['last_login_time'] = online.login_time;
            row['user_agent'] = online.user_agent;

            row2['User Name'] = online.user_name;
            // row['user_agent'] = online.user_agent;
            row2['Ip Address'] = online.ip_address;
            // row2['Last login time'] = online.login_time;
            row2['User Agent'] = online.user_agent;

            var last = Date.parse(row['last_login_time']);
            time = now - last;
            let diffday = Math.floor(time / 86400000);
            let diffhoure = Math.floor((time % 86400000) / 3600000);
            let diffmin = Math.floor(((time % 86400000) % 3600000) / 60000);

            if (diffday >= 1) {
                row['login_time'] = diffday + ' days ago';
                row2['Login time'] = diffday + ' days ago';
            } else {
                if (diffhoure >= 1) {
                    row['login_time'] = diffhoure + ' hours ago';
                    row2['Login time'] = diffhoure + ' hours ago';
                } else {
                    if (diffmin >= 1) {
                        row['login_time'] = diffmin + ' minutes ago';
                        row2['Login time'] = diffmin + ' minutes ago';
                    } else {
                        row['login_time'] = 'Recently';
                        row2['Login time'] = 'Recently';
                    }
                }
            }
            if (online.status == 1) {
                row['status'] = 'Online';
                row2['Status'] = 'Online';
            } else {
                row['status'] = 'Failed';
                row2['Status'] = 'Failed';
            }
            row['count'] = i + 1;
            data[i] = row;
            this.data2[i] = row2;

            i++;
            this.active_count = row['count'];
        }
        return data;
    }

    generateExportData(): any[] {
        return this.data2;
    }

    excelExport(): void {
        const now = new Date();
        var date = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();
        this.exportSettings = {
            columnsHeader: true,
            fileName: `Awash Bank - Users login status ${date}`,
            hiddenColumns: false
        };
        this.generateUserData();

        let reportData = {
            sheet_name: 'User Login Status',
            title: 'Audit Management System - User Login Status',
            data: this.generateExportData(),
            headers: Object.keys(this.generateExportData()[0])
        };
        this.exportService.exportExcel(reportData);
    }

    deleteSelectedLoginStatus() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete selected login statuses?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.userService.updateLoginStatus(this.selectedOnlineFailedUsers).subscribe({
                    next: (response) => {
                        this.onlineFailedUsers = this.onlineFailedUsers.filter((val) => !this.selectedOnlineFailedUsers.includes(val));
                        this.selectedOnlineFailedUsers = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Login Statuses Deleted',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deleting login status!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }


    async exportExcel() {
        if (!this.onlineFailedUsers || this.onlineFailedUsers.length === 0) {
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
            const worksheet = workbook.addWorksheet("User Login Status Report");

            // ==========================
            // COLUMN DEFINITIONS
            // ==========================
            worksheet.columns = [
                { header: "User Name", key: "user_name", width: 20 },
                { header: "Ip Address", key: "ip_address", width: 18 },
                { header: "Browser", key: "user_agent", width: 30 },
                { header: "Login Time", key: "login_time", width: 22 },
                { header: "Status", key: "status", width: 15 },
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
            titleCell.value = "User Login Status Report";
            titleCell.font = { size: 20, bold: true };
            titleCell.alignment = { horizontal: "center", vertical: "middle" };

            // ==========================
            // DATE
            // ==========================
            const today = new Date().toISOString().split("T")[0];
            worksheet.mergeCells("E1:E1");
            const dateCell = worksheet.getCell("E1");
            dateCell.value = `Date: ${today}`;
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
            this.onlineFailedUsers.forEach(user => {
                const row = worksheet.addRow({
                    user_name: user.user_name ?? "",
                    ip_address: user.ip_address ?? "",
                    user_agent: user.user_agent ?? "",
                    login_time: user.login_time ? new Date(user.login_time).toLocaleString("en-CA") : "",
                    status: user.status === 1 ? "Online" : "Failed",
                });

                row.eachCell(cell => {
                    cell.alignment = { horizontal: "center", vertical: "middle" };
                    cell.border = {
                        top: { style: "thin" },
                        left: { style: "thin" },
                        bottom: { style: "thin" },
                        right: { style: "thin" },
                    };
                });
            });

            // ==========================
            // FREEZE HEADER
            // ==========================
            worksheet.views = [{ state: "frozen", ySplit: 4 }];

            // ==========================
            // FOOTER
            // ==========================
            worksheet.addRow([]);
            worksheet.addRow([]);
            const footerRow = worksheet.addRow([
                "Generated from https://tms.awashbank.com/",
            ]);
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
            saveAs(new Blob([buffer]), `tms_report_${today}.xlsx`);

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
        if (!this.onlineFailedUsers || this.onlineFailedUsers.length === 0) {
            this.messageService.add({
                severity: "warn",
                summary: "No Data",
                detail: "There is no data to export!",
                life: 2000,
            });
            return;
        }

        try {
            const columns = [
                { header: "User Name", key: "user_name" },
                { header: "Ip Address", key: "ip_address" },
                { header: "Browser", key: "user_agent" },
                { header: "Login Time", key: "login_time" },
                { header: "Status", key: "status" },
            ];

            const d = new Date();
            const formattedDate = `${d.getFullYear()}-${(d.getMonth() + 1)
                .toString()
                .padStart(2, "0")}-${d.getDate().toString().padStart(2, "0")}`;

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

  body { font-family: Calibri, sans-serif; font-size: 11px; }

  table { border-collapse: collapse; width: 100%; }
  th, td {
    border: 1px solid #000;
    padding: 4px;
    font-size: 10px;
    word-break: break-word;
  }
  th { background-color: #f2f2f2; }

  .header-table td { border: none; }
  .title { font-size: 16px; font-weight: bold; text-align: center; color: #0085A3; }
  .date { text-align: right; font-size: 12px; }
  .footer { margin-top: 20px; text-align: center; font-weight: bold; }
</style>
</head>

<body>
<div class="Section1">

<table class="header-table">
<tr>
  <td width="25%">
    <img src="${imgBase64}" width="120" height="26"/>
  </td>
  <td width="50%" class="title">
    ${this.selectedTitleForReport}
  </td>
  <td width="25%" class="date">
    Export Date: ${formattedDate}
  </td>
</tr>
</table>

<table>
<thead>
<tr>
${columns.map(c => `<th>${c.header}</th>`).join("")}
</tr>
</thead>
<tbody>
${this.onlineFailedUsers.map(user => `
<tr>
  <td>${user.user_name ?? ""}</td>
  <td>${user.ip_address ?? ""}</td>
  <td>${user.user_agent ?? ""}</td>
  <td>${user.login_time ?? ""}</td>
  <td>${user.status === 1 ? "Online" : "Failed"}</td>
</tr>
`).join("")}
</tbody>
</table>

<div class="footer">
  Generated from https://tms.awashbank.com/tms/
</div>

</div>
</body>
</html>
`;

            const blob = new Blob([htmlContent], { type: "application/msword" });
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
            this.messageService.add({
                severity: "error",
                summary: "Error",
                detail: "Error generating Word document",
                life: 3000,
            });
        }
    }


    async exportPDF() {
        if (!this.onlineFailedUsers || this.onlineFailedUsers.length === 0) {
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

            const d = new Date();
            const formattedDate = `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, "0")}-${d.getDate().toString().padStart(2, "0")}`;

            // Logo
            const imgProps = pdf.getImageProperties(imgBase64);
            const imgWidth = 120;
            const imgHeight = (imgProps.height * imgWidth) / imgProps.width;
            pdf.addImage(imgBase64, "PNG", 40, 20, imgWidth, imgHeight);

            // Title
            pdf.setFontSize(16);
            pdf.setTextColor("#0085A3");
            pdf.text(this.selectedTitleForReport, pageWidth / 2, 40, { align: "center" });

            // Date
            pdf.setFontSize(12);
            pdf.setTextColor(0);
            pdf.text(`Date: ${formattedDate}`, pageWidth - 40, 40, { align: "right" });

            autoTable(pdf, {
                startY: 70 + imgHeight,
                head: [[
                    "User Name",
                    "Ip Address",
                    "Browser",
                    "Login Time",
                    "Status",
                ]],
                body: this.onlineFailedUsers.map(user => [
                    user.user_name ?? "",
                    user.ip_address ?? "",
                    user.user_agent ?? "",
                    user.login_time ? new Date(user.login_time).toLocaleString("en-CA") : "",
                    user.status === 1 ? "Online" : "Failed",
                ]),
                theme: "grid",
                styles: { fontSize: 10 },
                columnStyles: {
                    0: { cellWidth: 120 },
                    1: { cellWidth: 100 },
                    2: { cellWidth: 200 },
                    3: { cellWidth: 100 },
                    4: { cellWidth: 80 },
                },
                didDrawPage: () => {
                    pdf.setFontSize(10);
                    pdf.setTextColor(100);
                    pdf.text("Generated from https://tms.awashbank.com/tms/", pageWidth / 2, pageHeight - 20, { align: "center" }
                    );
                }
            });

            pdf.save(`${this.selectedTitleForReport}_${formattedDate}.pdf`);

            this.messageService.add({
                severity: "success",
                summary: "Success",
                detail: "PDF report exported successfully!",
                life: 2000,
            });
        } catch (error) {
            this.messageService.add({
                severity: "error",
                summary: "Error",
                detail: "Error generating PDF document",
                life: 3000,
            });
        }
    }


}
