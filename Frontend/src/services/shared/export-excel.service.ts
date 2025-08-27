import { Injectable } from '@angular/core';
import { imgBase64 } from 'app/helpers/logo';
import { Workbook } from 'exceljs';

interface ExportColumn {
  title: string;
  dataKey: string;
}

@Injectable({
  providedIn: 'root',
})
export class ExportExcelService {
  constructor() {}

  exportExcel(excelData: {
    sheet_name: any;
    title: any;
    data: any;
    headers: any;
  }) {
    //Title, Header & Data
    const title = excelData.title;
    const header = excelData.headers;
    const data = excelData.data;

    //Create a workbook with a worksheet
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet(excelData.sheet_name);

    //Add Row and formatting
    worksheet.mergeCells('B3', 'L4');
    let titleRow = worksheet.getCell('B3');
    titleRow.value = title;
    titleRow.font = {
      name: 'Calibri',
      size: 16,
      // underline: 'single',
      bold: true,
      color: { argb: '0085A3' },
    };
    titleRow.alignment = { vertical: 'middle', horizontal: 'center' };

    // Date
    // worksheet.mergeCells('G1:H4');
    let d = new Date();
    let date = d.getDate() + '-' + d.getMonth() + '-' + d.getFullYear();
    // let dateCell = worksheet.getCell('G1');
    // dateCell.value = date;
    // dateCell.font = {
    //   name: 'Calibri',
    //   size: 12,
    //   bold: true,
    // };
    // dateCell.alignment = { vertical: 'middle', horizontal: 'center' };

    //Add Image
    let myLogoImage = workbook.addImage({
      base64: imgBase64,
      extension: 'png',
    });
    worksheet.mergeCells('C1:H2');
    worksheet.addImage(myLogoImage, 'C1:H2');

    //Blank Row
    worksheet.addRow([]);

    //Adding Header Row
    let headerRow = worksheet.addRow(header);
    headerRow.eachCell((cell, number) => {
      cell.fill = {
        type: 'pattern',
        pattern: 'solid',
        fgColor: { argb: '4167B8' },
        bgColor: { argb: '' },
      };
      cell.font = {
        bold: true,
        color: { argb: 'FFFFFF' },
        size: 12,
      };
    });

    // Adding Data with Conditional Formatting
    data.forEach((d: any) => {
      let row = worksheet.addRow(Object.values(d));
      //   let sales = row.getCell(6);
      //   let color = 'FF99FF99';
      //   let sales_val = sales.value || 0;
      //   // Conditional fill color
      //   if (sales_val < 200000) {
      //     color = 'FF9999';
      //   }

      //   sales.fill = {
      //     type: 'pattern',
      //     pattern: 'solid',
      //     fgColor: { argb: color },
      //   };
    });

    // worksheet.getColumn(3).width = 20;
    worksheet.properties.showGridLines = false;
    worksheet.addRow([]);

    // Footer Row
    let footerRow = worksheet.addRow([
      'Audit Finding Reporting and Followup Management System - Report Generated from https:/afrfms.awashbank.com/afrfms/ at ' +
        date,
    ]);
    footerRow.getCell(1).fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: 'FFB050' },
    };

    // Merge Cells
    worksheet.mergeCells(`A${footerRow.number}:P${footerRow.number}`);

    // worksheet.protect('BigFcy', {
    //   formatCells: true,
    //   formatColumns: true,
    //   formatRows: true,
    //   insertRows: true,
    //   insertColumns: false,
    //   insertHyperlinks: true,
    //   deleteRows: true,
    //   deleteColumns: false,
    //   sort: true,
    //   autoFilter: true,
    //   selectLockedCells: false,
    //   selectUnlockedCells: false,
    // })
    workbook.company = 'Awash Bank';
    workbook.created = d;
    workbook.creator = 'Audit Finding Reporting and Followup Management System';
    workbook.addImage({
      base64: imgBase64,
      extension: 'png',
    });
    //Generate & Save Excel File
    workbook.xlsx.writeBuffer().then((data) => {
      // let blob = new Blob([data], {
      //   type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      // });

      const blob = new Blob([data], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      });
      let url = window.URL.createObjectURL(blob);
      let a = document.createElement('a');
      document.body.appendChild(a);
      a.setAttribute('style', 'display: none');
      a.href = url;
      a.download = `${title}.xlsx`;
      a.click();
      window.URL.revokeObjectURL(url);
      a.remove();
    });
  }

  exportPdf(
    exportColumns: any,
    reportResponse: any,
    fileName: string,
    title: string
  ) {
    reportResponse.forEach((d: any) => {
      for (const [key, value] of Object.entries(d)) {
        if ((value === null || value == '') && !key.startsWith('col_'))
          d[key] = '-';
      }
    });

    import('jspdf' as any).then((jsPDF) => {
      import('jspdf-autotable' as any).then((x) => {
        const doc = new jsPDF.default('l', 'px', 'a4');
        doc.setFontSize(11);
        doc.setFont('Calibri');

        let d = new Date();

        const options: Intl.DateTimeFormatOptions = {
          weekday: 'long', // Full day name
          year: 'numeric',
          month: 'long',
          day: 'numeric',
        };

        const fullDayFormat = d.toLocaleDateString('en-US', options);

        doc.autoTable({
          didDrawCell: (data: any) => {
            if (data.row.index === 0) {
              if (data.column.index === 0) {
                doc.addImage(
                  imgBase64,
                  'PNG',
                  data.cell.x + 2,
                  data.cell.y + 2,
                  120,
                  26
                );
              }
              data.row.height = 26;
            }
          },
          body: [['', title, 'Date: ' + fullDayFormat]],
          columnStyles: {
            0: {
              cellWidth: 120,
              fillColor: 'white',
              halign: 'center',
              valign: 'middle',
            },
            1: {
              cellWidth: 'auto',
              fillColor: 'white',
              halign: 'center',
              valign: 'middle',
              fontStyle: 'bold',
              fontSize: 14,
              font: 'Calibri',
              textColor: '0085A3',
            },
            2: {
              cellWidth: 120,
              fillColor: 'white',
              halign: 'center',
              valign: 'middle',
              fontSize: 11,
              font: 'Calibri',
              textColor: '000000',
            },
          },
          styles: {
            halign: 'center',
          },
        });

        doc.autoTable(exportColumns, reportResponse);

        doc.autoTable({
          body: [
            [
              '',
              'AFRFMS - Report Generated from https://afrfms.awashbank.com/afrfms/',
              '',
            ],
          ],
          columnStyles: {
            0: {
              cellWidth: 120,
              fillColor: 'white',
              halign: 'center',
              valign: 'middle',
            },
            1: {
              cellWidth: 'auto',
              fillColor: 'white',
              halign: 'center',
              valign: 'middle',
              fontStyle: 'bold',
              fontSize: 11,
              font: 'Calibri',
            },
            2: {
              cellWidth: 120,
              fillColor: 'white',
              halign: 'center',
              valign: 'middle',
              fontSize: 11,
              font: 'Calibri',
              textColor: '000000',
            },
          },
          styles: {
            halign: 'center',
          },
        });
        doc.save(fileName);
      });
    });
  }

  exportExcelReport(excelData: {
    sheet_name: any;
    title: any;
    data: any;
    headers: ExportColumn[];
  }) {
    //Title, Header & Data
    const title = excelData.title;
    const header_titles = [];
    for (let header of excelData.headers) header_titles.push(header.title);
    const header = header_titles;
    const data = excelData.data;

    // data.sort((a: any, b: any) => {
    //   // Create a tuple of keys for each object
    //   const aKeys = excelData.headers.dataKey.map((key: any) => a[key]);
    //   const bKeys = excelData.headers.dataKey.map((key: any) => b[key]);
    //   // Compare the tuples lexicographically
    //   return aKeys < bKeys ? -1 : aKeys > bKeys ? 1 : 0;
    // });

    // let result: any[];
    // const sorted: any = [];
    // data.array.forEach((d: any) => {
    //   for (let h of excelData.headers) {
    //     if (d.hasOwnProperty(h)) {
    //       sorted.push([h, d[h]]);
    //     }
    //   }
    //   result.push(Object.fromEntries(sorted));
    // });

    //Create a workbook with a worksheet
    let workbook = new Workbook();
    let worksheet = workbook.addWorksheet(excelData.sheet_name);
    // workbook['!col'] = [{ wch: 20 }];
    //Add Row and formatting
    // worksheet.mergeCells('B3', 'L4');
    worksheet.mergeCells(2, 3, 3, header_titles.length - 2);

    let d = new Date();
    const options: Intl.DateTimeFormatOptions = {
      weekday: 'long', // Full day name
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    };

    const fullDayFormat = d.toLocaleDateString('en-US', options);

    worksheet.mergeCells(2, header_titles.length - 1, 3, header_titles.length);
    const dateRow = worksheet.getCell(2, header_titles.length - 1);
    dateRow.value =
      // 'Date: ' + d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate();
      'Date: ' + fullDayFormat;

    dateRow.font = {
      name: 'Calibri',
      size: 11,
      // underline: 'single',
      // bold: true,
      color: { argb: '000000' },
    };
    dateRow.alignment = { vertical: 'middle', horizontal: 'center' };

    const titleRow = worksheet.getCell(2, 3);
    titleRow.value = title;
    titleRow.font = {
      name: 'Calibri',
      size: 16,
      // underline: 'single',
      bold: true,
      color: { argb: '0085A3' },
    };
    titleRow.alignment = { vertical: 'middle', horizontal: 'center' };

    // Logo
    worksheet.mergeCells('A2:B3');

    // let dateCell = worksheet.getCell('G1');
    // dateCell.value = date;
    // dateCell.font = {
    //   name: 'Calibri',
    //   size: 12,
    //   bold: true,
    // };
    // dateCell.alignment = { vertical: 'middle', horizontal: 'center' };

    //Add Image
    let myLogoImage = workbook.addImage({
      base64: imgBase64,
      extension: 'png',
    });
    // worksheet.mergeCells('C1:H2');
    worksheet.addImage(myLogoImage, 'A2:B3');

    //Blank Row
    worksheet.addRow([]);

    ////////////////////////////////////////////////////////
    //Adding Header Row
    // let headerRow = worksheet.addRow(header);
    // headerRow.eachCell((cell, number) => {
    //   cell.fill = {
    //     type: 'pattern',
    //     pattern: 'solid',
    //     fgColor: { argb: '4167B8' },
    //     bgColor: { argb: '' },
    //   };
    //   cell.font = {
    //     bold: true,
    //     color: { argb: 'FFFFFF' },
    //     size: 12,
    //   };
    // });
    //////////////////////////////////////////////////////////
    // headerRow.alignment = { wrapText: true };

    // Adding Data with Conditional Formatting
    ///////////////////////////////////////////////////////
    // data.forEach((d: any) => {
    //   let row = worksheet.addRow(Object.values(d));
    //   //   let sales = row.getCell(6);
    //   //   let color = 'FF99FF99';
    //   //   let sales_val = sales.value || 0;
    //   //   // Conditional fill color
    //   //   if (sales_val < 200000) {
    //   //     color = 'FF9999';
    //   //   }

    //   //   sales.fill = {
    //   //     type: 'pattern',
    //   //     pattern: 'solid',
    //   //     fgColor: { argb: color },
    //   //   };
    // });

    const headerRow = worksheet.addRow(
      excelData.headers.map((column: any) => column.title)
    );
    headerRow.eachCell((cell, number) => {
      cell.fill = {
        type: 'pattern',
        pattern: 'solid',
        fgColor: { argb: '4167B8' },
        bgColor: { argb: '' },
      };
      cell.font = {
        bold: true,
        color: { argb: 'FFFFFF' },
        size: 12,
      };
    });

    // Add the data rows to the worksheet
    excelData.data.forEach((dataRow: any) => {
      const row = worksheet.addRow(
        excelData.headers.map((column: any) => dataRow[column.dataKey])
      );
    });
    ////////////////////////////////////////////////////////////

    // worksheet.getColumn(3).width = 20;
    // worksheet.properties.showGridLines = false;
    // worksheet.addRow([]);

    // Footer Row
    worksheet.mergeCells(
      data.length + 7,
      3,
      data.length + 8,
      header_titles.length - 2
    );
    // let footerRow = worksheet.addRow([
    //   'Big Foreign Currency Non Govermental Organizations Employees Loan Followup Management System - Report Generated from https://lms.awashbank.com/bigfcy/ at ',
    // ]);
    // footerRow.getCell(1).fill = {
    //   type: 'pattern',
    //   pattern: 'solid',
    //   fgColor: { argb: 'FFB050' },
    // };

    const footerRow = worksheet.getCell(data.length + 7, 3);
    footerRow.value =
      'AFRFMS - Report Generated from https://afrfms.awashbank.com/afrfms/';
    footerRow.font = {
      name: 'Calibri',
      size: 11,
      // // underline: 'single',
      bold: true,
      // color: { argb: '0085A3' },
    };
    footerRow.alignment = { vertical: 'middle', horizontal: 'center' };

    // Merge Cells
    // worksheet.mergeCells(`A${footerRow.number}:P${footerRow.number}`);

    // worksheet.protect('BigFcy', {
    //   formatCells: true,
    //   formatColumns: true,
    //   formatRows: true,
    //   insertRows: true,
    //   insertColumns: false,
    //   insertHyperlinks: true,
    //   deleteRows: true,
    //   deleteColumns: false,
    //   sort: true,
    //   autoFilter: true,
    //   selectLockedCells: false,
    //   selectUnlockedCells: false,
    // })
    workbook.company = 'Awash Bank';
    workbook.created = d;
    workbook.creator = 'Audit Finding Reporting and Followup Management System';
    workbook.addImage({
      base64: imgBase64,
      extension: 'png',
    });
    worksheet.columns.forEach((column) => {
      column.width = 13;
    });
    //Generate & Save Excel File
    workbook.xlsx.writeBuffer().then((data) => {
      // let blob = new Blob([data], {
      //   type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      // });

      const blob = new Blob([data], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      });
      let url = window.URL.createObjectURL(blob);
      let a = document.createElement('a');
      document.body.appendChild(a);
      a.setAttribute('style', 'display: none');
      a.href = url;
      a.download = `${title}.xlsx`;
      a.click();
      window.URL.revokeObjectURL(url);
      a.remove();
    });
  }
}
