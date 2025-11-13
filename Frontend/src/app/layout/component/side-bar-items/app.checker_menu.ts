import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AppMenuitem } from '../app.menuitem';
import { StorageService } from '../../../service/sharedService/storage.service';

@Component({
    selector: 'app-checker_menu',
    standalone: true,
    imports: [CommonModule, AppMenuitem, RouterModule],
    template: `<ul class="layout-menu">
        <ng-container *ngFor="let item of model; let i = index">
            <li app-menuitem *ngIf="!item.separator" [item]="item" [index]="i" [root]="true"></li>
            <li *ngIf="item.separator" class="menu-separator"></li>
        </ng-container>
    </ul> `
})
export class AppMenuChecker {
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
                items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/applayout'] }]
            },
            {
                label: 'Tax Management',
                items: [
                    { label: 'Waiting For Reviewed',  icon: 'pi pi-clock', routerLink: ['/applayout/reviewer/manage_tax', 'pending'] },
                    { label: 'Sent',  icon: 'pi pi-verified', routerLink: ['/applayout/reviewer/manage_tax', 'sent'] },
                    { label: 'Rejected', icon: 'pi pi-times-circle', routerLink: ['/applayout/reviewer/manage_tax', 'rejected'] },
                    { label: 'Settled', icon: 'pi pi-check-circle',  routerLink: ['/applayout/reviewer/manage_tax', 'settled'] },
                ]
            },

            {
                label: 'Report',
                items: [{ label: 'Report', icon: 'pi pi-fw pi-table', routerLink: ['/applayout/approver/report'] }  
            ]
            },

             {
                label: 'Activity',
                items: [{ label: 'Recent Activity', icon: 'pi pi-fw pi-comment', routerLink: ['/applayout/admin/user-recent-activity'] }  
            ]
            },
        ];
        this.model = this.general_items;
    }
}
