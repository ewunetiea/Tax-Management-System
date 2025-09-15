import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Skeleton } from "primeng/skeleton";

@Component({
    standalone: true,
    selector: 'app-bar-and-line-chart-skeleton',
    imports: [CommonModule, Skeleton],
template: `
                <!-- title placeholder -->
                <p-skeleton width="6rem" height="1.5rem" styleClass="mb-4"></p-skeleton>

                <!-- legends  -->

                <div class="flex justify-center gap-4 mt-3 mb-2">
                    <p-skeleton width="6rem" height="1rem"></p-skeleton>
                    <p-skeleton width="6rem" height="1rem"></p-skeleton>
                </div>
             <!-- big rectangle to simulate the chart area -->

                <p-skeleton width="100%" styleClass="!h-96"></p-skeleton>
             <!-- x- axis in month  -->

                <div class="flex justify-between mt-2">
                    <p-skeleton width="2rem" height="1rem"></p-skeleton>
                    <p-skeleton width="2rem" height="1rem"></p-skeleton>
                    <p-skeleton width="2rem" height="1rem"></p-skeleton>
                    <p-skeleton width="2rem" height="1rem"></p-skeleton>
                    <p-skeleton width="2rem" height="1rem"></p-skeleton>
                    <p-skeleton width="2rem" height="1rem"></p-skeleton>
                </div>`
})
export class BarAndLineChartSkeleton {}



// import { Component, OnInit } from '@angular/core';
// import { Subscription } from 'rxjs';
// import { AdminDashboard } from '../../../models/admin/admin-dashboard';
// import { AppConfig } from '../../../models/admin/appconfig';
// import { MenuItem, MessageService } from 'primeng/api';
// import { StorageService } from '../../../service/sharedService/storage.service';
// import { SharedUiModule } from '../../../../shared-ui';
// import { HttpErrorResponse } from '@angular/common/http';
// import { User } from '../../../models/admin/user';
// import { UserService } from '../../../service/admin/user.service';

// @Component({
//     standalone: true,
//     selector: 'app-stats-widget',
//     imports: [SharedUiModule],

//     template: `<div class="col-span-12 lg:col-span-6 xl:col-span-3">
//             <div class="card mb-0">
//                 <div class="flex justify-between mb-4">
//                     <div>
//                         <span class="block text-muted-color font-medium mb-4"
//                             ><h5 class="card-title">IS <span>| Total</span></h5></span
//                         >
//                         <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">
//                             {{ adminDashboardData!.card_data?.[0] }}
//                         </div>
//                     </div>
//                     <div class="flex items-center justify-center bg-blue-100 dark:bg-blue-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
//                         <i class="pi pi-shopping-cart text-blue-500 !text-xl"></i>
//                     </div>
//                 </div>
//                 <span class="text-primary font-medium"> Users </span>
//             </div>
//         </div>
//         <div class="col-span-12 lg:col-span-6 xl:col-span-3">
//             <div class="card mb-0">
//                 <div class="flex justify-between mb-4">
//                     <div>
//                         <span class="block text-muted-color font-medium mb-4">
//                             <h5 class="card-title">MGT <span>| Total</span></h5>
//                         </span>
//                         <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{ adminDashboardData.card_data?.[1] }}</div>
//                     </div>
//                     <div class="flex items-center justify-center bg-orange-100 dark:bg-orange-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
//                         <i class="pi pi-dollar text-orange-500 !text-xl"></i>
//                     </div>
//                 </div>
//                 <span class="text-primary font-medium"> Users </span>
//             </div>
//         </div>
//         <div class="col-span-12 lg:col-span-6 xl:col-span-3">
//             <div class="card mb-0">
//                 <div class="flex justify-between mb-4">
//                     <div>
//                         <span class="block text-muted-color font-medium mb-4">
//                             <h5 class="card-title">BFA <span>| Total</span></h5>
//                         </span>
//                         <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{ adminDashboardData.card_data?.[2] }}</div>
//                     </div>
//                     <div class="flex items-center justify-center bg-cyan-100 dark:bg-cyan-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
//                         <i class="pi pi-users text-cyan-500 !text-xl"></i>
//                     </div>
//                 </div>
//                 <span class="text-primary font-medium">Users </span>
//             </div>
//         </div>
//         <div class="col-span-12 lg:col-span-6 xl:col-span-3">
//             <div class="card mb-0">
//                 <div class="flex justify-between mb-4">
//                     <div>
//                         <span class="block text-muted-color font-medium mb-4">
//                             <h5 class="card-title">INS <span>| Total</span></h5>
//                         </span>
//                         <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{ adminDashboardData.card_data?.[3] }}</div>
//                     </div>
//                     <div class="flex items-center justify-center bg-purple-100 dark:bg-purple-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
//                         <i class="pi pi-comment text-purple-500 !text-xl"></i>
//                     </div>
//                 </div>
//                 <span class="text-muted-color">Users</span>
//             </div>
//         </div>`
// })
// export class StatsWidget implements OnInit {
//     isLoggedIn = false;
//     currentUser = new User();
//     adminDashboardData = new AdminDashboard();
//     loading = false;

