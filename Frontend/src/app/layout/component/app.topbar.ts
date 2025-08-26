import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StyleClassModule } from 'primeng/styleclass';
import { AppConfigurator } from './app.configurator';
import { LayoutService } from '../service/layout.service';
import { StorageService } from '../../pages/service/admin/storage.service';
import { AuthService } from '../../pages/service/admin/auth.service';
import { Menubar } from 'primeng/menubar';
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { InputTextModule } from 'primeng/inputtext';
import { TabsModule } from 'primeng/tabs';
import { StepperModule } from 'primeng/stepper';
import { IconField } from 'primeng/iconfield';
import { InputIcon } from 'primeng/inputicon';
import { NotifyAdmin } from '../../models/admin/notify-admin';
import { catchError, filter, of, Subject, Subscription, switchMap, timer } from 'rxjs';
import { RealTime } from '../../models/admin/real-time';
import { NotifyMeService } from '../../pages/service/admin/notify-service';
import { RealTimeService } from '../../pages/service/admin/real-time-service';

@Component({
    selector: 'app-topbar',
    standalone: true,
    imports: [RouterModule, CommonModule, StyleClassModule, Menubar, BadgeModule, AvatarModule, InputTextModule, TabsModule, StepperModule, IconField, InputIcon, AppConfigurator],
    template: `
        <div class="layout-topbar w-full">
            <p-menubar [model]="nestedMenuItems" class="w-full">
                <ng-template #start>
                    <div class="layout-topbar-logo-container flex items-center gap-2">
                        <button class="layout-menu-button layout-topbar-action" (click)="layoutService.onMenuToggle()">
                            <i class="pi pi-bars"></i>
                        </button>
                        <a class="layout-topbar-logo flex items-center gap-2" routerLink="/">
                            <img src="assets/img/Awash_International_Bank.png" alt="Awash Bank Logo" class="mb-4 w-8 mx-auto" />
                            <span class="font-semibold text-lg text-primary">{{ category }}</span>
                        </a>
                    </div>
                </ng-template>
                <ng-template #end>
                    <div class="layout-config-menu flex items-center gap-4 w-full justify-end">
                        <button type="button" class="layout-topbar-action" (click)="toggleDarkMode()">
                            <i [ngClass]="{ pi: true, 'pi-moon': layoutService.isDarkTheme(), 'pi-sun': !layoutService.isDarkTheme() }"></i>
                        </button>
                        <div class="relative">
                            <button
                                class="layout-topbar-action layout-topbar-action-highlight"
                                pStyleClass="@next"
                                enterFromClass="hidden"
                                enterActiveClass="animate-scalein"
                                leaveToClass="hidden"
                                leaveActiveClass="animate-fadeout"
                                [hideOnOutsideClick]="true"
                            >
                                <i class="pi pi-palette"></i>
                            </button>
                            <app-configurator></app-configurator>
                        </div>
                        <p-iconfield>
                            <p-inputicon class="pi pi-search" />
                            <input type="text" pInputText placeholder="Search" />
                        </p-iconfield>
                    </div>
                </ng-template>
            </p-menubar>
        </div>
    `
})
export class AppTopbar {
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
    roles: string[] = [];
    minutes: number = 0;
    subscription!: Subscription;
    intervalPeriod: number = 1000000;
    realTime = new RealTime();
    realTimeInfo: RealTime = {};
    admin_items: MenuItem[] = [];
    auditor_items: MenuItem[] = [];
    nestedMenuItems: MenuItem[] = [];

    constructor(
        public layoutService: LayoutService,
        private storageService: StorageService,
        private authService: AuthService,
        private notificationService: NotifyMeService,
        private realTimeService: RealTimeService
    ) {}

    ngOnInit() {
        this.isLoggedIn = this.storageService.isLoggedIn();
        if (this.isLoggedIn) {
            const user = this.storageService.getUser();
            this.roles = user.roles;
            this.category = user?.category;
            this.isLoggedIn = true;
            this.email = user?.email;
            this.role = user?.roles[0] || '';
            this.id_login_tracker = user.id_login_tracker;
            this.admin = this.roles.includes('ROLE_ADMIN');
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
                routerLink: '/applayout'
            },
            {
                label: 'Log',
                icon: 'pi pi-fw pi-inbox',
                routerLink: '/applayout'
            },
            {
                label: 'Contact',
                icon: 'pi pi-fw pi-envelope',
                routerLink: ['/applayout/manage-contact']
            },
            {
                label: `Notifications (${this.check_notification_list.length || 0})`,
                icon: 'pi pi-fw pi-bell',
                routerLink: ['/applayout/admin-notification']
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

        this.auditor_items = [
            {
                label: 'Home',
                icon: 'pi pi-home',
                routerLink: '/applayout'
            },

            {
                label: 'Contact',
                icon: 'pi pi-envelope'
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

        if(this.admin){
            this.nestedMenuItems = this.admin_items;
        }else{
            this.nestedMenuItems = this.auditor_items;
        }
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
        // window.location.reload();
        // this.reloadPageAndRedirect("https://afrfms.awashbank.com/afrfms");
        // this.reloadPageAndRedirect('http://localhost:8082/afrfms');
        // this.reloadPageAndRedirect('http://localhost:4200/afrfms');
        // this.reloadPageAndRedirect('https://10.10.101.60:8082');
        //    this.reloadPageAndRedirect('https://10.10.101.76:8443/inspection/');
        this.reloadPageAndRedirect('http://localhost:4200/');
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

        // Initialize realTime object
        this.realTime = {
            ...this.realTime,
            is_saved: true,
            user_id: -1,
            isAdmin: true
        };

        // First-time fetch
        this.fetchAdminNotifications();

        // Setup polling with timer
        if (this.subscription) {
            this.subscription.unsubscribe(); // prevent multiple active subscriptions
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

        this.subscription = timer(0, this.minutes || 60000) // fallback 60s
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

                // Limit to top 3 notifications
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
