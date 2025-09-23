import { CommonModule, DOCUMENT } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { environment } from '../../../../../environments/environment.prod';
import { NotifyAdmin } from '../../../../models/admin/notify-admin';
import { RealTime } from '../../../../models/admin/real-time';
import { SharedUiModule } from '../../../../../shared-ui';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { AuthService } from '../../../../service/sharedService/auth.service';
import { NotifyMeService } from '../../../../service/admin/notify-service';

@Component({
    selector: 'app-admin-notification',
    imports: [SharedUiModule, CommonModule],
    templateUrl: './admin-notification.component.html',
    styleUrl: './admin-notification.component.scss'
})
export class AdminNotificationComponent {
    exportSettings!: {
        columnsHeader: boolean;
        fileName: string;
        hiddenColumns: boolean;
    };
    submitted: boolean = false;
    errorMessage = '';
    success: boolean = false;
    isLoggedIn = false;
    private roles: string[] = [];
    admin = false;
    id_login_tracker?: number;
    username?: string;
    index = 0;
    admin_notification_list: NotifyAdmin[] = [];
    check_notification_list: NotifyAdmin[] = [];
    admin_notification: any[] = [];
    environment = environment;
    photoUrl: String = '';
    minutes: number = 0;
    subscription!: Subscription;
    intervalPeriod: number = 1000000;
    realTime = new RealTime();
    realTimeInfo: RealTime = {};
    logDialog = false;
    notificationDialog = false;
    settingDialog = false;

    constructor(
        @Inject(DOCUMENT) private document: Document,
        private storageService: StorageService,
        private authService: AuthService,
        private notificationService: NotifyMeService,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.isLoggedIn = this.storageService.isLoggedIn();
        this.document.body.classList.toggle('toggle-sidebar');

        if (this.isLoggedIn) {
            const user = this.storageService.getUser();
            this.roles = user.roles;
            this.admin = this.roles.includes('ROLE_ADMIN');
            this.username = user.email;
            this.id_login_tracker = user.id_login_tracker;
            this.photoUrl = user.photoUrl;
            this.minutes = this.intervalPeriod * 6 * 10;

            // if (this.admin) {
            //   this.realTime.is_saved = true;
            //   this.realTime.user_id = -1;
            //   this.realTime.isAdmin = true;
            //   let count: number = 0;
            //   this.notificationService.notifyAdmin(this.realTime).subscribe(
            //     (data) => {
            //       this.admin_notification_list = data;
            //       this.check_notification_list = this.admin_notification_list;
            //       if (this.admin_notification_list) {
            //         if (this.admin_notification_list.length > 0) {
            //           this.alertMe();
            //         }
            //       }
            //       for (const notifyAdmin of this.admin_notification_list) {
            //         count++;
            //         this.admin_notification.push(notifyAdmin);
            //         if (count == 3) {
            //           break;
            //         }
            //       }
            //     },
            //     (error) => {
            //     }
            //   );

            //   this.realTimeInfo.user_id = -1;
            //   this.realTimeInfo.approver_approve = false;
            //   this.realTimeInfo.approver_reject = false;
            //   this.realTimeInfo.maker_disburse = false;
            //   this.realTimeInfo.maker_edit = false;
            //   this.realTimeInfo.notify = false;
            //   this.realTimeInfo.isAdmin = true;

            //   this.subscription = timer(0, this.minutes)
            //     .pipe(
            //       switchMap(() => {
            //         return this.realTimeService
            //           .getRealTimeByUserId(this.realTimeInfo)
            //           .pipe(
            //             catchError((err) => {
            //               // Handle errors
            //               console.error(err);
            //               return of(undefined);
            //             })
            //           );
            //       }),
            //       filter((data) => data !== undefined)
            //     )
            //     .subscribe((data) => {
            //       this.realTime = data;
            //       this.realTime.is_saved = false;
            //       this.notificationService.notifyAdmin(this.realTime).subscribe({
            //         next: (response: any[]) => {
            //           this.check_notification_list = response;
            //           if (this.check_notification_list) {
            //             if (this.check_notification_list.length > 0) {
            //               this.admin_notification = [];
            //               this.admin_notification_list = this.check_notification_list;
            //               // if (this.admin_notification_list.length > 0) {
            //               this.alertMe();
            //               // }
            //               let count = 0;
            //               for (const notification of this.admin_notification_list) {
            //                 count++;
            //                 this.admin_notification.push(notification);
            //                 if (count == 3) {
            //                   break;
            //                 }
            //               }
            //             }
            //           }
            //         },
            //         error: (error) => {
            //         },
            //       });
            //     });
            // }
        }
    }

    sidebarToggle() {
        this.document.body.classList.toggle('toggle-sidebar');
    }

    logout(): void {
        this.authService.logout(this.id_login_tracker).subscribe({
            next: (res) => {
                this.storageService.clean();
                window.location.reload();
            },
            error: (err) => {
                console.log(err);
            }
        });
    }

    viewedNotificationsByAdminDropDown(): void {
        this.notificationService.viewedNotificationsByAdmin(this.admin_notification).subscribe({
            error: (err) => {
                console.log(err);
            }
        });
    }

    adminNotificationModal() {
        this.notificationDialog = true;
        this.viewedAdminNotificationsModal();
    }

    viewedAdminNotificationsModal(): void {
        this.notificationService.viewedNotificationsByAdmin(this.admin_notification_list).subscribe({
            error: (err) => {
                console.log(err);
            }
        });
    }

    alertMe() {
        let audio = new Audio();
        audio.src = 'assets/alert_sound/alert.mp3';
        audio.load();
        audio.play();
    }

    back() {
        this.router.navigate(['dashboard']);
        window.location.reload();
    }
    alertClosed(status: String): void {
        if (status === 'success') {
            this.submitted = false;
        } else if (status === 'danger') {
            this.errorMessage = '';
        }
    }

    openLogModal() {
        this.logDialog = true;
    }

    hideDialog() {
        this.logDialog = false;
        this.settingDialog = false;
    }
}
