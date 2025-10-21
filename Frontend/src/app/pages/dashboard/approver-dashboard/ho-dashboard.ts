import { Component } from '@angular/core';
import { debounceTime, forkJoin, Subject, Subscription, takeUntil } from 'rxjs';
import { LayoutService } from '../../../layout/service/layout.service';

import { CardSkeleton } from "../../skeleton/card/four-card";
import { SharedUiModule } from '../../../../shared-ui';
import { BarAndLineChartSkeleton } from "../../skeleton/bar-and-lign-chart/bar-and-lign-chart";
import { PieDougnutPolarSkeleton } from "../../skeleton/dougnut-polar-chart/pie-dougnut-polar";
import { PieDougnutPolarSkeletonDescription } from "../../skeleton/dougnut-polar-chart/polar-pie-dougnut-title";
import { RecentActivity } from 'app/models/admin/recent-activity';
import { User } from 'app/models/admin/user';
import { Announcement } from 'app/models/approver/announcement';
import { ReviewerDashboard } from 'app/models/reviewer/reviewerDashboard';
import { AdminDashboardService } from 'app/service/admin/admin-dashboard-service';
import { AnnouncementService } from 'app/service/approver/announcement.service';
import { ReviewerDashboardService } from 'app/service/reviewer/reviewer-dashboard-service';
import { StorageService } from 'app/service/sharedService/storage.service';
import { EditorModule } from 'primeng/editor';
import { ChartModule } from 'primeng/chart';
import { ApproverDashboardService } from 'app/service/approver/approver-dashboard-service';

@Component({
    selector: 'app-ho-dashboard',
    standalone: true,
    imports: [SharedUiModule, CardSkeleton, BarAndLineChartSkeleton, PieDougnutPolarSkeleton, PieDougnutPolarSkeletonDescription, EditorModule, ChartModule],
    templateUrl: './ho-dashboard.component.html',
})
export class HODashboard {
    subscription: Subscription;
      loading = true;
      user: User = new User();
      private destroy$ = new Subject<void>();
      announcement: Announcement = new Announcement();
      reviewerDashboard: ReviewerDashboard = new ReviewerDashboard();
      taxStatus: number[] = [];
      recentActivityies: RecentActivity[] = [];
      branch_id?: number;
      stackedBarTaxesStatusData: any;
      stackedBarOptions: any;
    
      constructor(
        private layoutService: LayoutService,
        private announcemetService: AnnouncementService,
        private approverDashboardService: ApproverDashboardService,
        private adminDashboardService: AdminDashboardService,
        private storageService: StorageService,
      ) {
        this.subscription = this.layoutService.configUpdate$.pipe(debounceTime(25)).subscribe(() => {});
      }
    
      ngOnInit() {
        this.user = this.storageService.getUser();
        this.branch_id = this.user.branch?.id;
        this.loadReviewerDashboards();
      }
    
      loadReviewerDashboards() {
        forkJoin({
          taxStatus: this.approverDashboardService.getTaxStatusForApprover(this.branch_id),
          recentActivities: this.adminDashboardService.getRecentActivity(this.user.id as any),
          announcement: this.announcemetService.fetchAnnouncemetForDashBoard(),
          stackedBarTaxesStatusData: this.approverDashboardService.getStackedBarTaxesStatusData(this.branch_id)
        })
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: ({ taxStatus, recentActivities, announcement, stackedBarTaxesStatusData }) => {
            this.taxStatus = taxStatus;
            this.recentActivityies = recentActivities;
            this.announcement = announcement;
    
            // Build chart config
            const chartConfig = this.buildStackedBarChart(stackedBarTaxesStatusData);
            this.stackedBarTaxesStatusData = chartConfig.data;
            this.stackedBarOptions = chartConfig.options;
    
            // 👇 Force refresh
            setTimeout(() => {
              this.stackedBarTaxesStatusData = { ...this.stackedBarTaxesStatusData };
            }, 0);
    
            this.loading = false;
          },
          error: (err) => {
            console.error('Error loading dashboard data:', err);
            this.loading = false;
          }
        });
      }
    
      private buildStackedBarChart(data: any[]) {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--p-text-color');
        const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
        const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');
    
        // Sort data by month
        const sortedData = (data || []).sort((a, b) => a.month - b.month);
    
        const labels = [
          'January', 'February', 'March', 'April', 'May', 'June',
          'July', 'August', 'September', 'October', 'November', 'December'
        ];
    
        const pendingData = labels.map((_, i) => {
          const found = sortedData.find(d => d.month === i + 1);
          return found ? found.Pending || 0 : 0;
        });
    
        const approvedData = labels.map((_, i) => {
          const found = sortedData.find(d => d.month === i + 1);
          return found ? found.Approved || 0 : 0;
        });
    
        const rejectedData = labels.map((_, i) => {
          const found = sortedData.find(d => d.month === i + 1);
          return found ? found.Rejected || 0 : 0;
        });
    
        const chartData = {
          labels,
          datasets: [
            { type: 'bar', label: 'Pending', backgroundColor: documentStyle.getPropertyValue('--p-gray-500'), data: pendingData },
            { type: 'bar', label: 'Approved', backgroundColor: documentStyle.getPropertyValue('--p-cyan-500'), data: approvedData },
            { type: 'bar', label: 'Rejected',  backgroundColor: documentStyle.getPropertyValue('--p-pink-500'), data: rejectedData }
          ]
        };
    
        const chartOptions = {
          maintainAspectRatio: false,
          responsive: true,
          aspectRatio: 0.8,
          plugins: {
            tooltip: { mode: 'index', intersect: false },
            legend: { position: 'top', labels: { color: textColor } }
          },
          layout: { padding: { bottom: 20 } },
          scales: {
            x: { stacked: true, ticks: { color: textColorSecondary, autoSkip: false, maxRotation: 0, minRotation: 0 }, grid: { color: surfaceBorder, drawBorder: false } },
            y: { stacked: true, beginAtZero: true, ticks: { color: textColorSecondary }, grid: { color: surfaceBorder, drawBorder: false } }
          }
        };
    
        return { data: chartData, options: chartOptions };
      }
    
      ngOnDestroy() {
        this.destroy$.next();
        this.destroy$.complete();
      }
}
