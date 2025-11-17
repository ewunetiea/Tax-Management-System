import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router, RouterModule } from '@angular/router';
import { AppConfigurator } from './app.configurator';
import { LayoutService } from '../service/layout.service';
import { NotifyAdmin } from '../../models/admin/notify-admin';
import { catchError, filter, of, Subscription, switchMap, timer } from 'rxjs';
import { RealTime } from '../../models/admin/real-time';
import { SharedUiModule } from '../../../shared-ui';
import { SettingConfigurationComponent } from '../../pages/admin/setting-configuration/setting-configuration.component';
import { LogComponent } from '../../pages/admin/log/log/log.component';
import { AdminNotificationComponent } from '../../pages/admin/notification/admin-notification/admin-notification.component';
import { OverlayBadgeModule } from 'primeng/overlaybadge';
import { AuthService } from '../../service/sharedService/auth.service';
import { NotifyMeService } from '../../service/admin/notify-service';
import { RealTimeService } from '../../service/admin/real-time-service';
import { StorageService } from '../../service/sharedService/storage.service';
import { AutoLogoutService } from 'app/service/sharedService/auto-logout.service';

@Component({
    selector: 'app-topbar',
    standalone: true,
    imports: [RouterModule, SharedUiModule, AppConfigurator, SettingConfigurationComponent, LogComponent, AdminNotificationComponent, OverlayBadgeModule],
    templateUrl: './app.topbar.component.html'
})
export class AppTopbar {
    showJwtModal = false;
    showLogModal = false;
    showNotificationModal = false;
    isLoggedIn = false;
    id_login_tracker?: number;
    items!: MenuItem[];
    role?: string;
    email?: string;
    category?: string;
    admin_notification_list: NotifyAdmin[] = [];
    check_notification_list: NotifyAdmin[] = [];
    admin_notification: any[] = [];
    admin = false;
    maker = false;
    roles: string[] = [];
    minutes: number = 0;
    subscription!: Subscription;
    intervalPeriod: number = 1000000;
    realTime = new RealTime();
    realTimeInfo: RealTime = {};
    admin_items: MenuItem[] = [];
    maker_items: MenuItem[] = [];
    nestedMenuItems: MenuItem[] = [];
    loading = true;

    constructor(
        public layoutService: LayoutService,
        private storageService: StorageService,
        private authService: AuthService,
        private notificationService: NotifyMeService,
        private realTimeService: RealTimeService,
        private autoLogoutService: AutoLogoutService,
        private router: Router,
    ) {}

    ngOnInit() {
        this.isLoggedIn = this.storageService.isLoggedIn();
        if (this.isLoggedIn) {
            const user = this.storageService.getUser();
            this.roles = user.roles;
            this.category = this.roles.length > 0 ? this.roles[0].replace('ROLE_', '') : '';
            this.isLoggedIn = true;
            this.email = user?.email;
            this.role = user?.roles[0] || '';
            this.id_login_tracker = user.id_login_tracker;
            this.admin = this.roles.includes('ROLE_ADMIN');
            this.maker = this.roles.includes('ROLE_MAKER');
        }

        this.admin_items = [
            {
                label: 'Home',
                icon: 'pi pi-fw pi-home',
                routerLink: '/applayout'
            },

            {
                label: 'JWT',
                icon: 'pi pi-fw pi-cog',
                command: () => (this.showJwtModal = true) // ✅ open modal
            },
            {
                label: 'Log',
                icon: 'pi pi-fw pi-inbox',
                command: () => (this.showLogModal = true) // ✅ open modal
            },
            {
                label: 'Contact',
                icon: 'pi pi-fw pi-envelope',
                routerLink: ['/applayout/manage-contact']
            },
            {
                label: 'Notification',
                icon: 'pi pi-fw pi-bell',
                badge: this.check_notification_list.length > 0 ? this.check_notification_list.length.toString() : undefined,
                badgeStyleClass: 'p-badge-danger' ,
                command: () => (this.showNotificationModal = true)
            },
            {
                label: 'Profile',
                icon: 'pi pi-fw pi-user',
                items: [
                    {
                        label: 'My Profile',
                        icon: 'pi pi-fw pi-user',
                        routerLink: ['/applayout/user-profile']
                    }
                ]
            },
            {
                label: 'Logout',
                icon: 'pi pi-fw pi-sign-out',
                command: () => this.logout()
            }
        ];

        this.maker_items = [
            {
                label: 'Home',
                icon: 'pi pi-home',
                routerLink: '/applayout'
            },

            {
                label: 'Contact',
                icon: 'pi pi-envelope',
                routerLink: ['/applayout/manage-contact']
            },
            {
                label: 'Profile',
                icon: 'pi pi-fw pi-user',
                items: [
                    {
                        label: 'My Profile',
                        icon: 'pi pi-fw pi-user',
                        routerLink: ['/applayout/user-profile']
                    }
                ]
            },
            {
                label: 'Logout',
                icon: 'pi pi-sign-out',
                command: () => this.logout()
            }
        ];

        if (this.admin) {
            this.nestedMenuItems = this.admin_items;
        } else {
            this.nestedMenuItems = this.maker_items;
        }

        setTimeout(() => {
            this.loading = false;
        }, 1000); // 1000 ms = 1 seconds
    }

