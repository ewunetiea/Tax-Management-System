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
import { TaxableSearchEngineComponent } from '../taxable-search-engine/taxable-search-engine.component';
import { imgBase64 } from '../../../../helpers/logo';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';
import jsPDF from 'jspdf';

interface Column {
  field: string;
  header: string;
  customExportHeader?: string;
}

interface ExportColumn {
  title: string;
  dataKey: string;
}

@Component({
  selector: 'app-report',
  providers: [MessageService, ConfirmationService],
  imports: [SharedUiModule, TaxableSearchEngineComponent],
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
  breadcrumbText: string = 'Manage Taxes';
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
  cols!: Column[];
  selectedTitleForReport: string = "TMS - Report";

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

   onDataGenerated(data: Tax[]) {
    this.taxes = data;
    this.fetching = true;
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
                  Date: ${formattedDate}
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
              TMS - Report Generated from https://tms.awashbank.com/tms/
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
        life: 3000,
      });
    } catch (error) {
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Error generating Word document: " + error,
        life: 4000,
      });
    }
  } else {
    this.messageService.add({
      severity: "warn",
      summary: "No Data",
      detail: "There is no data to export!",
      life: 3000,
    });
  }
}

async exportExcel() {
  if (this.taxes && this.taxes.length > 0) {
    try {
      // Define columns exactly as in your Word export
      const excel_allowed_columns = [
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

      // Prepare data
      const excelData = this.taxes.map((tax) => {
        const row: Record<string, any> = {};
        excel_allowed_columns.forEach(col => {
          let value = (tax as Record<string, any>)[col.field];

          if (value == null || value === "") {
            row[col.header] = "";
          } else if (typeof value === "number") {
            row[col.header] = value;
          } else if (col.field.toLowerCase().includes("date")) {
            row[col.header] = new Date(value).toLocaleDateString("en-CA");
          } else {
            row[col.header] = value;
          }
        });
        return row;
      });

      // Create worksheet from JSON **without origin**
      const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(excelData);

      // Add a header title row and date at the top using sheet_add_aoa
      const d = new Date();
      const formattedDate = `${d.getFullYear()}-${(d.getMonth() + 1)
        .toString()
        .padStart(2, "0")}-${d.getDate().toString().padStart(2, "0")}`;

      // Prepend title and date
      XLSX.utils.sheet_add_aoa(worksheet, [
        [this.selectedTitleForReport],
        [`Date: ${formattedDate}`],
        [] // empty row between header and table
      ], { origin: 0 }); // origin 0 = top-left

      // Auto width for columns
      const colWidths = excel_allowed_columns.map(col => ({ wch: col.header.length + 5 }));
      worksheet["!cols"] = colWidths;

      // Create workbook
      const workbook: XLSX.WorkBook = {
        Sheets: { "TMS Report": worksheet },
        SheetNames: ["TMS Report"],
      };

      // Export Excel file
      const excelBuffer: any = XLSX.write(workbook, { bookType: "xlsx", type: "array" });
      const blob = new Blob([excelBuffer], { type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" });
      saveAs(blob, `${this.selectedTitleForReport}_${formattedDate}.xlsx`);

      this.messageService.add({
        severity: "success",
        summary: "Success",
        detail: "Excel report exported successfully!",
        life: 3000,
      });

    } catch (error) {
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Error generating Excel document: " + error,
        life: 4000,
      });
    }
  } else {
    this.messageService.add({
      severity: "warn",
      summary: "No Data",
      detail: "There is no data to export!",
      life: 3000,
    });
  }
}

async exportPDF() {
  if (this.taxes && this.taxes.length > 0) {
    try {
      const pdf = new jsPDF('l', 'pt', 'a4'); // landscape A4
      const pageWidth = pdf.internal.pageSize.getWidth();

      // Header: Title + Date
      const d = new Date();
      const formattedDate = `${d.getFullYear()}-${(d.getMonth() + 1)
        .toString()
        .padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;

      pdf.setFontSize(16);
      pdf.setTextColor('#0085A3');
      pdf.text(this.selectedTitleForReport, pageWidth / 2, 30, { align: 'center' });

      pdf.setFontSize(12);
      pdf.setTextColor(0, 0, 0);
      pdf.text(`Date: ${formattedDate}`, pageWidth - 40, 50, { align: 'right' });

      // Define columns for PDF
      const pdfColumns = [
        "Unit",
        "Director",
        "Reference Number",
        "Tax Category",
        "Number of Employee",
        "Taxable Amount",
        "Tax With Hold",
        "Graduate Tax Pool",
        "Graduate Base Salary",
        "Graduate Total Employee",
        "Graduate Tax With Hold",
        "Maker Name",
        "Maker Date",
        "Checked By",
        "Checked Date",
        "Updated By",
        "Updated Date",
      ];

      // Prepare rows
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

      // Generate table
      (pdf as any).autoTable({
        startY: 70,
        head: [pdfColumns],
        body: pdfRows,
        styles: { fontSize: 10 },
        headStyles: { fillColor: [242, 242, 242], textColor: 0 },
        columnStyles: { 5: { halign: 'right' }, 6: { halign: 'right' }, 7: { halign: 'right' }, 8: { halign: 'right' }, 9: { halign: 'right' }, 10: { halign: 'right' } }
      });

      pdf.save(`${this.selectedTitleForReport}_${formattedDate}.pdf`);
      this.messageService.add({
        severity: "success",
        summary: "Success",
        detail: "PDF report exported successfully!",
        life: 3000,
      });

    } catch (error) {
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Error generating PDF document: " + error,
        life: 4000,
      });
    }
  } else {
    this.messageService.add({
      severity: "warn",
      summary: "No Data",
      detail: "There is no data to export!",
      life: 3000,
    });
  }
}


}
