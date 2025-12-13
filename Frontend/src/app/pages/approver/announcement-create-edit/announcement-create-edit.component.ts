import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Announcement } from '../../../models/approver/announcement';
import { AnnouncementService } from '../../../service/approver/announcement.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedUiModule } from '../../../../shared-ui';
import { StorageService } from '../../../service/sharedService/storage.service';
import { FileUpload } from 'primeng/fileupload';
import { EditorModule } from 'primeng/editor';
import { AnnouncementFile } from 'app/models/approver/announcementFile';

@Component({
  standalone: true,
  selector: 'app-announcement-create-edit',
  imports: [SharedUiModule, FileUpload, EditorModule],
  templateUrl: './announcement-create-edit.component.html',
  styleUrl: './announcement-create-edit.component.scss'
})
export class AnnouncementCreateEditComponent {
  @Output() visibleChange = new EventEmitter<boolean>();
  @Output() saved = new EventEmitter<Announcement>();
  @Output() cancel = new EventEmitter<void>();
  isEdit: boolean = false;
  announcement: Announcement = new Announcement();
  visible: boolean = false;
  minExpiryDate: Date = new Date();
  submitting = false;

  @Input() passedAnnouncement: any[] = [];
  @Output() editedAnnouncement: EventEmitter<any> = new EventEmitter();
today = new Date();

  constructor(
    private announcementService: AnnouncementService,
    private messageService: MessageService,
    private storageService: StorageService
  ) { }

  ngOnInit(): void {
    

    this.isEdit = this.passedAnnouncement[1];
    if (this.isEdit) {
      this.editAnnouncement(this.passedAnnouncement);
    } else {
      this.openNew();
    }
  }

  openNew() {
    this.announcement = new Announcement();
  }

  editAnnouncement(passedData: any[]) {
    this.announcement = passedData[0];
  }

  emitData(data: any[]) {
        this.editedAnnouncement.emit(data);
    }

  onSave() {
    this.announcement.created_date = this.today


    console.log(this.announcement.created_date, "expiry date " , this.announcement.expiry_date)
    this.announcement.posted_by = this.storageService.getUser().id

    if (this.announcement.isFileEdited) {
      if (!this.announcement.announcementFile || this.announcement.announcementFile.length === 0) {
        this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'File is not available to update' });
        return;
      }
    }

    const formData = new FormData();
    formData.append('announcement', new Blob([JSON.stringify(this.announcement)], { type: 'application/json' }));

    if (this.announcement.announcementFile && this.announcement.announcementFile.length > 0) {
      this.announcement.announcementFile.forEach((anoncmentFile: AnnouncementFile) => {
        if (anoncmentFile.file && anoncmentFile.fileName) {
          formData.append('files', anoncmentFile.file, anoncmentFile.fileName.toString());
        }
      });
    }

    this.submitting = true;
    this.announcementService.createAnnouncemet(formData).subscribe({
      next: (response) => {
        const isNew = !this.announcement.id;
        this.saved.emit(response);
        this.visible = false;
        this.visibleChange.emit(this.visible);
        this.submitting = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: isNew ? 'Announcement saved' : 'Announcement updated'
        });
      },
      error: (err: HttpErrorResponse) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message });
        this.submitting = false;
      }
    });
  }

  onCancel() {
    this.visible = false;
    this.visibleChange.emit(this.visible);
    this.cancel.emit();
  }


  onFileSelect(event: any) {
    const files: File[] = Array.from(event.files);
    if (!this.announcement!.announcementFile) {
      this.announcement!.announcementFile = [];
    }

    files.forEach(file => {
      const announcementFile: AnnouncementFile = {
        file: file,
        fileName: file.name,
        extension: '.' + file.name.split('.').pop(),
        fileType: file.type
      };
      this.announcement!.announcementFile!.push(announcementFile);
    });

    this.messageService.add({
      severity: 'success',
      summary: 'Upload Successful',
      detail: `${files.length} file(s) added successfully!`
    });
  }

  onFileClear() {
    this.announcement.announcementFile = [];
    this.messageService.add({
      severity: 'info',
      summary: 'Files Cleared',
      detail: 'All selected files have been removed.'
    });
  }

  onFileRemove(event: any) {
    const removedFile = event.file;
    if (this.announcement!.announcementFile) {
      this.announcement.announcementFile = this.announcement.announcementFile.filter(f => f.file !== removedFile);
    }
    this.messageService.add({
      severity: 'info',
      summary: 'File Removed',
      detail: `${removedFile.name} has been removed.`
    });
  }


}
