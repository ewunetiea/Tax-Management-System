import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AppMenuitem } from '../app.menuitem';
import { StorageService } from '../../../service/sharedService/storage.service';

@Component({
    selector: 'app-maker_menu',
    standalone: true,
    imports: [CommonModule, AppMenuitem, RouterModule],
    template: `<ul class="layout-menu">
        <ng-container *ngFor="let item of model; let i = index">
            <li app-menuitem *ngIf="!item.separator" [item]="item" [index]="i" [root]="true"></li>
            <li *ngIf="item.separator" class="menu-separator"></li>
        </ng-container>
    </ul> `
})
export class AppMenuMaker {
    model: MenuItem[] = [];
    general_items: MenuItem[] = [];
    admin_items: MenuItem[] = [];
    auditor_items: MenuItem[] = [];
    regional_compiler_items: MenuItem[] = [];
    followup_items: MenuItem[] = [];
    approver_items: MenuItem[] = [];
    auditee_items: MenuItem[] = [];
    division_items: MenuItem[] = [];
    isLoggedIn = false;
    roles: string[] = [];

    constructor(
        private storageService: StorageService,
        public router: Router
    ) { }

    ngOnInit() {
        this.isLoggedIn = this.storageService.isLoggedIn();
        this.general_items = [
            {
                label: 'Home',
                items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home text-orange-500', routerLink: ['/applayout'] }]
            },


            {
                label: 'Tax Management',
                items: [
                    { label: 'Drafted', icon: 'pi pi-fw pi-pencil text-blue-500', routerLink: ['/applayout/maker/drafted-tax'] },
                    { label: 'Submitted to Checker', icon: 'pi pi-fw pi-send text-green-500', routerLink: ['/applayout/maker/submited-tax'] },
                    { label: 'Sent to Approval', icon: 'pi pi-fw pi-share-alt text-orange-500', routerLink: ['/applayout/maker/sent-tax'] },
                    { label: 'Settled', icon: 'pi pi-fw pi-check-circle text-teal-500', routerLink: ['/applayout/maker/setteled-tax'] },
                    { label: 'Rejected', icon: 'pi pi-fw pi-times-circle text-red-600', routerLink: ['/applayout/maker/rejected-tax'] },
                ]
            }
            ,

            {
                label: 'Tax Progress',
                items: [{ label: 'General Status ', icon: 'pi pi-spinner text-blue-500', routerLink: ['/applayout/maker/general-tax-status'] }

                ],

            },

            {
                label: 'Report',
                items: [{ label: 'Report', icon: 'pi pi-file-excel text-green-500', routerLink: ['/applayout/approver/report'] }
                ]
            },

            {
                label: 'Activity Logs',
                items: [{ label: 'User Activities ', icon: 'pi pi-history text-teal-500', routerLink: ['/applayout/admin/user-recent-activity'] }

                ],

            },

            // {
            //     label: 'UI Components',
            //     items: [
            //         { label: 'Crud', icon: 'pi pi-fw pi-pencil', routerLink: ['/applayout/maker/crud'] },

            //         { label: 'Form Layout', icon: 'pi pi-fw pi-id-card', routerLink: ['/applayout/uikit/formlayout'] },
            //         { label: 'Input', icon: 'pi pi-fw pi-check-square', routerLink: ['/applayout/uikit/input'] },
            //         { label: 'Button', icon: 'pi pi-fw pi-mobile', class: 'rotated-icon', routerLink: ['/applayout/uikit/button'] },
            //         { label: 'Table', icon: 'pi pi-fw pi-table', routerLink: ['/applayout/uikit/table'] },
            //         { label: 'List', icon: 'pi pi-fw pi-list', routerLink: ['/applayout/uikit/list'] },
            //         { label: 'Tree', icon: 'pi pi-fw pi-share-alt', routerLink: ['/applayout/uikit/tree'] },
            //         { label: 'Panel', icon: 'pi pi-fw pi-tablet', routerLink: ['/applayout/uikit/panel'] },
            //         { label: 'Overlay', icon: 'pi pi-fw pi-clone', routerLink: ['/applayout/uikit/overlay'] },
            //         { label: 'Media', icon: 'pi pi-fw pi-image', routerLink: ['/applayout/uikit/media'] },
            //         { label: 'Menu', icon: 'pi pi-fw pi-bars', routerLink: ['/applayout/uikit/menu'] },
            //         { label: 'Message', icon: 'pi pi-fw pi-comment', routerLink: ['/applayout/uikit/message'] },
            //         { label: 'File', icon: 'pi pi-fw pi-file', routerLink: ['/applayout/uikit/file'] },
            //         { label: 'Chart', icon: 'pi pi-fw pi-chart-bar', routerLink: ['/applayout/maker/charts'] },
            //         { label: 'Timeline', icon: 'pi pi-fw pi-calendar', routerLink: ['/applayout/uikit/timeline'] },
            //         { label: 'Misc', icon: 'pi pi-fw pi-circle', routerLink: ['/applayout/uikit/misc'] }
            //     ]
            // },

            // {
            //     label: 'Pages',
            //     icon: 'pi pi-fw pi-briefcase',
            //     routerLink: ['/pages'],
            //     items: [
            //         {
            //             label: 'Landing',
            //             icon: 'pi pi-fw pi-globe',
            //             routerLink: ['/landing']
            //         },
            //         {
            //             label: 'Auth',
            //             icon: 'pi pi-fw pi-user',
            //             items: [
            //                 {
            //                     label: 'Login',
            //                     icon: 'pi pi-fw pi-sign-in',
            //                     routerLink: ['/auth/login']
            //                 },
            //                 {
            //                     label: 'Error',
            //                     icon: 'pi pi-fw pi-times-circle',
            //                     routerLink: ['/auth/error']
            //                 },
            //                 {
            //                     label: 'Access Denied',
            //                     icon: 'pi pi-fw pi-lock',
            //                     routerLink: ['/auth/access']
            //                 }
            //             ]
            //         },

            //         {
            //             label: 'Not Found',
            //             icon: 'pi pi-fw pi-exclamation-circle',
            //             routerLink: ['/pages/notfound']
            //         },
            //         {
            //             label: 'Empty',
            //             icon: 'pi pi-fw pi-circle-off',
            //             routerLink: ['/pages/empty']
            //         }
            //     ]
            // },
            // {
            //     label: 'Hierarchy',
            //     items: [
            //         {
            //             label: 'Submenu 1',
            //             icon: 'pi pi-fw pi-bookmark',
            //             items: [
            //                 {
            //                     label: 'Submenu 1.1',
            //                     icon: 'pi pi-fw pi-bookmark',
            //                     items: [
            //                         { label: 'Submenu 1.1.1', icon: 'pi pi-fw pi-bookmark' },
            //                         { label: 'Submenu 1.1.2', icon: 'pi pi-fw pi-bookmark' },
            //                         { label: 'Submenu 1.1.3', icon: 'pi pi-fw pi-bookmark' }
            //                     ]
            //                 },
            //                 {
            //                     label: 'Submenu 1.2',
            //                     icon: 'pi pi-fw pi-bookmark',
            //                     items: [{ label: 'Submenu 1.2.1', icon: 'pi pi-fw pi-bookmark' }]
            //                 }
            //             ]
            //         },
            //         {
            //             label: 'Submenu 2',
            //             icon: 'pi pi-fw pi-bookmark',
            //             items: [
            //                 {
            //                     label: 'Submenu 2.1',
            //                     icon: 'pi pi-fw pi-bookmark',
            //                     items: [
            //                         { label: 'Submenu 2.1.1', icon: 'pi pi-fw pi-bookmark' },
            //                         { label: 'Submenu 2.1.2', icon: 'pi pi-fw pi-bookmark' }
            //                     ]
            //                 },
            //                 {
            //                     label: 'Submenu 2.2',
            //                     icon: 'pi pi-fw pi-bookmark',
            //                     items: [{ label: 'Submenu 2.2.1', icon: 'pi pi-fw pi-bookmark' }]
            //                 }
            //             ]
            //         }
            //     ]
            // },
            // {
            //     label: 'Get Started',
            //     items: [
            //         {
            //             label: 'Documentation',
            //             icon: 'pi pi-fw pi-book',
            //             routerLink: ['/documentation']
            //         },
            //         {
            //             label: 'View Source',
            //             icon: 'pi pi-fw pi-github',
            //             url: 'https://github.com/primefaces/sakai-ng',
            //             target: '_blank'
            //         }
            //     ]
            // }
        ];
        this.model = this.general_items;
    }
}
