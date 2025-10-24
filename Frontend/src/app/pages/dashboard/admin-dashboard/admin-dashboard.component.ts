import { Component, OnDestroy, OnInit } from '@angular/core';
import { AdminDashboardService } from '../../../service/admin/admin-dashboard-service';
import { StorageService } from '../../../service/sharedService/storage.service';
import { AdminDashboard } from '../../../models/admin/admin-dashboard';
import { RecentActivity } from '../../../models/admin/recent-activity';
import { User } from '../../../models/admin/user';
import { MenuItem } from 'primeng/api';
import { SharedUiModule } from '../../../../shared-ui';
import { CardSkeleton } from "../../skeleton/card/four-card";
import { BarAndLineChartSkeleton } from "../../skeleton/bar-and-lign-chart/bar-and-lign-chart";
import { Subject, forkJoin, takeUntil } from 'rxjs';
import { DoughnutController } from 'chart.js';

@Component({
  standalone: true,
  selector: 'app-admin-dashboard',
  imports: [SharedUiModule, CardSkeleton, BarAndLineChartSkeleton],
  templateUrl: './admin-dashboard.component.html'
})
export class AdminDashboardComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  isLoggedIn = false;
  currentUser = new User();
  adminDashboardData = new AdminDashboard();
  loading = true;

  // Chart data
  cardData: number[] = [];
  barChartBranchperRegion: any;
  usersPerRegionandAudit: any;
  lineChartData: any;
  recentActivity: RecentActivity[] = [];
  recentActivityies: RecentActivity[] = [];


  // Chart options
  multiAxisOptions: any;
  horizontalOptions: any;

  data: any;

  options: any;

  constructor(
    private storageService: StorageService,
    private adminDashboardService: AdminDashboardService
  ) { }

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();
    this.currentUser = this.storageService.getUser();

    // Load all data in parallel
    forkJoin({
      cardData: this.adminDashboardService.getCardData(),
      pieChart: this.adminDashboardService.getPieChartData(this.currentUser.id as any),
      branchPerRegion: this.adminDashboardService.getBranchPerRegion(),
      recentActivities: this.adminDashboardService.getRecentActivity(this.currentUser.id as any)
    })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: ({
          cardData,
          pieChart,
          branchPerRegion,
          recentActivities
        }) => {
          this.cardData = cardData;
          this.pieChartOption(pieChart);
          this.barChartBranchperRegion = this.buildBranchPerRegion(branchPerRegion);
          this.recentActivityies = recentActivities;
          this.loading = false;


        },
        error: (err) => {
          console.error('Error loading dashboard data:', err);
          this.loading = false;
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /** --------------------------
   *  Chart Builders
   * -------------------------- */
  pieChartOption(pieChartData: any) {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');

    this.data = {
      labels: ['Active', 'Inactive'],
      datasets: [
        {
          data: pieChartData,  // Example data for Active and Inactive
          backgroundColor: [
            documentStyle.getPropertyValue('--p-gray-500'),
            documentStyle.getPropertyValue('--p-orange-500')
          ],
          hoverBackgroundColor: [
            documentStyle.getPropertyValue('--p-gray-400'),
            documentStyle.getPropertyValue('--p-orange-400')
          ]
        }
      ]
    };

    this.options = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor
          }
        }
      }
    };

  }


  private buildBranchPerRegion(data: any) {
    const regions = data.regions;
    const branchCounts = data.branchCounts;
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');

    return {
      labels: regions,
      datasets: [
        {
          label: 'Branch',
          backgroundColor: documentStyle.getPropertyValue('--p-cyan-500'),
          borderColor: documentStyle.getPropertyValue('--p-cyan-500'),

          borderWidth: 1,
          data: branchCounts
        }
      ]
    };
  }




}
