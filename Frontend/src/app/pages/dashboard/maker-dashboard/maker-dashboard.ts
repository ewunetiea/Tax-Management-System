import { Component, OnInit } from '@angular/core';
import { debounceTime, forkJoin, Subject, Subscription, takeUntil } from 'rxjs';
import { LayoutService } from '../../../layout/service/layout.service';

import { CardSkeleton } from "../../skeleton/card/four-card";
import { SharedUiModule } from '../../../../shared-ui';
import { BarAndLineChartSkeleton } from "../../skeleton/bar-and-lign-chart/bar-and-lign-chart";
import { PieDougnutPolarSkeleton } from "../../skeleton/dougnut-polar-chart/pie-dougnut-polar";
import { PieDougnutPolarSkeletonDescription } from "../../skeleton/dougnut-polar-chart/polar-pie-dougnut-title";
import { AnnouncementService } from '../../../service/approver/announcement.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Announcement } from '../../../models/approver/announcement';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { HttpErrorResponse } from '@angular/common/http';
import { EditorModule } from 'primeng/editor';
import { User } from '../../../models/admin/user';
import { MakerDashboardData } from '../../../models/maker/dashboard/makerDashboardPayload';
import { RecentActivity } from '../../../models/admin/recent-activity';
import { MakerDashboardService } from '../../../service/maker/maker-dashboaed.service';


@Component({
    selector: 'app-maker-dashboard',
    standalone: true,
    imports: [SharedUiModule, CardSkeleton, BarAndLineChartSkeleton, PieDougnutPolarSkeleton, PieDougnutPolarSkeletonDescription,EditorModule],
    templateUrl: './maker-dashboard.component.html',
})
export class MakerDashboard  implements OnInit{

 private destroy$ = new Subject<void>();

  isLoggedIn = false;
  currentUser = new User();
  makerDashboardData = new MakerDashboardData();
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

    lineData: any;
    barData: any;
    pieData: any;
    polarData: any;

    lineOptions: any;
    barOptions: any;
    pieOptions: any;
    polarOptions: any;
    radarOptions: any;

    subscription: Subscription;
  announcement: Announcement = new Announcement();

    constructor(private layoutService: LayoutService,
         private announcemetService: AnnouncementService,
            private messageService: MessageService,
            private confirmationService: ConfirmationService,
                private sanitizer: DomSanitizer,
              private   makerDashboardService: MakerDashboardService
            
    ) {
        this.subscription = this.layoutService.configUpdate$
            .pipe(debounceTime(25))
            .subscribe(() => {
                this.initCharts();
            });
    }

    ngOnInit() {

         setTimeout(() => {
    this.loading = false;
  }, 1000); // 1000 ms = 1 seconds
        this.initCharts();



   forkJoin({
      cardData: this.makerDashboardService.getCardData(),
      polarData: this.makerDashboardService.getPolarData(this.currentUser.id as any),
      barData: this.makerDashboardService.getBarChartData(),
      horizontalData: this.makerDashboardService.getHorizontalBarChartData(),
      radarData: this.makerDashboardService.getRadarAgeData(),
      lineData: this.makerDashboardService.getLineChartData(),
      recentActivities: this.makerDashboardService.getRecentActivity(this.currentUser.id as any)
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
        // this.polarChartUsersStatusData = this.buildPolarChart(polarData);
        // this.barChartRolePerAudits = this.buildBarChart(barData);
        // this.horizontalBarChartData = this.buildHorizontalChart(horizontalData);
        // this.radarData = this.buildRadarChart(radarData);
        // this.usersPerRegionandAudit = this.buildLineChart(lineData as any);
        this.recentActivityies = recentActivities;
        this.loading = false;
                console.error('data come:');


      },
      error: (err) => {
        console.error('Error loading dashboard data:', err);
        this.loading = false;
      }
    });

    this.loadAnnouncements();


    }

   loadAnnouncements() {
  this.announcemetService.fetchAnnouncemetForDashBoard().subscribe(
    (announcement: any) => {   // directly the single object
      if (announcement) {
        // Detect file type from base64
        const fileType = this.getFileType(announcement.image);
        announcement.fileType = fileType;

        // Prepare PDF blob URL if PDF
        if (fileType === 'application/pdf') {
          const byteCharacters = atob(announcement.image);
          const byteNumbers = new Array(byteCharacters.length);
          for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
          }
          const byteArray = new Uint8Array(byteNumbers);
          const blob = new Blob([byteArray], { type: 'application/pdf' });

          const url = URL.createObjectURL(blob);
          announcement.pdfUrl = url;
          announcement.safePdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url) as SafeResourceUrl;
        }

        this.announcement = announcement; // directly assign the object
      }
    },
    (error: HttpErrorResponse) => {
      this.messageService.add({
        severity: 'error',
        summary:
          error.status === 401
            ? 'You are not permitted to perform this action!'
            : 'Something went wrong while fetching announcements!',
        detail: '',
      });
    }
  );
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
