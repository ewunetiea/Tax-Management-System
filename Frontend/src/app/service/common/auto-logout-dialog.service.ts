// auto-logout-dialog.service.ts
import { Injectable } from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { AutoLogoutDialogComponent } from 'app/pages/common/auto-logout-dialog/auto-logout-dialog.component';

@Injectable({ providedIn: 'root' })
export class AutoLogoutDialogService {
  private ref?: DynamicDialogRef;

  constructor(private dialogService: DialogService) {}

  private currentRef?: DynamicDialogRef;

openCountdownDialog(ms: number): Promise<boolean> {
  return new Promise<boolean>((resolve) => {
    this.currentRef = this.dialogService.open(AutoLogoutDialogComponent, {
      data: { countdownMs: ms },
      width: '350px'
    });

    this.currentRef.onClose.subscribe((value) => {
      this.currentRef = undefined;
      resolve(value);
    });
  });
}

forceClose(): void {
  if (this.currentRef) {
    this.currentRef.close(false); 
    this.currentRef = undefined;
  }
}


  public closeDialog() {
    this.ref?.close();
    this.ref = undefined;
  }

}
