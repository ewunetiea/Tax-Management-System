// auto-logout-dialog.service.ts
import { Injectable } from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { AutoLogoutDialogComponent } from 'app/pages/common/auto-logout-dialog/auto-logout-dialog.component';

@Injectable({ providedIn: 'root' })
export class AutoLogoutDialogService {
  private ref?: DynamicDialogRef;

  constructor(private dialogService: DialogService) {}

  public openCountdownDialog(countdownMs: number): Promise<boolean> {
    return new Promise<boolean>(resolve => {
      this.ref = this.dialogService.open(AutoLogoutDialogComponent, {
        header: 'Session Timeout In',
        width: '400px',
        closable: false,
        data: { countdownMs },
      });

      this.ref.onClose.subscribe((result: boolean) => {
        resolve(Boolean(result));
      });
    });
  }

  public closeDialog() {
    this.ref?.close();
    this.ref = undefined;
  }
}
