import { Injectable, NgZone } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "./auth.service";
import { StorageService } from "./storage.service";
import { User } from "app/models/admin/user";

@Injectable({ providedIn: 'root' })
export class AutoLogoutService {
  private user: User | null = null;
  private id_login_tracker?: number;
  private timeoutInMs = 15 * 60 * 1000; // 15 minutes
  private warningTimer: any;
  private logoutTimer: any;
  private isWatching = false;

  constructor(
    private router: Router,
    private ngZone: NgZone,
    private authService: AuthService,
    private storageService: StorageService
  ) {}

  /** ✅ Call this immediately after successful login */
  public startAfterLogin() {
    this.user = this.storageService.getUser();
    console.log("AutoLogout started for:", this.user);

    if (this.user) {
      this.id_login_tracker = this.user.id_login_tracker;
      this.startWatching();
    }
  }

  /** ✅ Start user activity tracking */
  private startWatching() {
    if (this.isWatching) return;
    this.isWatching = true;

    const events = ['click', 'mousemove', 'keydown', 'scroll', 'touchstart'];
    events.forEach(event => document.addEventListener(event, () => this.resetTimer(), true));

    this.resetTimer();
  }

  /** ✅ Stop all timers */
  private stopWatching() {
    clearTimeout(this.warningTimer);
    clearTimeout(this.logoutTimer);
    this.isWatching = false;
  }

  /** ✅ Reset inactivity timers */
  private resetTimer() {
    if (!this.user) return;

    clearTimeout(this.warningTimer);
    clearTimeout(this.logoutTimer);

    const warningTime = this.timeoutInMs - 1 * 60 * 1000;
    const logoutTime = this.timeoutInMs;

    this.ngZone.runOutsideAngular(() => {
      this.warningTimer = setTimeout(() => {
        this.ngZone.run(() => {
          if (!this.user) return; // skip if already logged out
          const stay = confirm('You have been inactive. Stay logged in?');
          if (stay) {
            this.resetTimer();
          } else {
            this.logout(); // immediate logout on cancel
          }
        });
      }, warningTime);

      // Auto logout
      this.logoutTimer = setTimeout(() => {
        this.ngZone.run(() => {
          if (this.user) {
            console.log("Auto logout triggered due to inactivity.");
            this.logout();
          }
        });
      }, logoutTime);
    });
  }

  /** ✅ Logout the user and clear everything */
  public logout() {
    // stop timers first
    this.stopWatching();

    const trackerId = this.id_login_tracker;

    // clear data before navigating
    this.storageService.clean();
    this.user = null;
    this.id_login_tracker = undefined;

    // Call backend logout only if tracker exists
    if (trackerId) {
      this.authService.logout(trackerId).subscribe({
        next: () => console.log('✅ Auto-logout API call completed'),
        error: err => console.error('⚠️ Logout API error:', err)
      });
    }

    // Navigate to login no matter what
    this.router.navigate(['']).then(() => {
      console.log("✅ Navigated to login after auto-logout");
    });
  }
}
