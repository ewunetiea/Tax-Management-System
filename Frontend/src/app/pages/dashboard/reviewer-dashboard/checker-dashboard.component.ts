import { Component } from '@angular/core';
import { LayoutService } from '../../../layout/service/layout.service';
import { SharedUiModule } from '../../../../shared-ui';
import { debounceTime, forkJoin, Subject, Subscription, takeUntil } from 'rxjs';
import { BarAndLineChartSkeleton } from '../../skeleton/bar-and-lign-chart/bar-and-lign-chart';
import { CardSkeleton } from '../../skeleton/card/four-card';
import { PieDougnutPolarSkeleton } from '../../skeleton/dougnut-polar-chart/pie-dougnut-polar';
import { PieDougnutPolarSkeletonDescription } from '../../skeleton/dougnut-polar-chart/polar-pie-dougnut-title';
import { Announcement } from '../../../models/approver/announcement';
import { AnnouncementService } from '../../../service/approver/announcement.service';
import { MessageService } from 'primeng/api';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { HttpErrorResponse } from '@angular/common/http';
import { EditorModule } from 'primeng/editor';
import { ReviewerDashboardService } from 'app/service/reviewer/reviewer-dashboard-service';
import { RecentActivity } from 'app/models/admin/recent-activity';
import { AdminDashboardService } from 'app/service/admin/admin-dashboard-service';
import { StorageService } from 'app/service/sharedService/storage.service';
import { User } from 'app/models/admin/user';
import { ReviewerDashboard } from 'app/models/reviewer/reviewerDashboard';

