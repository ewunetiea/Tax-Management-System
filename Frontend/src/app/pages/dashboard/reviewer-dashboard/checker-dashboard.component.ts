import { Component, OnInit, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { LayoutService } from '../../../layout/service/layout.service';
import { SharedUiModule } from '../../../../shared-ui';
import { debounceTime, forkJoin, Subject, Subscription, takeUntil } from 'rxjs';
import { Announcement } from '../../../models/approver/announcement';
import { AnnouncementService } from '../../../service/approver/announcement.service';
import { EditorModule } from 'primeng/editor';
import { ReviewerDashboardService } from 'app/service/reviewer/reviewer-dashboard-service';
import { AdminDashboardService } from 'app/service/admin/admin-dashboard-service';
import { StorageService } from 'app/service/sharedService/storage.service';
import { User } from 'app/models/admin/user';
import { ReviewerDashboard } from 'app/models/reviewer/reviewerDashboard';
import { ChartModule } from 'primeng/chart';
import { BarAndLineChartSkeleton } from 'app/pages/skeleton/bar-and-lign-chart/bar-and-lign-chart';
import { CardSkeleton } from 'app/pages/skeleton/card/four-card';
import { PieDougnutPolarSkeleton } from 'app/pages/skeleton/dougnut-polar-chart/pie-dougnut-polar';
import { PieDougnutPolarSkeletonDescription } from 'app/pages/skeleton/dougnut-polar-chart/polar-pie-dougnut-title';

@Component({
  selector: 'app-checker-dashboard',
  templateUrl: './checker-dashboard.component.html',
  standalone: true,
  imports: [
    SharedUiModule,
    CardSkeleton,
    BarAndLineChartSkeleton,
    PieDougnutPolarSkeleton,
    PieDougnutPolarSkeletonDescription,
    EditorModule,
    ChartModule
  ]
})
export class CheckerDashboardComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  subscription: Subscription;
  loading = true;
  user: User = new User();
  announcement: Announcement = new Announcement();
  reviewerDashboard: ReviewerDashboard = new ReviewerDashboard();
  taxStatus: number[] = [];
  branch_id?: number;
  stackedBarTaxesStatusData: any;
  stackedBarOptions: any;

  constructor(
    private layoutService: LayoutService,
    private announcemetService: AnnouncementService,
    private reviewerDashboardService: ReviewerDashboardService,
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
      taxStatus: this.reviewerDashboardService.getTaxStatusForReviewer(this.branch_id),
      announcement: this.announcemetService.fetchAnnouncemetForDashBoard(),
      stackedBarTaxesStatusData: this.reviewerDashboardService.getStackedBarTaxesStatusData(this.branch_id)
    })
    .pipe(takeUntil(this.destroy$))
    .subscribe({
      next: ({ taxStatus, announcement, stackedBarTaxesStatusData }) => {
        this.taxStatus = taxStatus;
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

    const reviewedData = labels.map((_, i) => {
      const found = sortedData.find(d => d.month === i + 1);
      return found ? found.Reviewed || 0 : 0;
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
        { type: 'bar', label: 'Sent',  backgroundColor: documentStyle.getPropertyValue('--p-orange-400'), data: reviewedData },
        { type: 'bar', label: 'Settled', backgroundColor: documentStyle.getPropertyValue('--p-cyan-500'), data: approvedData },
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
