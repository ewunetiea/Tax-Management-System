import { Component } from '@angular/core';
import { SafeResourceUrl, DomSanitizer } from '@angular/platform-browser';
import { MenuItem, ConfirmationService, MessageService } from 'primeng/api';
import { PaginatorPayLoad } from '../../../models/admin/paginator-payload';
import { User } from '../../../models/admin/user';
import { Tax } from '../../../models/maker/tax';
import { FileDownloadService } from '../../../service/maker/file-download-service';
import { StorageService } from '../../../service/sharedService/storage.service';
import { Table } from 'primeng/table';
import { SharedUiModule } from '../../../../shared-ui';
import { imgBase64 } from '../../../../helpers/logo';
import * as ExcelJS from 'exceljs';
import { saveAs } from 'file-saver';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { ReportSearchEngineComponent } from '../report-search-engine/report-search-engine.component';

@Component({
  selector: 'app-report',
  providers: [MessageService, ConfirmationService],
  imports: [SharedUiModule, ReportSearchEngineComponent],
  templateUrl: './report.component.html',
  styleUrl: './report.component.scss'
})

export class ReportComponent {
  paginatorPayLoad: PaginatorPayLoad = new PaginatorPayLoad();
  expandedRows: { [key: number]: boolean } = {};
  sizes!: any[];
  selectedSize: any = 'normal';
  items: MenuItem[] | undefined;
  home: MenuItem | undefined;
  breadcrumbText: string = 'Generate Report';
  user: User = new User();
  taxes: Tax[] = [];
  selectedTaxes: Tax[] = [];
  loading = true;
  rejectTaxDialog = false;
  outputRejectedTax: any[] = [];
  tax: Tax = new Tax();
  fetching = false;
  taxDialog = false
  isEdit = false;
  activeIndex1: number = 0;
  activeState: boolean[] = [true, false, false];
  pdfSrc: any;
  selectedPdf: SafeResourceUrl | null = null;
  showPdfModal = false;
  selectedTitleForReport: string = "Tax Management System - Report";

  constructor(
    private messageService: MessageService,
    private storageService: StorageService,
    private fileDownloadService: FileDownloadService,
    private sanitizer: DomSanitizer,
  ) { }

  ngOnInit(): void {
    this.user = this.storageService.getUser();
    this.paginatorPayLoad.branch_id = this.user.branch?.id;
    this.paginatorPayLoad.user_id = this.user.id;

    this.home = { icon: 'pi pi-home', routerLink: '/' };
    this.items = [{ label: this.breadcrumbText }];
    this.sizes = [
      { name: 'Small', value: 'small' },
      { name: 'Normal', value: 'normal' },
      { name: 'Large', value: 'large' }
    ];
  }

  /** Clear table filters */
  clear(table: Table): void {
    table.clear();
  }

  /** Global search filter */
  onGlobalFilter(table: Table, event: Event): void {
    const input = event.target as HTMLInputElement;
    table.filterGlobal(input.value, 'contains');
  }


  onDataGenerated(event: { data: Tax[]; fetching: boolean }): void {
    this.taxes = event.data;
    this.fetching = event.fetching;
    this.loading = false;
  }


  previewPdf(file: any) {
    if (file.pdfUrl) {
      this.selectedPdf = file.pdfUrl;
      this.showPdfModal = true;
    }
  }

  toggle(index: number) {
    this.activeState = this.activeState.map((_, i) => i === index ? !this.activeState[i] : false);
  }

  downloadPdf(file: any) {
    if (!file.pdfUrl && !file.fileType) return;

    // If we have the blob stored (recommended)
    this.fileDownloadService.fetchFileByFileName(file.fileName).subscribe((blob: Blob) => {
      const link = document.createElement('a');
      const blobUrl = URL.createObjectURL(blob); // create object URL from blob
      link.href = blobUrl;
      link.download = file.fileName || 'document.pdf';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(blobUrl);
    });
  }

  closeModal() {
    this.showPdfModal = false;
    this.selectedPdf = null;
  }

