import { Component } from '@angular/core';
import { User } from '../../../models/admin/user';
import { Subscription } from 'rxjs';
import { AdminDashboard } from '../../../models/admin/admin-dashboard';
import { AppConfig } from '../../../models/admin/appconfig';
import { MenuItem, MessageService } from 'primeng/api';
import { StorageService } from '../../service/admin/storage.service';
import { UserService } from '../../service/admin/user.service';
import { SharedUiModule } from '../../../../shared-ui';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    standalone: true,
    selector: 'app-admin-dashboard',
    imports: [SharedUiModule],
    templateUrl: './admin-dashboard.component.html'
})
export class AdminDashboardComponent {
    isLoggedIn = false;
    currentUser = new User();
    adminDashboardData = new AdminDashboard();
    basicData: any;
    multiAxisData: any;
    usersPerRegionandAudit: any;
    multiAxisOptions: any;
    lineStylesData: any;
    basicOptions: any;
    subscription?: Subscription;
    config?: AppConfig;
    chartOptions: any;
    stackedData: any;
    stackedOptions: any;
    horizontalOptions: any;

    //// bar chart data
    basicDataBarChart: any;
    basicOptionsBarChart: any;
    multiAxisDataBarChart: any;
    chartOptionsBarChart: any;
    multiAxisOptionsBarChart: any;
    stackedDataBarChart: any;
    stackedbarChartRolePerAudits: any;
    barChartRolePerAudits: any;
    doughnut_data: any;
    chartOptionsDoughnut: any;
    horizontal_bar_chart_data: any;
    stackedOptionsBarChart: any;
    horizontalOptionsBarChart: any;
    subscriptionBarChart?: Subscription;
    configBarChart?: AppConfig;

    /////   Dougnnut////////
    datadoughnut: any;
    chartOptionsdoughnut: any;
    subscriptiondoughnut?: Subscription;
    configdoughnut?: AppConfig;

    ///// Pie Chart //////
    polarChartData: any;
    polarChartUsersStatusData: any;
    polarChartOptions: any;
    chartOptionsPie: any;
    subscriptionPie?: Subscription;
    configPie?: AppConfig;
    radar_data: any;
    line_chart_data: any;
    loading = false;
    live = true;
    breadcrumbText: string = 'Admin Dashboard';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;

    constructor(
        private messageService: MessageService,
        private storageService: StorageService,
        private adminDashboardService: UserService
    ) {}

