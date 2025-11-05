import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { interval, Subscription } from 'rxjs';
import { SharedUiModule } from 'shared-ui';

@Component({
  selector: 'app-auto-logout-dialog',
  standalone: true,
  imports: [SharedUiModule],
  templateUrl: './auto-logout-dialog.component.html'
})
export class AutoLogoutDialogComponent implements OnInit, OnDestroy {
  private ref = inject(DynamicDialogRef);
  private config = inject(DynamicDialogConfig);

  countdown: number = 0;
  private timerSub?: Subscription;

  ngOnInit(): void {
    const countdownMs = this.config.data?.countdownMs ?? 60000;
    this.countdown = Math.ceil(countdownMs / 1000);

    this.timerSub = interval(1000).subscribe(() => {
      this.countdown--;
      if (this.countdown <= 0) this.logout();
    });
  }

  stay(): void {
    this.timerSub?.unsubscribe();
    this.ref.close(true);
  }

  logout(): void {
    this.timerSub?.unsubscribe();
    this.ref.close(false);
  }

  ngOnDestroy(): void {
    this.timerSub?.unsubscribe();
  }
}