  onRowExpand(event: any) {
    const tax = event.data;

    if (!tax.taxFile || tax.taxFile.length === 0) {
      return;
    }

    const fileFetchPromises = tax.taxFile.map((file: any) => {
      if (!file?.fileName) return Promise.resolve(null);

      return this.fileDownloadService.fetchFileByFileName(file.fileName).toPromise()
        .then((blob: Blob | undefined) => {
          if (!blob) {
            console.warn(`No blob returned for file: ${file.fileName}`);
            return null;
          }

          const newFile = { ...file };
          newFile.fileType = blob.type;

          // PDF
          if (blob.type === 'application/pdf') {
            // Revoke previous URL if exists
            if (newFile.pdfUrl) {
              URL.revokeObjectURL(
                (newFile.pdfUrl as any).changingThisBreaksApplicationSecurity
              );
            }
            const blobUrl = URL.createObjectURL(blob);
            newFile.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(blobUrl);
            newFile.file = null;
            return newFile;
          }

          // Image
          if (blob.type.startsWith('image/')) {
            return new Promise((resolve) => {
              const reader = new FileReader();
              reader.onload = (e: any) => {
                newFile.file = e.target.result.split(',')[1]; // base64
                newFile.pdfUrl = null;
                resolve(newFile);
              };
              reader.readAsDataURL(blob);
            });
          }

          // Word or other files
          newFile.file = blob;
          newFile.pdfUrl = null;
          return newFile;
        })
        .catch((error) => {
          console.error('Error fetching file:', error);
          return null;
        });
    });

    Promise.all(fileFetchPromises).then((results) => {
      tax.taxFile = results.filter(file => file !== null);
      // Force change detection for PDFs
      setTimeout(() => {

      }, 0);
    });
  }

  onRowCollapse(event: any) {
    const tax = event.data;
    delete this.expandedRows[tax.Id];
  }

