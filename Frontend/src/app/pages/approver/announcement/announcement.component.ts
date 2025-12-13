import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { Table, TableRowCollapseEvent, TableRowExpandEvent } from 'primeng/table';
import { HttpErrorResponse } from '@angular/common/http';
import { ProductService } from '../../../service/product.service';
import { Announcement } from '../../../models/approver/announcement';
import { AnnouncementService } from '../../../service/approver/announcement.service';
import { SharedUiModule } from '../../../../shared-ui';
import { AnnouncementCreateEditComponent } from '../announcement-create-edit/announcement-create-edit.component';
import { DataViewModule } from 'primeng/dataview';
import { SplitButtonModule } from 'primeng/splitbutton';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { EditorModule } from 'primeng/editor';
import { StorageService } from 'app/service/sharedService/storage.service';
import { AnnouncementPayload } from 'app/models/approver/announcementPayload';
import { FileDownloadService } from 'app/service/maker/file-download-service';

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
  selector: 'app-announcement',
  standalone: true,
  templateUrl: './announcement.component.html',
  styleUrl: './announcement.component.scss',
  providers: [MessageService, ProductService, ConfirmationService],
  imports: [SharedUiModule, AnnouncementCreateEditComponent, DataViewModule, SplitButtonModule, EditorModule]
})
export class AnnouncementComponent implements OnInit {
  expandedRows: { [key: number]: boolean } = {};
  selectedPdf: SafeResourceUrl | null = null;
  onlyBody = {
    toolbar: false  // <--- removes toolbar completely
  };
  showPdfModal = false;
  announcemetDialog: boolean = false;
  announcements: Announcement[] = [];
  announcement!: Announcement;
  selecteAnnouncements!: Announcement[] | null;
  submitted: boolean = false;
  statuses!: any[];
  @ViewChild('dt') dt!: Table;
  exportColumns!: ExportColumn[];
  cols!: Column[];
  uploadedFiles: any[] = [];
  announcement_type: String = ''
  isEdit = false;
  activeIndex1: number = 0;
  activeState: boolean[] = [true, false, false, false, false];
  pdfSrc: any;
  sizes!: any[];
  selectedSize: any = 'normal';
  breadcrumbText: string = ' Anouncement';
  items: MenuItem[] | undefined;
  rowToggles: { [id: number]: { message: boolean; file: boolean } } = {};
  status!: string;
  announcmentPayload = new AnnouncementPayload();
  isDialogVisible = false;
  outputAnnouncement: any[] = [];

  constructor(
    private announcemetService: AnnouncementService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private sanitizer: DomSanitizer,
    private route: ActivatedRoute,
    private storageService: StorageService,
    private fileDownloadService: FileDownloadService,
  ) { }

  ngOnInit(): void {
    this.announcement_type = this.route.snapshot.data['status'];
    this.items = [{ label: this.breadcrumbText }];
    this.sizes = [
      { name: 'Small', value: 'small' },
      { name: 'Normal', value: 'normal' },
      { name: 'Large', value: 'large' }
    ];

    this.announcmentPayload.announcement_type = this.announcement_type
    this.announcmentPayload.role = this.storageService.getUser().roles[0];
    this.loadAnnouncements(this.announcmentPayload);
  }

loadAnnouncements(announcmentPayload: AnnouncementPayload) {
  this.announcemetService.fetchAnnouncemets(announcmentPayload).subscribe(
    (response) => {
      this.announcements = (response as any).map((announcement: any) => {

        // Detect file type from base64
        const fileType = this.getFileType(announcement.image);
        announcement.fileType = fileType;

        // Prepare PDF blob URL if PDF
        if (fileType === 'application/pdf') {
          const byteCharacters = atob(announcement.image);
          const byteNumbers = new Array(byteCharacters.length);

          for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
          }

          const byteArray = new Uint8Array(byteNumbers);
          const blob = new Blob([byteArray], { type: 'application/pdf' });
          const url = URL.createObjectURL(blob);

          announcement.pdfUrl = url;
          announcement.safePdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url) as SafeResourceUrl;
        }

        return announcement;
      });
    },

