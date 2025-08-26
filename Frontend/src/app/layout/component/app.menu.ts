import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AppMenuitem } from './app.menuitem';
import { StorageService } from '../../pages/service/admin/storage.service';

@Component({
    selector: 'app-menu',
    standalone: true,
    imports: [CommonModule, AppMenuitem, RouterModule],
    template: `<ul class="layout-menu">
        <ng-container *ngFor="let item of model; let i = index">
            <li app-menuitem *ngIf="!item.separator" [item]="item" [index]="i" [root]="true"></li>
            <li *ngIf="item.separator" class="menu-separator"></li>
        </ng-container>
    </ul> `
})
export class AppMenu {
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
        public router: Router,
    ) {}

    ngOnInit() {
        this.isLoggedIn = this.storageService.isLoggedIn();
        this.admin_items = [
            {
                label: 'Home',
                items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: '/applayout' }]
            },
            {
                label: 'Region Management', 
                items: [
                    { label: 'Manage Region', icon: 'pi pi-building-columns', routerLink: ['/applayout/admin/manage-region'] },
                    { label: 'Manage Branch', icon: 'pi pi-map-marker', routerLink: ['/applayout/admin/manage-branch'] },
                ]
            },
            {
                label: 'Role Management',
                icon: 'pi pi-fw pi-briefcase',
                items: [
                    {
                        label: 'Manage Role',  icon: 'pi pi-user', routerLink: ['/applayout/admin/manage-role']
                    },
                    {
                        label: 'Manage Job Position',
                        icon: 'pi pi-graduation-cap',
                        routerLink: ['/applayout/admin/manage-job-position']
                    }
                ]
            },
            {
                label: 'Permissions',
                icon: 'pi pi-fw pi-user',
                items: [
                    {
                        label: 'Role Permissions',
                        icon: 'pi pi-verified',
                        routerLink: ['/applayout/admin/role_functionalities']
                    },
                    {
                        label: 'Manage Permissions',
                        icon: 'pi pi-verified',
                        routerLink: ['/applayout/admin/manage_role_functionalities']
                    }, 
                    {
                        label: 'Manage User Permissions',
                        icon: 'pi pi-verified',
                        routerLink: ['/applayout/admin/manage_user_permissions']
                    }
                ]
            },
            {
                label: 'User Management',
                icon: 'pi pi-fw pi-briefcase',
                routerLink: ['/pages'],
                items: [
                    {
                        label: 'User',
                        icon: 'pi pi-fw pi-user',
                        items: [
                            {
                                label: 'Manage User',
                                icon: 'pi pi-fw pi-users',
                                routerLink: ['/applayout/admin/manage_user']
                            },
                            {
                                label: 'Manage User Status',
                                icon: 'pi pi-fw pi-lock',
                                routerLink: ['/applayout/admin/manage-user-status']
                            },
                            {
                                label: 'Replace HR Data',
                                icon: 'pi pi-database',
                                routerLink: ['/applayout/admin/replace-hr-data'] 
                            }
                        ]
                    },
                ]
            },

            {
                label: 'Menu Management',
                icon: 'pi pi-fw pi-briefcase',
                routerLink: ['/pages'],
                items: [
                    {
                        label: 'Menu',
                        icon: 'pi pi-warehouse',
                        items: [
                            {
                                label: 'Menu Headers',
                                icon: 'pi pi-warehouse',
                                routerLink: ['/applayout/admin/menu-headers']
                            },
                            {
                                label: 'Menu Items',
                                icon: 'pi pi-fw pi-briefcase',
                                routerLink: ['/applayout/admin/menu-items']
                            }
                        ]
                    },
                ]
            },
            {
                label: 'Reports Management',
                icon: 'pi pi-file-excel',
                routerLink: ['/pages'],
                items: [
                    {
                        label: 'Report',
                        icon: 'pi pi-file-excel',
                        items: [
                            {
                                label: 'User Login Status',
                                icon: 'pi pi-fw pi-lock',
                                routerLink: ['/applayout/admin/user-login-status'] 
                            },
                            {
                                label: 'User Activities',
                                icon: 'pi pi-fw pi-times-circle',
                                routerLink: ['/applayout/admin/user-recent-activity']
                            },
                        ]
                    },
                ]
            },
            
            {
                label: 'Database Management',
                icon: 'pi pi-database',
                items: [
                    {
                        label: 'Backup Database',
                        icon: 'pi pi-fw pi-database',
                        routerLink: ['/applayout/admin/backup']
                    },
                ]
            },

            {
                label: 'Schedule Management',
                icon: 'pi pi-fw pi-user',
                items: [
                    {
                        label: 'Schedule',
                        icon: 'pi pi-fw pi-sign-in',
                        routerLink: ['/applayout/admin/manage-schedules']
                    },
                ]
            },

        ];

        this.general_items = [
            {
                label: 'Home',
                items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'] }]
            },
            {
                label: 'UI Components',
                items: [
                    { label: 'Form Layout', icon: 'pi pi-fw pi-id-card', routerLink: ['/uikit/formlayout'] },
                    { label: 'Input', icon: 'pi pi-fw pi-check-square', routerLink: ['/uikit/input'] },
                    { label: 'Button', icon: 'pi pi-fw pi-mobile', class: 'rotated-icon', routerLink: ['/uikit/button'] },
                    { label: 'Table', icon: 'pi pi-fw pi-table', routerLink: ['/uikit/table'] },
                    { label: 'List', icon: 'pi pi-fw pi-list', routerLink: ['/uikit/list'] },
                    { label: 'Tree', icon: 'pi pi-fw pi-share-alt', routerLink: ['/uikit/tree'] },
                    { label: 'Panel', icon: 'pi pi-fw pi-tablet', routerLink: ['/uikit/panel'] },
                    { label: 'Overlay', icon: 'pi pi-fw pi-clone', routerLink: ['/uikit/overlay'] },
                    { label: 'Media', icon: 'pi pi-fw pi-image', routerLink: ['/uikit/media'] },
                    { label: 'Menu', icon: 'pi pi-fw pi-bars', routerLink: ['/uikit/menu'] },
                    { label: 'Message', icon: 'pi pi-fw pi-comment', routerLink: ['/uikit/message'] },
                    { label: 'File', icon: 'pi pi-fw pi-file', routerLink: ['/uikit/file'] },
                    { label: 'Chart', icon: 'pi pi-fw pi-chart-bar', routerLink: ['/uikit/charts'] },
                    { label: 'Timeline', icon: 'pi pi-fw pi-calendar', routerLink: ['/uikit/timeline'] },
                    { label: 'Misc', icon: 'pi pi-fw pi-circle', routerLink: ['/uikit/misc'] }
                ]
            },
            {
                label: 'Pages',
                icon: 'pi pi-fw pi-briefcase',
                routerLink: ['/pages'],
                items: [
                    {
                        label: 'Landing',
                        icon: 'pi pi-fw pi-globe',
                        routerLink: ['/landing']
                    },
                    {
                        label: 'Auth',
                        icon: 'pi pi-fw pi-user',
                        items: [
                            {
                                label: 'Login',
                                icon: 'pi pi-fw pi-sign-in',
                                routerLink: ['/auth/login']
                            },
                            {
                                label: 'Error',
                                icon: 'pi pi-fw pi-times-circle',
                                routerLink: ['/auth/error']
                            },
                            {
                                label: 'Access Denied',
                                icon: 'pi pi-fw pi-lock',
                                routerLink: ['/auth/access']
                            }
                        ]
                    },
                    {
                        label: 'Crud',
                        icon: 'pi pi-fw pi-pencil',
                        routerLink: ['/pages/crud']
                    },
                    {
                        label: 'Not Found',
                        icon: 'pi pi-fw pi-exclamation-circle',
                        routerLink: ['/pages/notfound']
                    },
                    {
                        label: 'Empty',
                        icon: 'pi pi-fw pi-circle-off',
                        routerLink: ['/pages/empty']
                    }
                ]
            },
            {
                label: 'Hierarchy',
                items: [
                    {
                        label: 'Submenu 1',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [
                            {
                                label: 'Submenu 1.1',
                                icon: 'pi pi-fw pi-bookmark',
                                items: [
                                    { label: 'Submenu 1.1.1', icon: 'pi pi-fw pi-bookmark' },
                                    { label: 'Submenu 1.1.2', icon: 'pi pi-fw pi-bookmark' },
                                    { label: 'Submenu 1.1.3', icon: 'pi pi-fw pi-bookmark' }
                                ]
                            },
                            {
                                label: 'Submenu 1.2',
                                icon: 'pi pi-fw pi-bookmark',
                                items: [{ label: 'Submenu 1.2.1', icon: 'pi pi-fw pi-bookmark' }]
                            }
                        ]
                    },
                    {
                        label: 'Submenu 2',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [
                            {
                                label: 'Submenu 2.1',
                                icon: 'pi pi-fw pi-bookmark',
                                items: [
                                    { label: 'Submenu 2.1.1', icon: 'pi pi-fw pi-bookmark' },
                                    { label: 'Submenu 2.1.2', icon: 'pi pi-fw pi-bookmark' }
                                ]
                            },
                            {
                                label: 'Submenu 2.2',
                                icon: 'pi pi-fw pi-bookmark',
                                items: [{ label: 'Submenu 2.2.1', icon: 'pi pi-fw pi-bookmark' }]
                            }
                        ]
                    }
                ]
            },
            {
                label: 'Get Started',
                items: [
                    {
                        label: 'Documentation',
                        icon: 'pi pi-fw pi-book',
                        routerLink: ['/documentation']
                    },
                    {
                        label: 'View Source',
                        icon: 'pi pi-fw pi-github',
                        url: 'https://github.com/primefaces/sakai-ng',
                        target: '_blank'
                    }
                ]
            }
        ];

        if (this.isLoggedIn) {
            const user = this.storageService.getUser();
            this.roles = user?.roles || [];
            if (this.roles.includes('ROLE_ADMIN')) {
                this.model = this.admin_items;
            } else if (this.roles.includes('ROLE_AUDITOR_IS')) {
                this.model = this.auditor_items;
            } else if (this.roles.includes('ROLE_APPROVER_IS')) {
                this.model = this.approver_items;
            } else {
                this.model = this.general_items;
            }
        } else {
            this.model = this.general_items;
        }
    }
}