    ngOnInit() {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.isLoggedIn = this.storageService.isLoggedIn();
        if (this.isLoggedIn) {
            setTimeout(() => {
                this.loading = true;
            }, 4000);
            this.getDashBoardData();
        }

        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');
        const borderColor = documentStyle.getPropertyValue('--surface-border');
        const textMutedColor = documentStyle.getPropertyValue('--text-color-secondary');

        this.multiAxisOptions = {
            stacked: false,
            plugins: {
                legend: {
                    labels: {
                        color: '#495057'
                    }
                }
            },
            scales: {
                x: {
                    ticks: {
                        color: '#495057'
                    },
                    grid: {
                        color: '#ebedef'
                    }
                },
                y: {
                    type: 'linear',
                    display: true,
                    position: 'left',
                    ticks: {
                        color: '#495057'
                    },
                    grid: {
                        color: '#ebedef'
                    }
                },
                y1: {
                    type: 'linear',
                    display: true,
                    position: 'right',
                    ticks: {
                        color: '#495057'
                    },
                    grid: {
                        drawOnChartArea: false,
                        color: '#ebedef'
                    }
                }
            }
        };

        this.multiAxisOptionsBarChart = {
            plugins: {
                legend: {
                    labels: {
                        color: '#495057'
                    }
                },
                tooltips: {
                    mode: 'index',
                    intersect: true
                }
            },
            scales: {
                x: {
                    ticks: {
                        color: '#495057'
                    },
                    grid: {
                        color: '#ebedef'
                    }
                },
                y: {
                    type: 'linear',
                    display: true,
                    position: 'left',
                    ticks: {
                        min: 0,
                        max: 100,
                        color: '#495057'
                    },
                    grid: {
                        color: '#ebedef'
                    }
                },
                y1: {
                    type: 'linear',
                    display: true,
                    position: 'right',
                    grid: {
                        drawOnChartArea: false,
                        color: '#ebedef'
                    },
                    ticks: {
                        min: 0,
                        max: 100,
                        color: '#495057'
                    }
                }
            }
        };

        this.horizontalOptions = {
            indexAxis: 'y',
            plugins: {
                legend: {
                    labels: {
                        color: '#495057'
                    }
                }
            },
            scales: {
                x: {
                    ticks: {
                        color: '#495057'
                    },
                    grid: {
                        color: '#ebedef'
                    }
                },
                y: {
                    ticks: {
                        color: '#495057'
                    },
                    grid: {
                        color: '#ebedef'
                    }
                }
            }
        };

        this.stackedOptionsBarChart = {
            tooltips: {
                mode: 'index',
                intersect: false
            },
            responsive: true,
            scales: {
                xAxes: [
                    {
                        stacked: true
                    }
                ],
                yAxes: [
                    {
                        stacked: true
                    }
                ]
            }
        };

        this.polarChartOptions = {
            maintainAspectRatio: false,
            aspectRatio: 0.8,
            plugins: {
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
                        color: textMutedColor
                    },
                    grid: {
                        color: 'transparent',
                        borderColor: 'transparent'
                    }
                },
                y: {
                    stacked: true,
                    ticks: {
                        color: textMutedColor
                    },
                    grid: {
                        color: borderColor,
                        borderColor: 'transparent',
                        drawTicks: false
                    }
                }
            }
        };
    }

    getDashBoardData() {
        this.currentUser.id = this.storageService.getUser().id;
        this.currentUser.category = this.storageService.getUser().category;
        this.adminDashboardService.drawBarChartUsersPerRegion(this.currentUser).subscribe(
            (response) => {
                this.loading = false;
                this.adminDashboardData = response;
                this.getUsersPerAuditandRegion(this.adminDashboardData.directorates as any, this.adminDashboardData.line_chart_data as any);
                this.getUsersPerRoleandAuditBarChart(this.adminDashboardData.bar_chart_data as any, this.adminDashboardData.roles_length_IS_MGT_INS as any);

                this.getUsersPerRoleandAuditHorizontalBarChart(this.adminDashboardData.horizontal_bar_chart_data as any, this.adminDashboardData.roles_name_BFA as any);
                this.getRadarData(this.adminDashboardData.age_data as any);
            },
            (error) => (error: HttpErrorResponse) => {
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching findings!',
                    detail: ''
                });
            }
        );
    }

    getUsersPerAuditandRegion(regions: String[], user_region_audit: Number[]) {
        let IS = [],
            MGT = [],
            INS = [],
            BFA = [];

        for (let index = -1; index < user_region_audit.length; ) {
            IS.push(user_region_audit[++index]);
            MGT.push(user_region_audit[++index]);
            INS.push(user_region_audit[++index]);
            BFA.push(user_region_audit[++index]);
        }

        this.usersPerRegionandAudit = {
            labels: regions,
            datasets: [
                {
                    label: 'IS',
                    data: IS,
                    fill: false,
                    borderDash: [5, 5],
                    tension: 0.4,
                    backgroundColor: '#95A5A6',
                    borderColor: '#95A5A6'
                },
                {
                    label: 'MGT',
                    data: MGT,
                    fill: false,
                    borderDash: [5, 5],
                    tension: 0.4,
                    backgroundColor: '#36A2EB',
                    borderColor: '#36A2EB'
                },
                {
                    label: 'INS',
                    data: INS,
                    fill: false,
                    borderDash: [5, 5],
                    tension: 0.4,
                    backgroundColor: '#48C9B0',
                    borderColor: '#48C9B0'
                },
                {
                    label: 'BFA',
                    data: BFA,
                    fill: false,
                    borderDash: [5, 5],
                    tension: 0.4,
                    backgroundColor: '#2874A6',
                    borderColor: '#2874A6'
                }
            ]
        };
    }

    getFindingStatusPerMonthStackedBarChart(stacked_bar_chart_data: Number[]) {
        let total_responded_audits = [],
            total_approved_audits = [],
            total_approver_rejected_audits = [];

        for (let index = -1; index < stacked_bar_chart_data.length; ) {
            total_approved_audits.push(stacked_bar_chart_data[++index]);
            total_approver_rejected_audits.push(stacked_bar_chart_data[++index]);
            total_responded_audits.push(stacked_bar_chart_data[++index]);
        }
        this.stackedbarChartRolePerAudits = {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [
                {
                    type: 'bar',
                    label: 'Approved',
                    backgroundColor: '#48C9B0',
                    data: total_approved_audits
                },
                {
                    type: 'bar',
                    label: 'Approver Rejected',
                    backgroundColor: '#F8C471',
                    data: total_approver_rejected_audits
                },
                {
                    type: 'bar',
                    label: 'Responded',
                    backgroundColor: '#2874A6',
                    data: total_responded_audits
                }
            ]
        };
    }

    getUsersPerRoleandAuditHorizontalBarChart(horizontal_bar_chart_data: Number[], roles_name_BFA: String[]) {
        let auditor = [],
            reviewer = [],
            division_compiler = [],
            approver = [],
            branchM = [],
            regionalD = [];
        this.horizontal_bar_chart_data = {
            labels: ['Auditor', 'Reviewer', 'Division Compiler', 'Approver', 'Branch Manager', 'Regional Director'],
            datasets: [
                {
                    label: ' Financial Audit Users per Roles',
                    backgroundColor: '#34568B',
                    data: horizontal_bar_chart_data
                }
            ]
        };
    }

    getPolarChartData(polar_data: Number[]) {
        this.polarChartUsersStatusData = {
            datasets: [
                {
                    data: polar_data,
                    backgroundColor: ['#28B463', '#F8C471', '#FA8072', '#2874A6'],
                    hoverBackgroundColor: ['#28B463', '#154360', '#F5B041', '#F5B041'],
                    label: 'System Users Status'
                }
            ],
            labels: ['Active', 'Inactive', 'Account Locked', 'Credential Expired']
        };
    }

    getDoughnutData(doughnut_data: Number[]) {
        this.doughnut_data = {
            labels: ['Followup Rejected', 'Partialy Rectified', 'Approved', 'Responded', 'Rectified'],
            datasets: [
                {
                    data: doughnut_data,
                    backgroundColor: ['#F5A9BC', '#C8FE2E', '#48C9B0', '#2874A6', '#28B463'],
                    hoverBackgroundColor: ['#F5A9BC', '#154360', '#F5B041', '#154360', '#F5B041']
                }
            ]
        };
    }

    getUsersPerRoleandAuditBarChart(bar_chart_data: Number[], roles_length_IS_MGT_INS: Number[]) {
        let approver = [],
            auditor = [],
            followup = [],
            reviewer = [];

        for (let index = -1; index < bar_chart_data.length; ) {
            approver.push(bar_chart_data[++index]);
            auditor.push(bar_chart_data[++index]);
            followup.push(bar_chart_data[++index]);
            reviewer.push(bar_chart_data[++index]);
        }

        this.barChartRolePerAudits = {
            labels: ['IS', 'MGT', 'INS'],
            datasets: [
                {
                    type: 'bar',
                    label: 'Auditor ',
                    backgroundColor: '#48C9B0',
                    data: auditor
                },
                {
                    type: 'bar',
                    label: 'Reviewer',
                    backgroundColor: '#F8C471',
                    data: reviewer
                },
                {
                    type: 'bar',
                    label: 'Approver',
                    backgroundColor: '#28B463',
                    data: approver
                },
                {
                    type: 'bar',
                    label: 'Followup',
                    backgroundColor: '#FA8072',
                    data: followup
                }
            ]
        };
    }

    getLineChartData(line_chart_data: Number[]) {
        let drafted_audits = [],
            passed_audits = [],
            reviewer_rejected_audits = [],
            reviewed_audits = [];
        for (let index = -1; index < line_chart_data.length; ) {
            drafted_audits.push(line_chart_data[++index]);
            passed_audits.push(line_chart_data[++index]);
            reviewer_rejected_audits.push(line_chart_data[++index]);
            reviewed_audits.push(line_chart_data[++index]);
        }

        this.line_chart_data = {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [
                {
                    label: 'Drafted',
                    data: drafted_audits,
                    fill: false,
                    borderDash: [5, 5],
                    tension: 0.4,
                    borderColor: '#CCD1D1',
                    backgroundColor: '#CCD1D1'
                },
                {
                    label: 'Passed',
                    data: passed_audits,
                    fill: false,
                    borderDash: [5, 5],
                    tension: 0.4,
                    borderColor: '#95A5A6',
                    backgroundColor: '#95A5A6'
                },
                {
                    label: 'Reviewer Rejected',
                    data: reviewer_rejected_audits,
                    fill: false,
                    borderDash: [5, 5],
                    borderColor: '#FAD7A0',
                    tension: 0.4,
                    backgroundColor: '#FAD7A0'
                },
                {
                    label: 'Reviewed',
                    data: reviewed_audits,
                    fill: false,
                    borderDash: [5, 5],
                    tension: 0.4,
                    borderColor: '#36A2EB',
                    backgroundColor: '#36A2EB'
                }
            ]
        };
    }

    getRadarData(age_data: Number[]) {
        let active = [];
        let inactive = [];

        for (let index = -1; index < age_data.length; ) {
            active.push(age_data[++index]);
            inactive.push(age_data[++index]);
        }

        this.radar_data = {
            labels: ['IS', 'MGT', 'BFA', 'INS'],
            datasets: [
                {
                    label: 'Active',
                    backgroundColor: 'rgba(26, 188, 156 ,0.2)',
                    borderColor: 'rgba(26, 188, 156 , 1)',
                    pointBackgroundColor: 'rgba(26, 188, 156 , 1)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(26, 188, 156 , 1)',
                    data: active
                },
                {
                    label: 'Inactive',
                    backgroundColor: 'rgba(26, 82, 118,0.2)',
                    borderColor: 'rgba(31, 155, 232, 1)',
                    pointBackgroundColor: 'rgba(26, 82, 118,1)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(26, 82, 118,1)',
                    data: inactive
                }
            ]
        };
    }
}