    (error: HttpErrorResponse) => {
      this.messageService.add({
        severity: 'error',
        summary:
          error.status === 401
            ? 'You are not permitted to perform this action!'
            : 'Something went wrong while fetching announcements!',
        detail: '',
      });
    }
  );
}


noToolbar = {
  toolbar: false
};
  closeModal() {
    this.showPdfModal = false;
    this.selectedPdf = null;
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.outputAnnouncement = [];
    this.announcement = { created_date: new Date() } as Announcement;
    this.submitted = false;
    this.isEdit = false;
    this.outputAnnouncement.push(this.announcement);
    this.outputAnnouncement.push(this.isEdit);
    this.announcemetDialog = true;
  }

  editAnnouncement(announcement: Announcement) {
    this.outputAnnouncement = [];
    this.announcement = { ...announcement };
    this.announcement.previouseAnnouncementFile = announcement.announcementFile
    this.isEdit = true;
    this.outputAnnouncement.push(this.announcement);
    this.outputAnnouncement.push(this.isEdit);
    this.announcemetDialog = true;
  }
 
  onDataChange(data: any) {
    if (data[1]) {
      this.announcements[this.findIndexById(data[0].id)] = data[0];
    } else {
      this.loadAnnouncements(this.announcmentPayload);
      this.announcements = [...this.announcements];
      this.announcement = new Announcement();
    }
    this.announcemetDialog = false;
  }
  
  findIndexById(id: number): number {
    let index = -1;
    for (let i = 0; i < this.announcements.length; i++) {
      if (this.announcements[i].id === id) {
        index = i;
        break;
      }
    }
    return index;
  }

  
  deleteAnnouncements(announcements: Announcement | Announcement[] | null) {
    const itemsToDelete = Array.isArray(announcements) ? announcements : [announcements];
    this.confirmationService.confirm({
      message: `Are you sure you want to delete ${itemsToDelete.length} announcement(s)?`,
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.announcemetService.deleteSelectedAnnouncemets(itemsToDelete as any).subscribe({
          next: () => {
            // Remove deleted items from local list
            this.announcements = this.announcements.filter(
              val => !itemsToDelete.includes(val)
            );
            // Clear selection if it was a bulk delete
            if (Array.isArray(announcements)) {
              this.selecteAnnouncements = null;
            }
            this.messageService.add({
              severity: 'success',
              summary: 'Successful',
              detail: 'Announcements Deleted',
              life: 3000
            });
          },
          error: (err) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to delete announcements',
              life: 3000
            });
            console.error(err);
          }
        });
      }
    });
  }


  hideDialog() {
    this.announcemetDialog = false;
    this.submitted = false;
  }


  getSeverity(status: string) {
    switch (status) {
      case 'INSTOCK': return 'success';
      case 'LOWSTOCK': return 'warn';
      case 'OUTOFSTOCK': return 'danger';
      default: return 'info';
    }
  }


  onAnnouncementSaved(savedAnnouncement: Announcement) {
    if (this.isEdit) {
      const index = this.announcements.findIndex(a => a.id === savedAnnouncement.id);
      if (index !== -1) {
        this.announcements[index] = savedAnnouncement;
      }
    } else {

      // Detect file type from base64
      const fileType = this.getFileType(savedAnnouncement?.image as any);
      savedAnnouncement.fileType = fileType;

      // Prepare PDF blob URL if PDF
      if (fileType === 'application/pdf') {
        const byteCharacters = atob(savedAnnouncement.image as any);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
          byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        const blob = new Blob([byteArray], { type: 'application/pdf' });

        const url = URL.createObjectURL(blob);
        savedAnnouncement.pdfUrl = url;
        savedAnnouncement.safePdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url) as SafeResourceUrl;
      }
      // Add new announcement
      this.announcements = [savedAnnouncement, ...this.announcements];
    }

    this.announcemetDialog = false;
    this.announcement = {} as Announcement;
  }


  getFileType(base64: string): string {
    if (!base64) return '';
    // Common base64 prefixes
    if (base64.startsWith('/9j/')) return 'image/jpeg'; // JPEG
    if (base64.startsWith('iVBOR')) return 'image/png'; // PNG
    if (base64.startsWith('JVBER')) return 'application/pdf'; // PDF
    if (base64.startsWith('UEsDB')) return 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'; // DOCX
    if (base64.startsWith('0M8R4')) return 'application/msword'; // DOC
    return 'unknown';
  }



  toggleRow(announcement: Announcement, type: 'message' | 'file') {
    if (!this.rowToggles[announcement.id!]) {
      this.rowToggles[announcement.id!] = { message: false, file: false };
    }

    // Accordion-style: toggle selected type, close the other
    if (type === 'message') {
      this.rowToggles[announcement.id!].message = !this.rowToggles[announcement.id!].message;
      this.rowToggles[announcement.id!].file = false;
    } else {
      this.rowToggles[announcement.id!].file = !this.rowToggles[announcement.id!].file;
      this.rowToggles[announcement.id!].message = false;
    }
  }

  toggle(index: number) {
    this.activeState = this.activeState.map((_, i) => i === index ? !this.activeState[i] : false);
  }

  clear(table: Table) {
    table.clear();
  }

  onImageError(event: any) {
        console.error('Image failed to load', event);
    }

    openDialogImage() {
        this.isDialogVisible = true;
    }

    previewPdf(file: any) {
        if (file.pdfUrl) {
            this.selectedPdf = file.pdfUrl;
            this.showPdfModal = true;
        }
    }

    downloadPdf(file: any) {
        if (!file.pdfUrl && !file.fileType) return;

        // If we have the blob stored (recommended)
        this.fileDownloadService.fetcAnnouncementhFileByFileName(file.fileName).subscribe((blob: Blob) => {
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

     closeModalPDF() {
        this.showPdfModal = false;
        this.selectedPdf = null;
    }

    downloadWord(file: any) {
        try {
            let byteArray: Uint8Array;

            if (typeof file.file === 'string') {
                // Remove data URL prefix if it exists
                const base64 = file.file.includes(',') ? file.file.split(',')[1] : file.file;

                // Decode base64 safely
                const binary = atob(base64.replace(/\s/g, ''));
                byteArray = new Uint8Array(binary.length);

                for (let i = 0; i < binary.length; i++) {
                    byteArray[i] = binary.charCodeAt(i);
                }

                // Debug: Log the decoded binary and byte array


            } else {
                // If it's already a Blob, use it directly
                const blob = new Blob([file.file], { type: file.fileType });
                const link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = file.fileName;
                link.click();
                window.URL.revokeObjectURL(link.href);
                return; // Exit the function
            }

            const blob = new Blob([byteArray as any], { type: file.fileType });
            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = file.fileName;
            link.click();
            window.URL.revokeObjectURL(link.href);
        } catch (e) {
            console.error('Failed to download Word file', e);
            alert('Cannot download file. The data may be corrupted.');
        }
    }

    onRowExpand(event: any) {
    const announcement = event.data;
    if (!announcement.announcementFile || announcement.announcementFile.length === 0) {
      return;
    }

    const fileFetchPromises = announcement.announcementFile.map((file: any) => {
      if (!file?.fileName) return Promise.resolve(null);

      return this.fileDownloadService.fetcAnnouncementhFileByFileName(file.fileName).toPromise()
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
    announcement.announcementFile = results.filter(file => file !== null);

      // Force change detection for PDFs
      setTimeout(() => {

      }, 0);
    });
  }

   onRowCollapse(event: any) {
    const announcement = event.data;
    delete this.expandedRows[announcement.Id];
  }


}
