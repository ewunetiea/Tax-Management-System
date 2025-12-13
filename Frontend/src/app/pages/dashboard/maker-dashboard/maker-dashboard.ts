import { ChangeDetectorRef, Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { forkJoin, Subject, takeUntil } from 'rxjs';

import { CardSkeleton } from "../../skeleton/card/four-card";
import { SharedUiModule } from '../../../../shared-ui';
import { BarAndLineChartSkeleton } from "../../skeleton/bar-and-lign-chart/bar-and-lign-chart";
import { AnnouncementService } from '../../../service/approver/announcement.service';
import { Announcement } from '../../../models/approver/announcement';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { HttpErrorResponse } from '@angular/common/http';
import { EditorModule } from 'primeng/editor';
import { User } from '../../../models/admin/user';
import { MakerDashboardData } from '../../../models/maker/dashboard/makerDashboardPayload';
import { MakerDashboardService } from '../../../service/maker/maker-dashboaed.service';
import { of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { StorageService } from 'app/service/sharedService/storage.service';
import { TimelineModule } from 'primeng/timeline';

@Component({
    selector: 'app-maker-dashboard',
    standalone: true,
    imports: [SharedUiModule, CardSkeleton, BarAndLineChartSkeleton, EditorModule, TimelineModule],
    templateUrl: './maker-dashboard.component.html',
      styleUrl: './maker-dashboard.component.scss'



})
export class MakerDashboard implements OnInit {
today = new Date();      // For signature date
    private destroy$ = new Subject<void>();
    showAnnouncement: boolean = true; // default to Announcement

    isLoggedIn = false;
    currentUser = new User();
    makerDashboardData = new MakerDashboardData();
    loading = true;

    // Chart data
    cardData: number[] = [];
    radarData: any;


    polarData: any;
    polarOptions: any;
    barChartData: any;

    barOptions: any;
    radarOptions: any;
    barChartOptions: any;


    announcement: Announcement = new Announcement();

    draftedData: any
    reviewedData: any
    approvedData: any

    roles: String = ''
    announcmentavailable: boolean = false

    constructor(
        private announcemetService: AnnouncementService,
        private sanitizer: DomSanitizer,
        private makerDashboardService: MakerDashboardService,
        private cd: ChangeDetectorRef,
        private storageService: StorageService

    ) { }

    ngOnInit() {

        setTimeout(() => {
            this.loading = false;
        }, 1000); // 1000 ms = 1 seconds



        forkJoin({
            cardData: this.makerDashboardService.getCardData(this.storageService.getUser().id).pipe(
                catchError(err => { console.error(err); return of([]); })
            ),
            barData: this.makerDashboardService.getBarChartData(this.storageService.getUser().id).pipe(
                catchError(err => { console.error(err); return of([]); })
            ),
            // polarChartData: this.makerDashboardService.polarChartData(this.storageService.getUser().id).pipe(
            //     catchError(err => { console.error(err); return of({ drafted: 0, waiting: 0, reviewed: 0, approved: 0 }); })
            // ),
            // radarData: this.makerDashboardService.getRadarAgeData(this.storageService.getUser().id).pipe(
            //     catchError(err => { console.error(err); return of([]); })
            // )

        })
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: ({ cardData, barData,  }) => {


                    // Card and bar chart
                    this.cardData = cardData;
                    this.draftedData = barData.map(row => row[0]);
                    this.reviewedData = barData.map(row => row[1]);
                    this.approvedData = barData.map(row => row[2]);
                    this.initBarChart(this.draftedData, this.reviewedData, this.approvedData);

                    // Polar chart
                    // this.initPolarChart(polarChartData);

                    // this.radarData = radarData;

                    this.fetchRadarData(this.radarData);

                },
                error: err => console.error('forkJoin error', err)
            });


        this.loadAnnouncements();


    }

    loadAnnouncements() {
                    

        this.announcemetService.fetchAnnouncemetForDashBoard("ROLE_MAKER").subscribe(

            (announcement: any) => {   // directly the single object
                if (announcement) {


                    
                    this.announcmentavailable = true
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

                    if (this.announcement != null) {

                        this.showAnnouncement = true;
                    }

                    else {
                        this.showAnnouncement = false;

                    }


                }
            },
            (error: HttpErrorResponse) => {
                
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


    fetchRadarData(radarData: any[]) {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');
        const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

        // Assign to class property if needed
        this.radarData = radarData;

        if (!radarData || radarData.length === 0) {
            // Fallback: no data
            this.radarData = {
                labels: ['Drafted', 'Waiting for Review', 'Reviewed', 'Reviewer Rejected', 'Approved', 'Approver Rejected'],
                datasets: []
            };
            return;
        }

        const labels = ['Drafted', 'Waiting for Review', 'Reviewed', 'Reviewer Rejected', 'Approved', 'Approver Rejected'];

        // Map backend data to datasets
        const datasets = radarData.map((yearData, index) => ({
            label: `Year ${yearData.year}`,
            data: [
                yearData.drafted,
                yearData.waitingForReview,
                yearData.reviewed,
                yearData.reviewerRejected,
                yearData.approved,
                yearData.approverRejected
            ],
            borderColor: index === 0
                ? documentStyle.getPropertyValue('--p-indigo-400')
                : documentStyle.getPropertyValue('--p-purple-400'),
            backgroundColor: (index === 0
                ? documentStyle.getPropertyValue('--p-indigo-400')
                : documentStyle.getPropertyValue('--p-purple-400')) + '33',
            pointBackgroundColor: index === 0
                ? documentStyle.getPropertyValue('--p-indigo-400')
                : documentStyle.getPropertyValue('--p-purple-400'),
            pointBorderColor: index === 0
                ? documentStyle.getPropertyValue('--p-indigo-400')
                : documentStyle.getPropertyValue('--p-purple-400'),
            pointHoverBackgroundColor: textColor,
            pointHoverBorderColor: index === 0
                ? documentStyle.getPropertyValue('--p-indigo-400')
                : documentStyle.getPropertyValue('--p-purple-400')
        }));

        // Assign radar chart data
        this.radarData = { labels, datasets };

        // Radar chart options
        this.radarOptions = {
            plugins: {
                legend: { labels: { color: textColor } },
                title: {
                    display: true,
                    text: 'Status Comparison: Current Year vs Previous Year',
                    color: textColor
                }
            },
            scales: {
                r: {
                    pointLabels: { color: textColor },
                    grid: { color: surfaceBorder },
                    angleLines: { color: surfaceBorder },
                    suggestedMin: 0,
                    suggestedMax: Math.max(...datasets.flatMap(d => d.data)) + 10 // dynamic max
                }
            }
        };
    }


    public initPolarChart(polarDataObj?: { drafted: number, waiting: number, reviewed: number, approved: number }) {


        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');
        const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
        const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

        const data = polarDataObj
            ? [
                polarDataObj.drafted ?? 0,
                polarDataObj.waiting ?? 0,
                polarDataObj.reviewed ?? 0,
                polarDataObj.approved ?? 0
            ]
            : [11, 16, 7, 3];

        this.polarData = {
            datasets: [
                {
                    data: data,
                    backgroundColor: [
                        documentStyle.getPropertyValue('--p-indigo-500'),
                        documentStyle.getPropertyValue('--p-purple-500'),
                        documentStyle.getPropertyValue('--p-teal-500'),
                        documentStyle.getPropertyValue('--p-orange-500')
                    ],
                    label: 'Tax Status'
                }
            ],
            labels: ['Drafted', 'Waiting', 'Reviewed', 'Approved']
        };

        this.polarOptions = {
            scales: {
                r: {
                    min: 0,
                    grid: { color: surfaceBorder },
                    ticks: { display: true, color: textColorSecondary, beginAtZero: true }
                }
            },
            plugins: {
                legend: { labels: { color: textColor } },
                tooltip: {
                    callbacks: {
                        label: (tooltipItem: { label: string; raw: string; }) => {
                            return tooltipItem.label + ': ' + tooltipItem.raw;
                        }
                    }
                }
            }
        };

        this.cd.markForCheck();
    }



    public initBarChart(
        draftedData: number[],
        waitingData: number[],
        approvedData: number[]
    ) {



        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--p-text-color');
        const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
        const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

        // Labels for months
        const labels = [
            'January', 'February', 'March', 'April', 'May', 'June',
            'July', 'August', 'September', 'October', 'November', 'December'
        ];

        this.barChartData = {
            labels: labels,
            datasets: [
                {
                    type: 'bar',
                    label: 'Waiting for Review',
                    backgroundColor: documentStyle.getPropertyValue('--p-cyan-500'),
                    data: draftedData
                },
                {
                    type: 'bar',
                    label: 'Reviewed',
                    backgroundColor: documentStyle.getPropertyValue('--p-gray-500'),
                    data: waitingData
                },
                {
                    type: 'bar',
                    label: 'Approved',
                    backgroundColor: documentStyle.getPropertyValue('--p-orange-500'),
                    data: approvedData
                }
            ]
        };

        this.barChartOptions = {
            maintainAspectRatio: false,
            aspectRatio: 0.8,
            plugins: {
                tooltip: { mode: 'index', intersect: false },
                legend: { labels: { color: textColor } }
            },
            scales: {
                x: {
                    stacked: true,
                    ticks: { color: textColorSecondary },
                    grid: { color: surfaceBorder, drawBorder: false }
                },
                y: {
                    stacked: true,
                    ticks: { color: textColorSecondary },
                    grid: { color: surfaceBorder, drawBorder: false }
                }
            }
        };

        this.cd.markForCheck();
    }


}