@Component({
    selector: 'app-checker-dashboard',
    imports: [SharedUiModule, CardSkeleton, BarAndLineChartSkeleton, PieDougnutPolarSkeleton, PieDougnutPolarSkeletonDescription, EditorModule],
    templateUrl: './checker-dashboard.component.html',
    styleUrl: './checker-dashboard.component.scss'
})
export class CheckerDashboardComponent {
    lineData: any;
    barData: any;
    pieData: any;
    polarData: any;
    radarData: any;
    lineOptions: any;
    barOptions: any;
    pieOptions: any;
    polarOptions: any;
    radarOptions: any;
    subscription: Subscription;
    loading = true;
    user = new User();
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
        private reviewerDashboardService: ReviewerDashboardService,
        private adminDashboardService: AdminDashboardService,
        private storageService: StorageService,
        private sanitizer: DomSanitizer) {
        this.subscription = this.layoutService.configUpdate$.pipe(debounceTime(25)).subscribe(() => {
            this.initCharts();
        });
    }

    ngOnInit() {
        this.user = this.storageService.getUser();
        this.branch_id = this.user.branch?.id;
        this.initCharts();
        this.loadReviewerDashboards();
    }

    loadReviewerDashboards(){
            forkJoin({
              taxStatus: this.reviewerDashboardService.getTaxStatusForReviewer(this.branch_id),
              recentActivities: this.adminDashboardService.getRecentActivity(this.user.id as any),
              announcement: this.announcemetService.fetchAnnouncemetForDashBoard(),
              stackedBarTaxesStatusDataData: this.reviewerDashboardService.getStackedBarTaxesStatusData(this.branch_id),
            })
            .pipe(takeUntil(this.destroy$))
            .subscribe({
              next: ({
                taxStatus,
                recentActivities,
                announcement,
                stackedBarTaxesStatusDataData,
              }) => {
                this.taxStatus = taxStatus;
                this.recentActivityies = recentActivities;
                this.announcement = announcement;
                this.stackedBarTaxesStatusData = this.buildStackedBarChart(stackedBarTaxesStatusDataData);
                this.loading = false;
              },
              error: (err) => {
                  this.loading = false;
                console.error('Error loading dashboard data:', err);
              }
            });
    }

    getFileType(base64: string): string {
        if (!base64) return '';
        // Common base64 prefixes
        if (base64.startsWith('/9j/')) return 'image/jpeg'; // JPEG
        if (base64.startsWith('iVBOR')) return 'image/png'; // PNG
        if (base64.startsWith('JVBER')) return 'application/pdf'; // PDF
        if (base64.startsWith('UEsDB')) return 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'; // DOCX
        if (base64.startsWith('0M8R4')) return 'application/msword'; // DOC

        return 'unknown';
    }


    private buildStackedBarChart(data: any[]) {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
    const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

    // Ensure we have 12 months (fill empty ones with zeros)
    const months = Array.from({ length: 12 }, (_, i) => i + 1);

    const pendingData = months.map(m => data.find(d => d.month === m)?.Pending || 0);
    const reviewedData = months.map(m => data.find(d => d.month === m)?.Reviewed || 0);
    const approvedData = months.map(m => data.find(d => d.month === m)?.Approved || 0);
    const rejectedData = months.map(m => data.find(d => d.month === m)?.Rejected || 0);

    this.stackedBarTaxesStatusData = {
        labels: [
            'January', 'February', 'March', 'April', 'May', 'June',
            'July', 'August', 'September', 'October', 'November', 'December'
        ],
        datasets: [
            {
                type: 'bar',
                label: 'Pending',
                backgroundColor: documentStyle.getPropertyValue('--p-cyan-500'),
                data: pendingData
            },
            {
                type: 'bar',
                label: 'Reviewed',
                backgroundColor: documentStyle.getPropertyValue('--p-gray-500'),
                data: reviewedData
            },
            {
                type: 'bar',
                label: 'Approved',
                backgroundColor: documentStyle.getPropertyValue('--p-orange-500'),
                data: approvedData
            },
            {
                type: 'bar',
                label: 'Rejected',
                backgroundColor: documentStyle.getPropertyValue('--p-pink-500'),
                data: rejectedData
            }
        ]
    };

    this.stackedBarOptions = {
        maintainAspectRatio: false,
        aspectRatio: 0.8,
        plugins: {
            tooltip: {
                mode: 'index',
                intersect: false
            },
            legend: {
                labels: {
                    color: textColor
                }
            }
        },
        scales: {
            x: {
                stacked: true,
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder,
                    drawBorder: false
                }
            },
            y: {
                stacked: true,
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder,
                    drawBorder: false
                }
            }
        }
    };
}












    

    initCharts() {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');
        const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
        const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

        // ---- Bar Chart ----
        this.barData = {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
            datasets: [
                {
                    label: 'My First dataset',
                    backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
                    borderColor: documentStyle.getPropertyValue('--p-primary-500'),
                    data: [65, 59, 80, 81, 56, 55, 40]
                },
                {
                    label: 'My Second dataset',
                    backgroundColor: documentStyle.getPropertyValue('--p-primary-200'),
                    borderColor: documentStyle.getPropertyValue('--p-primary-200'),
                    data: [28, 48, 40, 19, 86, 27, 90]
                }
            ]
        };

        this.barOptions = {
            maintainAspectRatio: false,
            aspectRatio: 0.8,
            plugins: {
                legend: {
                    labels: { color: textColor }
                }
            },
            scales: {
                x: {
                    ticks: {
                        color: textColorSecondary,
                        font: { weight: 500 }
                    },
                    grid: { display: false, drawBorder: false }
                },
                y: {
                    ticks: { color: textColorSecondary },
                    grid: { color: surfaceBorder, drawBorder: false }
                }
            }
        };

        // ---- Pie Chart ----
        this.pieData = {
            labels: ['A', 'B', 'C'],
            datasets: [
                {
                    data: [540, 325, 702],
                    backgroundColor: [
                        documentStyle.getPropertyValue('--p-indigo-500'),
                        documentStyle.getPropertyValue('--p-purple-500'),
                        documentStyle.getPropertyValue('--p-teal-500')
                    ],
                    hoverBackgroundColor: [
                        documentStyle.getPropertyValue('--p-indigo-400'),
                        documentStyle.getPropertyValue('--p-purple-400'),
                        documentStyle.getPropertyValue('--p-teal-400')
                    ]
                }
            ]
        };

        this.pieOptions = {
            plugins: {
                legend: {
                    labels: { usePointStyle: true, color: textColor }
                }
            }
        };

        // ---- Line Chart ----
        this.lineData = {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
            datasets: [
                {
                    label: 'First Dataset',
                    data: [65, 59, 80, 81, 56, 55, 40],
                    fill: false,
                    backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
                    borderColor: documentStyle.getPropertyValue('--p-primary-500'),
                    tension: 0.4
                },
                {
                    label: 'Second Dataset',
                    data: [28, 48, 40, 19, 86, 27, 90],
                    fill: false,
                    backgroundColor: documentStyle.getPropertyValue('--p-primary-200'),
                    borderColor: documentStyle.getPropertyValue('--p-primary-200'),
                    tension: 0.4
                }
            ]
        };

        this.lineOptions = {
            maintainAspectRatio: false,
            aspectRatio: 0.8,
            plugins: {
                legend: { labels: { color: textColor } }
            },
            scales: {
                x: {
                    ticks: { color: textColorSecondary },
                    grid: { color: surfaceBorder, drawBorder: false }
                },
                y: {
                    ticks: { color: textColorSecondary },
                    grid: { color: surfaceBorder, drawBorder: false }
                }
            }
        };

        // ---- Polar Area Chart ----
        this.polarData = {
            datasets: [
                {
                    data: [11, 16, 7, 3],
                    backgroundColor: [
                        documentStyle.getPropertyValue('--p-indigo-500'),
                        documentStyle.getPropertyValue('--p-purple-500'),
                        documentStyle.getPropertyValue('--p-teal-500'),
                        documentStyle.getPropertyValue('--p-orange-500')
                    ],
                    label: 'My dataset'
                }
            ],
            labels: ['Indigo', 'Purple', 'Teal', 'Orange']
        };

        this.polarOptions = {
            plugins: {
                legend: { labels: { color: textColor } }
            },
            scales: {
                r: {
                    grid: { color: surfaceBorder },
                    ticks: { display: false, color: textColorSecondary }
                }
            }
        };

        // ---- Radar Chart ----
        this.radarData = {
            labels: ['Eating', 'Drinking', 'Sleeping', 'Designing', 'Coding', 'Cycling', 'Running'],
            datasets: [
                {
                    label: 'My First dataset',
                    borderColor: documentStyle.getPropertyValue('--p-indigo-400'),
                    pointBackgroundColor: documentStyle.getPropertyValue('--p-indigo-400'),
                    pointBorderColor: documentStyle.getPropertyValue('--p-indigo-400'),
                    pointHoverBackgroundColor: textColor,
                    pointHoverBorderColor: documentStyle.getPropertyValue('--p-indigo-400'),
                    data: [65, 59, 90, 81, 56, 55, 40]
                },
                {
                    label: 'My Second dataset',
                    borderColor: documentStyle.getPropertyValue('--p-purple-400'),
                    pointBackgroundColor: documentStyle.getPropertyValue('--p-purple-400'),
                    pointBorderColor: documentStyle.getPropertyValue('--p-purple-400'),
                    pointHoverBackgroundColor: textColor,
                    pointHoverBorderColor: documentStyle.getPropertyValue('--p-purple-400'),
                    data: [28, 48, 40, 19, 96, 27, 100]
                }
            ]
        };

        this.radarOptions = {
            plugins: {
                legend: { labels: { color: textColor } }
            },
            scales: {
                r: {
                    pointLabels: { color: textColor },
                    grid: { color: surfaceBorder }
                }
            }
        };
    }

    ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

}
