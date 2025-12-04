import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Announcement } from '../../../models/approver/announcement';
import { AnnouncementService } from '../../../service/approver/announcement.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedUiModule } from '../../../../shared-ui';
import { StorageService } from '../../../service/sharedService/storage.service';
import { FileUpload } from 'primeng/fileupload';
import { EditorModule } from 'primeng/editor';


@Component({
  standalone: true,
  selector: 'app-announcement-create-edit',
  imports:[SharedUiModule,  FileUpload, EditorModule],
  templateUrl: './announcement-create-edit.component.html',
  styleUrl: './announcement-create-edit.component.scss'
})
export class AnnouncementCreateEditComponent {
  @Input() visible!: boolean;
  @Output() visibleChange = new EventEmitter<boolean>(); // <-- required for two-way binding

  @Input() announcement!: Announcement;
  @Output() saved = new EventEmitter<Announcement>(); // emit to parent
  @Output() cancel = new EventEmitter<void>();

  minExpiryDate: Date = new Date();


  submitting = false;

  constructor(
    private announcementService: AnnouncementService,
    private messageService: MessageService, 
    private storageService: StorageService
  ) {}


  onSave() {
     this.announcement.posted_by = this.storageService.getUser().id
    this.submitting = true;
    this.announcementService.createAnnouncemet(this.announcement).subscribe({
      next: (response) => {
        this.saved.emit(response); // emit created announcement
        // close dialog and notify parent
        this.visible = false;
        this.visibleChange.emit(this.visible);
        this.submitting = false;
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Announcement saved' });
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


    onFileSelect(event: any): void {
        const file: File = event.files[0];

        const reader = new FileReader();
        reader.onload = () => {
            const arrayBuffer = reader.result as ArrayBuffer;
            const byteArray = new Uint8Array(arrayBuffer);
            this.announcement.image = Array.from(byteArray) as any; // 👈 Convert to number[]

            this.messageService.add({
                severity: 'success',
                summary: 'Upload Successful',
                detail: 'Image uploaded as byte array!'
            });


        };
        reader.readAsArrayBuffer(file);
    }

}