  async exportWord() {
    if (this.taxes && this.taxes.length > 0) {
      try {
        // ✅ Define columns exactly as in your table
        const word_allowed_columns = [
          { field: "initiator_branch", header: "Unit" },
          { field: "destination_branch", header: "Director" },
          { field: "reference_number", header: "Reference Number" },
          { field: "taxType", header: "Tax Category" },
          { field: "noOfEmployee", header: "Number of Employee" },
          { field: "taxableAmount", header: "Taxable Amount" },
          { field: "taxWithHold", header: "Tax With Hold" },
          { field: "graduatetaxPool", header: "Graduate Tax Pool" },
          { field: "graduaTotBasSalary", header: "Graduate Base Salary" },
          { field: "graduateTotaEmployee", header: "Graduate Total Employee" },
          { field: "graduatetaxWithHold", header: "Graduate Tax With Hold" },
          { field: "maker_name", header: "Maker Name" },
          { field: "maker_date", header: "Maker Date" },
          { field: "checker_name", header: "Checked By" },
          { field: "checked_Date", header: "Checked Date" },
          { field: "updated_user_name", header: "Updated By" },
          { field: "updated_event_date", header: "Updated Date" },
        ];

        const d = new Date();
        const formattedDate = `${d.getFullYear()}-${(d.getMonth() + 1)
          .toString()
          .padStart(2, "0")}-${d.getDate().toString().padStart(2, "0")}`;

        // ✅ Build HTML content
        const htmlContent = `
        <html xmlns:o='urn:schemas-microsoft-com:office:office'
              xmlns:w='urn:schemas-microsoft-com:office:word'
              xmlns='http://www.w3.org/TR/REC-html40'>
        <head>
          <meta charset='utf-8'>
          <title>${this.selectedTitleForReport} - ${formattedDate}</title>
          <style>
            @page Section1 {
              size: 841.95pt 595.35pt; /* A4 Landscape */
              mso-page-orientation: landscape;
              margin: 1in;
            }
            div.Section1 { page: Section1; }

            body {
              font-family: Calibri, sans-serif;
              font-size: 11px;
            }

            .header-table {
              width: 100%;
              margin-bottom: 20px;
              table-layout: fixed;
            }

            .header-table td {
              vertical-align: middle;
            }

            .header-title {
              font-size: 16px;
              font-weight: bold;
              color: #0085A3;
              text-align: center;
            }

            .header-date {
              text-align: right;
              font-size: 14px;
            }

            table.data-table {
              border-collapse: collapse;
              width: 100%;
              table-layout: fixed;
            }

            .data-table th, .data-table td {
              border: 1px solid black;
              padding: 4px;
              text-align: left;
              word-wrap: break-word !important;
              word-break: break-all !important;
              font-size: 10px;
            }

            .data-table th {
              background-color: #f2f2f2;
            }

            .footer-note {
              margin-top: 20px;
              text-align: center;
              font-size: 11px;
              font-weight: bold;
            }
          </style>
        </head>
        <body>
          <div class="Section1">
            <table class="header-table">
              <tr>
                <td style="width: 25%;">
                  <img src="${imgBase64}" width="120" height="26" alt="Awash Bank Logo" />
                </td>
                <td style="width: 50%;" class="header-title">
                  ${this.selectedTitleForReport}
                </td>
                <td style="width: 25%;" class="header-date">
                  Eport Date: ${formattedDate}
                </td>
              </tr>
            </table>

            <table class="data-table">
              <thead>
                <tr>
                  ${word_allowed_columns.map(col => `<th>${col.header}</th>`).join("")}
                </tr>
              </thead>
              <tbody>
                ${this.taxes
            .map(tax => `
                    <tr>
                      ${word_allowed_columns
                .map(col => {
                  let value = (tax as Record<string, any>)[col.field];
                  if (value == null || value === "") return "<td></td>";
                  // ✅ Formatting based on field type
                  if (typeof value === "number")
                    value = value.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
                  if (col.field.includes("date") || col.field.includes("Date"))
                    value = new Date(value).toLocaleDateString("en-CA");
                  return `<td>${value}</td>`;
                })
                .join("")}
                    </tr>
                  `)
            .join("")}
              </tbody>
            </table>

            <div class="footer-note">
              Generated from https://tms.awashbank.com/tms/
            </div>
          </div>
        </body>
        </html>
      `;

        // ✅ Create and trigger download
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
          detail: "Error generating Word document: " + error,
          life: 2000,
        });
      }
    } else {
      this.messageService.add({
        severity: "warn",
        summary: "No Data",
        detail: "There is no data to export!",
        life: 2000,
      });
    }
  }

  async exportPDF() {
  if (!this.taxes || this.taxes.length === 0) {
    this.messageService.add({
      severity: "warn",
      summary: "No Data",
      detail: "There is no data to export!",
      life: 2000,
    });
    return;
  }

  try {
    // 1️⃣ Create jsPDF instance in landscape A4
    const pdf = new jsPDF('l', 'pt', 'a4');
    const pageWidth = pdf.internal.pageSize.getWidth();
    const pageHeight = pdf.internal.pageSize.getHeight();

    // Current date
    const d = new Date();
    const formattedDate = `${d.getFullYear()}-${(d.getMonth() + 1)
      .toString()
      .padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;

    // 2️⃣ Add Logo
    const imgProps = pdf.getImageProperties(imgBase64);
    const imgWidth = 120;
    const imgHeight = (imgProps.height * imgWidth) / imgProps.width;
    pdf.addImage(imgBase64, 'PNG', 40, 20, imgWidth, imgHeight);

    // 3️⃣ Add Title
    pdf.setFontSize(16);
    pdf.setTextColor('#0085A3');
    pdf.text(this.selectedTitleForReport, pageWidth / 2, 40, { align: 'center' });

    // 4️⃣ Add Date
    pdf.setFontSize(12);
    pdf.setTextColor(0, 0, 0);
    pdf.text(`Date: ${formattedDate}`, pageWidth - 40, 40, { align: 'right' });

    // 5️⃣ Define columns
    const pdfColumns = [
      "Unit", "Director", "Reference Number", "Tax Category",
      "Number of Employee", "Taxable Amount", "Tax With Hold", "Graduate Tax Pool",
      "Graduate Base Salary", "Graduate Total Employee", "Graduate Tax With Hold",
      "Maker Name", "Maker Date", "Checked By", "Checked Date", "Updated By", "Updated Date",
    ];

    // 6️⃣ Prepare rows
    const pdfRows = this.taxes.map(tax => [
      tax.initiator_branch ?? '',
      tax.destination_branch ?? '',
      tax.reference_number ?? '',
      tax.taxType ?? '',
      tax.noOfEmployee ?? '',
      tax.taxableAmount ?? '',
      tax.taxWithHold ?? '',
      tax.graduatetaxPool ?? '',
      tax.graduaTotBasSalary ?? '',
      tax.graduateTotaEmployee ?? '',
      tax.graduatetaxWithHold ?? '',
      tax.maker_name ?? '',
      tax.maker_date ? new Date(tax.maker_date).toLocaleDateString('en-CA') : '',
      tax.checker_name ?? '',
      tax.checked_Date ? new Date(tax.checked_Date).toLocaleDateString('en-CA') : '',
      tax.updated_user_name ?? '',
      tax.updated_event_date ? new Date(tax.updated_event_date).toLocaleDateString('en-CA') : '',
    ]);

    // 7️⃣ Generate table using autoTable
    autoTable(pdf, {
      startY: 70 + imgHeight,
      head: [pdfColumns],
      body: pdfRows,
      styles: { fontSize: 10 },
      headStyles: { fillColor: [242, 242, 242], textColor: 0 },
      columnStyles: {
        5: { halign: 'right' },
        6: { halign: 'right' },
        7: { halign: 'right' },
        8: { halign: 'right' },
        9: { halign: 'right' },
        10: { halign: 'right' }
      },
      theme: 'grid',
      didDrawPage: (data) => {
        // Cast pdf to any to avoid TS errors
        const pdfAny = pdf as any;
        const pageCount = pdfAny.getNumberOfPages();
        const currentPage = pdfAny.internal.getCurrentPageInfo().pageNumber;

        // Footer only on last page
        if (currentPage === pageCount) {
          pdf.setFontSize(10);
          pdf.setTextColor(100);
          const footerText = "Generated from https://tms.awashbank.com/tms/";
          pdf.text(footerText, pageWidth / 2, pageHeight - 20, { align: 'center' });
        }
      }
    });

    // 8️⃣ Save PDF
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
      detail: "Error generating PDF document: " + error,
      life: 2000,
    });
  }
}


async exportExcel() {
  if (!this.taxes || this.taxes.length === 0) {
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
    const worksheet = workbook.addWorksheet("Tax Management Report");

    // ==========================
    // COLUMN DEFINITIONS 
    // ==========================
    worksheet.columns = [
      { header: "Unit",               key: "initiator_branch",      width: 18 },
      { header: "Director",           key: "destination_branch",    width: 18 },
      { header: "Reference Number",   key: "reference_number",      width: 20 },
      { header: "Tax Category",       key: "taxType",               width: 18 },
      { header: "Number of Employee", key: "noOfEmployee",          width: 20 },
      { header: "Taxable Amount",     key: "taxableAmount",         width: 18 },
      { header: "Tax With Hold",      key: "taxWithHold",           width: 18 },
      { header: "Graduate Tax Pool",  key: "graduatetaxPool",       width: 18 },
      { header: "Graduate Base Salary", key: "graduaTotBasSalary", width: 20 },
      { header: "Graduate Total Employee", key: "graduateTotaEmployee", width: 22 },
      { header: "Graduate Tax With Hold", key: "graduatetaxWithHold", width: 20 },
      { header: "Maker Name",         key: "maker_name",            width: 15 },
      { header: "Maker Date",         key: "maker_date",            width: 15 },
      { header: "Checked By",         key: "checker_name",          width: 15 },
      { header: "Checked Date",       key: "checked_Date",          width: 15 },
      { header: "Updated By",         key: "updated_user_name",     width: 15 },
      { header: "Updated Date",       key: "updated_event_date",    width: 15 },
    ];

    // ==========================
    // LOGO – A1:B1 
    // ==========================
    worksheet.mergeCells("A1:B1");
    const imageId = workbook.addImage({
      base64: imgBase64,
      extension: "png",
    });
    // place the image inside A1:B1 
    worksheet.addImage(imageId, {
       tl: { col: 0.2, row: 0.2 }, // position
       ext: { width: 120, height: 40 }, // logo size              
    });

    // ==========================
    // TITLE – C1:O1
    // ==========================
    worksheet.mergeCells("C1:N1");
    const titleCell = worksheet.getCell("C1");
    titleCell.value = "Tax Management System - Report";
    titleCell.font = { name: "Calibri", size: 20, bold: true };
    titleCell.alignment = { horizontal: "center", vertical: "middle" };

    // ==========================
    // DATE – O1:Q1
    // ==========================
    const today = new Date().toISOString().split("T")[0];
    worksheet.mergeCells("O1:Q1");
    const dateCell = worksheet.getCell("O1");
    dateCell.value = `Generated on: ${today}`;
    dateCell.font = { italic: true, size: 12, bold: true };
    dateCell.alignment = { horizontal: "right", vertical: "middle" };

    worksheet.addRow([]);   // row 2 stays empty

    // ==========================
    // TABLE HEADERS – row 3 (A3:Q3)
    // ==========================
    const headerRow = worksheet.addRow(worksheet.columns.map(col => col.header));
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
    // DATA ROWS – start at row 4
    // ==========================
    this.taxes.forEach(item => {
      const row = worksheet.addRow(item);
      row.eachCell((cell, colNumber) => {
        cell.alignment = {
           horizontal: "center", 
           vertical: "middle",
        };
      });
    });

    // ==========================
    // BORDERS FOR ALL ROWS (including headers & data)
    // ==========================
    worksheet.eachRow({ includeEmpty: false }, row => {
      row.eachCell(cell => {
        cell.border = {
          top: { style: "thin" },
          left: { style: "thin" },
          bottom: { style: "thin" },
          right: { style: "thin" },
        };
      });
    });

    // Freeze top 4 rows
    worksheet.views = [{ state: "frozen", ySplit: 4 }];
    
    // Footer Note
    worksheet.addRow([]);
    worksheet.addRow([]);
    const footerRow = worksheet.addRow(["Generated from https://tms.awashbank.com/"]);
    footerRow.getCell(1).font = { italic: true, size: 11, color: { argb: "FF666666" } };
    footerRow.getCell(1).alignment = { horizontal: "center", vertical: "middle" };
    worksheet.mergeCells(footerRow.number, 1, footerRow.number, worksheet.columnCount);

    // ==========================
    // EXPORT FILE
    // ==========================
    const buffer = await workbook.xlsx.writeBuffer();
    saveAs(new Blob([buffer]), `tms_report_${today}.xlsx`);

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


}
