import { Injectable, NgZone } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "./auth.service";
import { StorageService } from "./storage.service";

@Injectable({ providedIn: 'root' })
export class AutoLogoutService {
    private id_login_tracker?: number;
    private timeoutInMs = 2 * 60 * 1000;
    private warningTimer: any;
    private logoutTimer: any;
    private isWatching = false;

    constructor(
        private router: Router,
        private ngZone: NgZone,
        private authService: AuthService,
        private storageService: StorageService
    ) { }

    /** Call this after login */
    public startAfterLogin() {
        const user = this.storageService.getUser();
        console.log("User Exists:", user);
        if (user && user.id_login_tracker) {
            this.id_login_tracker = user.id_login_tracker;
            this.startWatching();
        }
    }

    private startWatching() {
        if (this.isWatching) return;
        this.isWatching = true;

        ['click', 'mousemove', 'keydown', 'scroll', 'touchstart'].forEach(event =>
            document.addEventListener(event, () => this.resetTimer(), true)
        );

        this.resetTimer();
    }

    private stopWatching() {
        clearTimeout(this.warningTimer);
        clearTimeout(this.logoutTimer);
        this.isWatching = false;
    }

    private resetTimer() {
        this.stopWatching();

        const warningTime = this.timeoutInMs - 1 * 60 * 1000; 
        const logoutTime = this.timeoutInMs; 

        this.ngZone.runOutsideAngular(() => {
            // Warning dialog
            this.warningTimer = setTimeout(() => {
                this.ngZone.run(() => {
                    const stay = confirm('You have been inactive. Stay logged in?');
                    if (stay) {
                        this.resetTimer();
                    } else {
                        this.logout();
                    }
                });
            }, warningTime);

            // Logout timer
            this.logoutTimer = setTimeout(() => {
            this.ngZone.run(() => this.logout());
            }, logoutTime);
        });
    }

    public logout() {
        this.stopWatching();
        this.storageService.clean();

        if (this.id_login_tracker) {
            this.authService.logout(this.id_login_tracker).subscribe({
                next: () => console.log('Auto-logout due to inactivity'),
                error: err => console.error(err)
            });
        }
        this.router.navigate(['/login']);
    }
}