    toggleDarkMode() {
        this.layoutService.layoutConfig.update((state) => ({ ...state, darkTheme: !state.darkTheme }));
    }

    logout(): void {
        this.authService.logout(this.id_login_tracker).subscribe({
            next: () => {
                this.handleLogoutSuccess();
            },
            error: (err) => {
                this.handleLogoutError(err);
            }
        });
    }

    private handleLogoutSuccess(): void {
        this.storageService.clean();
        this.autoLogoutService.logout();
        //  this.router.navigate(['']);
        // this.reloadPageAndRedirect('http://localhost:8082/');
        // this.reloadPageAndRedirect('https://10.10.106.194:8443/tms/');

        this.reloadPageAndRedirect('http://localhost:4200');
    }

    private handleLogoutError(err: any): void {
        console.error(err);
    }

    relodePage(): void {
        window.location.reload();
    }

    private reloadPageAndRedirect(redirectUrl: string): void {
        window.location.reload();
        window.location.href = redirectUrl;
    }

    notifyAdmin() {
        if (!this.admin) {
            return;
        }

        this.realTime = {
            ...this.realTime,
            is_saved: true,
            user_id: -1,
            isAdmin: true
        };

        this.fetchAdminNotifications();

        if (this.subscription) {
            this.subscription.unsubscribe();
        }

        this.realTimeInfo = {
            user_id: -1,
            approver_approve: false,
            approver_reject: false,
            maker_disburse: false,
            maker_edit: false,
            notify: false,
            isAdmin: true
        };

        this.subscription = timer(0, this.minutes || 60000)
            .pipe(
                switchMap(() =>
                    this.realTimeService.getRealTimeByUserId(this.realTimeInfo).pipe(
                        catchError((err) => {
                            console.error('Real-time fetch error:', err);
                            return of(undefined);
                        })
                    )
                ),
                filter((data) => data !== undefined)
            )
            .subscribe((data) => {
                this.realTime = { ...data, is_saved: false } as RealTime;
                this.fetchAdminNotifications();
            });
    }

    private fetchAdminNotifications() {
        this.notificationService.notifyAdmin(this.realTime).subscribe({
            next: (data: NotifyAdmin[]) => {
                this.check_notification_list = data || [];
                this.admin_notification_list = [...this.check_notification_list];

                if (this.admin_notification_list.length > 0) {
                    this.alertMe();
                }

                this.admin_notification = this.admin_notification_list.slice(0, 3);
            },
            error: (err) => {
                console.error('Notification fetch error:', err);
            }
        });
    }

    alertMe() {
        let audio = new Audio();
        audio.src = 'assets/alert_sound/alert.mp3';
        audio.load();
        audio.play();
    }
}
