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
  imports: [SharedUiModule, AnnouncementCreateEditComponent, DataViewModule, SplitButtonModule]
})
export class AnnouncementComponent implements OnInit {
  expandedRows = {};
  selectedPdf: SafeResourceUrl | null = null; 
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
  activeState: boolean[] = [true, false, false];
  pdfSrc: any;
  sizes!: any[];
  selectedSize: any = 'normal';
  breadcrumbText: string = 'Manage Anouncement';
  items: MenuItem[] | undefined;
  rowToggles: { [id: number]: { message: boolean; file: boolean } } = {};

  status!: string;

  constructor(
    private announcemetService: AnnouncementService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private sanitizer: DomSanitizer,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.announcement_type = this.route.snapshot.data['status'];
    this.items = [{ label: this.breadcrumbText }];
    this.sizes = [
      { name: 'Small', value: 'small' },
      { name: 'Normal', value: 'normal' },
      { name: 'Large', value: 'large' }
    ];

    this.loadAnnouncements(this.announcement_type);
  }


  loadAnnouncements(announcement_type: String) {
    this.announcemetService.fetchAnnouncemets(announcement_type).subscribe(
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

  previewPdf(announcement: any) {
    this.selectedPdf = announcement.safePdfUrl;
    this.showPdfModal = true;
  }

  downloadPdf(announcement: any) {
    const a = document.createElement('a');
    a.href = announcement.pdfUrl;
    a.download = 'document.pdf';
    a.click();
  }

  closeModal() {
    this.showPdfModal = false;
    this.selectedPdf = null;
  }





  onRowExpand(event: TableRowExpandEvent) {
    this.messageService.add({ severity: 'info', summary: 'User Information Expanded', detail: event.data.name, life: 3000 });
  }

  onRowCollapse(event: TableRowCollapseEvent) {
    this.messageService.add({ severity: 'success', summary: 'User information Collapsed', detail: event.data.name, life: 3000 });
  }




  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  openNew() {
    this.announcement = { created_date: new Date() } as Announcement; // default date avoids null crash

    this.submitted = false;
    this.announcemetDialog = true;
    this.isEdit = false
  }

  editAnnouncement(announcement: Announcement) {
    this.announcement = { ...announcement };
    this.announcemetDialog = true;

    this.isEdit = true;
  }

  deleteAnnouncements(announcements: Announcement | Announcement[] | null) {
    // Normalize to array
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



  findIndexById(id: any): number {
    return this.announcements.findIndex((p) => p.id === id);
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
    // Initialize row state if it doesn't exist
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


}
