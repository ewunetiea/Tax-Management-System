// auto-logout.service.ts
import { Injectable, NgZone, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, fromEvent, merge } from 'rxjs';
import { takeUntil, throttleTime } from 'rxjs/operators';
import { StorageService } from './storage.service';
import { AuthService } from './auth.service';
import { User } from 'app/models/admin/user';
import { AutoLogoutDialogService } from '../common/auto-logout-dialog.service';

@Injectable({ providedIn: 'root' })
export class AutoLogoutService implements OnDestroy {
  private readonly timeoutInMs = 15 * 60 * 1000; // 15 mins
  private readonly warningBeforeMs = 1 * 60 * 1000; // 1 min before logout
  private destroy$ = new Subject<void>();
  private warningTimer?: any;
  private logoutTimer?: any;
  private isWatching = false;
  private dialogOpen = false;
  private user: User | null = null;
  private id_login_tracker?: number;

  constructor(
    private router: Router,
    private ngZone: NgZone,
    private authService: AuthService,
    private storageService: StorageService,
    private dialogService: AutoLogoutDialogService
  ) { }

  public startAfterLogin(): void {
    this.user = this.storageService.getUser();
    if (this.user && Object.keys(this.user).length > 0 && !this.isWatching) {
      this.id_login_tracker = this.user.id_login_tracker;
      this.startWatching();
    }
  }


  private startWatching(): void {
    console.log("Started Watching for user activity", this.user?.email);
    if (this.isWatching) return;
    this.isWatching = true;
    const events = ['click', 'keydown', 'scroll', 'mousemove'];
    merge(...events.map(e => fromEvent(document, e)))
      .pipe(throttleTime(1000), takeUntil(this.destroy$))
      .subscribe(() => this.resetTimer());
    this.resetTimer();
  }

  private resetTimer(): void {
    clearTimeout(this.warningTimer);
    clearTimeout(this.logoutTimer);

    console.log("Time is reseted for: ", this.user?.email);
    this.ngZone.runOutsideAngular(() => {
      // Warning dialog timer
      this.warningTimer = setTimeout(async () => {
        this.ngZone.run(async () => {
          if (this.dialogOpen) return;
          this.dialogOpen = true;

          try {
            const stay = await this.dialogService.openCountdownDialog(this.warningBeforeMs);
            this.dialogOpen = false;
            if (stay) this.resetTimer();
            else this.logout();
          } catch {
            this.dialogOpen = false;
            this.logout();
          }
        });
      }, this.timeoutInMs - this.warningBeforeMs);

      // Final auto logout
      this.logoutTimer = setTimeout(() => {
        this.ngZone.run(() => this.logout());
      }, this.timeoutInMs);
    });
  }

  /**
   * Logout and cleanup timers.
   * @param skipServerCall if true, do not call AuthService.logout (useful when logout already performed)
   */
  public logout(skipServerCall = false): void {
  clearTimeout(this.warningTimer);
  clearTimeout(this.logoutTimer);

  // STOP WATCH
  this.isWatching = false;
  this.destroy$.next();
  this.destroy$.complete();

  // 🔥 IMPORTANT: Force close dialog so knob never appears
  this.dialogService.forceClose();

  // CLEANUP USER
  const trackerId = this.id_login_tracker;
  this.user = null;
  this.id_login_tracker = undefined;
  this.storageService.clean();

  if (!skipServerCall && trackerId) {
    this.authService.logout(trackerId).subscribe();
  }

  this.router.navigate(['']);
}


  ngOnDestroy(): void {
    clearTimeout(this.warningTimer);
    clearTimeout(this.logoutTimer);
    this.destroy$.next();
    this.destroy$.complete();
  }
}
