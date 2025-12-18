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
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { xssSqlValidator } from 'app/SQLi-XSS-Prevention/xssSqlValidator';
import { User } from 'app/models/admin/user';

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
  @Input() passedAnnouncement: any[] = [];

  announcement: Announcement = new Announcement();
  user: User = new User();
  isEdit: boolean = false;
  visible: boolean = false;
  submitting = false;
  today = new Date();
  form!: FormGroup;
  submitted = false;

  constructor(
    private announcementService: AnnouncementService,
    private messageService: MessageService,
    private storageService: StorageService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.isEdit = this.passedAnnouncement[1];
    this.user = this.storageService.getUser();
    this.form = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(200), xssSqlValidator]],
      message: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(4000), xssSqlValidator]],
      audience: ['', [Validators.required]],
      created_date: [{ value: this.today, disabled: true }, Validators.required],
      expiry_date: ['', Validators.required],
      isFileEdited: [false]
    });
    // Keep announcement.isFileEdited in sync with the reactive form control
    this.form.get('isFileEdited')?.valueChanges.subscribe((v: boolean) => {
      this.announcement.isFileEdited = !!v;
    });
    if (this.isEdit) {
      this.editAnnouncement(this.passedAnnouncement);
    } else {
      this.openNew();
    }
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  openNew() {
    this.announcement = new Announcement();
  }

  editAnnouncement(passedData: any[]) {
    this.announcement = passedData[0];
    this.announcement.created_date = new Date(this.announcement.created_date!);
    this.announcement.expiry_date = new Date(this.announcement.expiry_date!);

    // Patch form with announcement values
    this.form.patchValue({
      title: this.announcement.title,
      message: this.announcement.message,
      audience: this.announcement.audience,
      created_date: this.announcement.created_date,
      expiry_date: this.announcement.expiry_date,
      isFileEdited: !!this.announcement.isFileEdited
    });
  }


  onSave() {
    this.submitted = true;
    if (this.form.invalid) return;

    // Update announcement from form
    Object.assign(this.announcement, this.form.value);
    this.announcement.created_date = this.today;
    this.announcement.posted_by = this.user.id;

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
        this.submitting = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error occurred while saving announcement',
          detail: '',
          life: 3000
        });
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
