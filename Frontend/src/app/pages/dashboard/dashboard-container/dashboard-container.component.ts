// import { Component } from '@angular/core';
// import { StorageService } from '../../service/admin/storage.service';

import { Component } from "@angular/core";
import { BestSellingWidget } from "../components/bestsellingwidget";
import { NotificationsWidget } from "../components/notificationswidget";
import { RecentSalesWidget } from "../components/recentsaleswidget";
import { RevenueStreamWidget } from "../components/revenuestreamwidget";
import { StatsWidget } from "../components/statswidget";
import { AdminDashboardComponent } from "../admin-dashboard/admin-dashboard.component";

// import { AdminDashboardComponent } from '../admin-dashboard/admin-dashboard.component';
// import { SharedUiModule } from '../../../../shared-ui';

// @Component({
//         standalone: true,

//   selector: 'app-dashboard-container',
//   imports: [SharedUiModule,  AdminDashboardComponent],
//   templateUrl: './dashboard-container.component.html'
// })
// export class DashboardContainerComponent {
//   isLoggedIn = false;
//   private roles: string[] = [];
//   admin = false;
//   approver = false;
//   followup_officer = false;
//   reviewer = false;
//   auditee = false;
//   auditee_division = false;
//   auditor = false;
//   auditor_followup_officer = false;
//   approver_reviewer = false;
//   higher_official = false;

//   constructor(
//     private storageService: StorageService,
//   ) {}
//   ngOnInit(): void {
//     this.isLoggedIn = this.storageService.isLoggedIn();
//     if (this.isLoggedIn) {
//       const user = this.storageService.getUser();
//       this.roles = user.roles;
//       this.admin = this.roles.includes('ROLE_ADMIN');
//       this.approver =
//         this.roles.includes('ROLE_APPROVER_IS') ||
//         this.roles.includes('ROLE_APPROVER_MGT');
//       this.reviewer =
//         this.roles.includes('ROLE_REVIEWER_IS') ||
//         this.roles.includes('ROLE_REVIEWER_MGT');
//       this.followup_officer =
//         this.roles.includes('ROLE_FOLLOWUP_OFFICER_IS') ||
//         this.roles.includes('ROLE_FOLLOWUP_OFFICER_MGT');
//       this.auditor =
//         this.roles.includes('ROLE_AUDITOR_IS') ||
//         this.roles.includes('ROLE_AUDITOR_MGT');
//       this.auditee = this.roles.includes('ROLE_AUDITEE');
//       this.auditee_division = this.roles.includes('ROLE_AUDITEE_DIVISION');
//       this.higher_official = this.roles.includes('ROLE_HIGHER_OFFICIAL');
//       if (this.auditor && this.followup_officer) {
//         this.auditor_followup_officer = true;
//       }
//       if (this.approver && this.reviewer) {
//         this.approver_reviewer = true;
//       }
//     }
//   }

// }






@Component({
    standalone:true,
    selector: 'app-dashboard',
    imports: [StatsWidget, RecentSalesWidget, BestSellingWidget, RevenueStreamWidget, NotificationsWidget],
    template: `
        <div class="grid grid-cols-12 gap-8">
            <app-stats-widget class="contents" />
            <div class="col-span-12 xl:col-span-6">
                <app-recent-sales-widget />
                <app-best-selling-widget />
            </div>
            <div class="col-span-12 xl:col-span-6">
                <app-revenue-stream-widget />
                <app-notifications-widget />
            </div>

            <div class="grid grid-cols-12 gap-8">


        </div>
    `
})
export class DashboardContainerComponent {}
