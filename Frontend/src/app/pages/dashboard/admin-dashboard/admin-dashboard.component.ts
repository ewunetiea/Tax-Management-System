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
  polarChartUsersStatusData: any;
  barChartRolePerAudits: any;
  horizontalBarChartData: any;
  radarData: any;
  usersPerRegionandAudit: any;
  lineChartData: any;
  recentActivity: RecentActivity[] = [];
    recentActivityies: RecentActivity[] = [];


  // Chart options
  multiAxisOptions: any;
  horizontalOptions: any;
  polarChartOptions: any;
  chartOptionsRevenues: any;
  chartOptions: any;
  basicOptions: any;

  // Breadcrumb
  breadcrumbText = 'Admin Dashboard';
  items: MenuItem[] = [];
  home: MenuItem | undefined;

  constructor(
    private storageService: StorageService,
    private adminDashboardService: AdminDashboardService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();
    this.currentUser = this.storageService.getUser();
    this.home = { icon: 'pi pi-home', routerLink: '/' };
    this.items = [{ label: this.breadcrumbText }];

    this.initChartOptions();

    // Load all data in parallel
    forkJoin({
      cardData: this.adminDashboardService.getCardData(),
      polarData: this.adminDashboardService.getPolarData(this.currentUser.id as any),
      barData: this.adminDashboardService.getBarChartData(),
      horizontalData: this.adminDashboardService.getHorizontalBarChartData(),
      radarData: this.adminDashboardService.getRadarAgeData(),
      lineData: this.adminDashboardService.getLineChartData(),
      recentActivities: this.adminDashboardService.getRecentActivity(this.currentUser.id as any)
    })
    .pipe(takeUntil(this.destroy$))
    .subscribe({
      next: ({
        cardData,
        polarData,
        barData,
        horizontalData,
        radarData,
        lineData,
        recentActivities
      }) => {
        this.cardData = cardData;
        this.polarChartUsersStatusData = this.buildPolarChart(polarData);
        this.barChartRolePerAudits = this.buildBarChart(barData);
        this.horizontalBarChartData = this.buildHorizontalChart(horizontalData);
        this.radarData = this.buildRadarChart(radarData);
        this.usersPerRegionandAudit = this.buildLineChart(lineData as any);
        this.recentActivityies = recentActivities;
        this.loading = false;
                console.error('data come:');


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
  private buildPolarChart(data: number[]) {
    const style = getComputedStyle(document.documentElement);
    return {
      datasets: [{
        data,
        backgroundColor: [
          style.getPropertyValue('--p-cyan-500'),
          style.getPropertyValue('--p-orange-500'),
          style.getPropertyValue('--p-pink-500'),
          style.getPropertyValue('--p-gray-500')
        ],
        hoverBackgroundColor: [
          style.getPropertyValue('--p-cyan-400'),
          style.getPropertyValue('--p-orange-400'),
          style.getPropertyValue('--p-pink-400'),
          style.getPropertyValue('--p-gray-400')
        ],
        label: 'System Users Status'
      }],
      labels: ['Active', 'Inactive', 'Account Locked', 'Credential Expired']
    };
  }

  private buildBarChart(data: number[]) {
    const approver: number[] = [];
    const auditor: number[] = [];
    const followup: number[] = [];
    const reviewer: number[] = [];

    for (let i = 0; i < data.length;) {
      approver.push(data[i++]);
      auditor.push(data[i++]);
      followup.push(data[i++]);
      reviewer.push(data[i++]);
    }

    return {
      labels: ['IS', 'MGT', 'INS'],
      datasets: [
        { type: 'bar', label: 'Auditor', backgroundColor: '#48C9B0', data: auditor },
        { type: 'bar', label: 'Reviewer', backgroundColor: '#F8C471', data: reviewer },
        { type: 'bar', label: 'Approver', backgroundColor: '#28B463', data: approver },
        { type: 'bar', label: 'Followup', backgroundColor: '#FA8072', data: followup }
      ]
    };
  }

  private buildHorizontalChart(data: number[]) {
    return {
      labels: ['Auditor', 'Reviewer', 'Division Compiler', 'Approver', 'Branch Manager', 'Regional Director'],
      datasets: [{ label: 'Financial Audit Users per Roles', backgroundColor: '#34568B', data }]
    };
  }

  private buildRadarChart(data: number[]) {
    const active: number[] = [];
    const inactive: number[] = [];
    for (let i = 0; i < data.length;) {
      active.push(data[i++]);
      inactive.push(data[i++]);
    }

    return {
      labels: ['IS', 'MGT', 'BFA', 'INS'],
      datasets: [
        { label: 'Active', backgroundColor: 'rgba(26,188,156,0.2)', borderColor: 'rgba(26,188,156,1)', data: active },
        { label: 'Inactive', backgroundColor: 'rgba(26,82,118,0.2)', borderColor: 'rgba(26,82,118,1)', data: inactive }
      ]
    };
  }

  private buildLineChart(data: Record<string, number[]>) {
    const regions = Object.keys(data);
    const audits = Object.values(data);

    const IS: number[] = [], MGT: number[] = [], INS: number[] = [], BFA: number[] = [];
    audits.forEach(counts => {
      IS.push(counts[0]);
      MGT.push(counts[1]);
      INS.push(counts[2]);
      BFA.push(counts[3]);
    });

    return {
      labels: regions,
      datasets: [
        { label: 'IS', data: IS, borderColor: '#95A5A6', fill: false },
        { label: 'MGT', data: MGT, borderColor: '#36A2EB', fill: false },
        { label: 'INS', data: INS, borderColor: '#48C9B0', fill: false },
        { label: 'BFA', data: BFA, borderColor: '#2874A6', fill: false }
      ]
    };
  }

  /** --------------------------
   *  Chart Options Initialization
   * -------------------------- */
  private initChartOptions() {
    const style = getComputedStyle(document.documentElement);
    const textColor = style.getPropertyValue('--text-color');
    const borderColor = style.getPropertyValue('--surface-border');
    const textMutedColor = style.getPropertyValue('--text-color-secondary');

    this.multiAxisOptions = {
      plugins: { legend: { labels: { color: textColor } } },
      scales: {
        x: { ticks: { color: textMutedColor }, grid: { color: borderColor } },
        y: { ticks: { color: textMutedColor }, grid: { color: borderColor } }
      }
    };

    this.horizontalOptions = {
      indexAxis: 'y',
      plugins: { legend: { labels: { color: textColor } } },
      scales: {
        x: { ticks: { color: textMutedColor }, grid: { color: borderColor } },
        y: { ticks: { color: textMutedColor }, grid: { color: borderColor } }
      }
    };

    this.polarChartOptions = { maintainAspectRatio: false, aspectRatio: 0.8 };
    this.chartOptionsRevenues = { maintainAspectRatio: false, aspectRatio: 0.8 };
    this.basicOptions = { plugins: { legend: { labels: { color: textColor } } } };
  }
}