//     // Chart & Dashboard properties
//     basicData: any;
//     multiAxisData: any;
//     usersPerRegionandAudit: any;
//     multiAxisOptions: any;
//     lineStylesData: any;
//     basicOptions: any;
//     subscription?: Subscription;
//     config?: AppConfig;
//     chartOptions: any;
//     stackedData: any;
//     stackedOptions: any;
//     horizontalOptions: any;

//     // Bar chart data
//     basicDataBarChart: any;
//     basicOptionsBarChart: any;
//     multiAxisDataBarChart: any;
//     chartOptionsBarChart: any;
//     multiAxisOptionsBarChart: any;
//     stackedDataBarChart: any;
//     stackedbarChartRolePerAudits: any;
//     barChartRolePerAudits: any;
//     doughnut_data: any;
//     chartOptionsDoughnut: any;
//     horizontal_bar_chart_data: any;
//     stackedOptionsBarChart: any;
//     horizontalOptionsBarChart: any;
//     subscriptionBarChart?: Subscription;
//     configBarChart?: AppConfig;

//     // Doughnut chart
//     datadoughnut: any;
//     chartOptionsdoughnut: any;
//     subscriptiondoughnut?: Subscription;
//     configdoughnut?: AppConfig;

//     // Pie chart
//     polarChartData: any;
//     polarChartUsersStatusData: any;
//     polarChartOptions: any;
//     chartOptionsPie: any;
//     subscriptionPie?: Subscription;
//     configPie?: AppConfig;
//     radar_data: any;
//     line_chart_data: any;

//     breadcrumbText: string = 'Admin Dashboard';
//     items: MenuItem[] | undefined;
//     home: MenuItem | undefined;

//     constructor(
//         private messageService: MessageService,
//         private storageService: StorageService,
//         private adminDashboardService: UserService
//     ) {}

//     ngOnInit() {
//         this.home = { icon: 'pi pi-home', routerLink: '/' };
//         this.items = [{ label: this.breadcrumbText }];
//         this.isLoggedIn = this.storageService.isLoggedIn();

//         // Provide default card data to avoid template errors
//         if (!this.adminDashboardData.card_data) {
//             this.adminDashboardData.card_data = [152, 97, 28441, 85];
//         }

//         if (this.isLoggedIn) {
//             setTimeout(() => {
//                 this.loading = true;
//             }, 4000);
//             this.getDashBoardData();
//         }

//         // Chart options can be initialized here (multiAxisOptions, etc.)
//     }

//     getCardTitle(index: number): string {
//         return ['IS', 'MGT', 'BFA', 'INS'][index] || '';
//     }

//     getCardIcon(index: number): string {
//         return ['pi pi-bookmark text-primary', 'pi pi-step-forward text-success', 'pi pi-minus-circle text-warning', 'pi pi-verified text-info'][index] || '';
//     }

//     getCardBgColor(index: number): string {
//         return (
//             [
//                 '#cfe2ff', // blue-100
//                 '#fff3cd', // orange-100
//                 '#cff4fc', // cyan-100
//                 '#e2d9f3' // purple-100
//             ][index] || '#f0f0f0'
//         );
//     }

//     //   getCardExtra(index: number): string {
//     //     return ['24 new', '%52+', '520', '85'][index] || '';
//     //   }

//     getCardSubtext(index: number): string {
//         return ['since last visit', 'since last week', 'newly registered', 'responded'][index] || '';
//     }

//     getDashBoardData() {
//         this.currentUser.id = this.storageService.getUser().id;
//         this.currentUser.category = this.storageService.getUser().category;
//         this.adminDashboardService.drawBarChartUsersPerRegion(this.currentUser).subscribe(
//             (response) => {
//                 this.loading = false;
//                 this.adminDashboardData = response;
//             },
//             (error: HttpErrorResponse) => {
//                 this.messageService.add({
//                     severity: 'error',
//                     summary: error.status === 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching findings!',
//                     detail: ''
//                 });
//             }
//         );
//     }
// }
