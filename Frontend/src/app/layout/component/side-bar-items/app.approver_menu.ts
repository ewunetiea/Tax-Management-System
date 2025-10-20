import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AppMenuitem } from '../app.menuitem';
import { StorageService } from '../../../service/sharedService/storage.service';

@Component({
    selector: 'app-ho_menu',
    standalone: true,
    imports: [CommonModule, AppMenuitem, RouterModule],
    template: `<ul class="layout-menu">
        <ng-container *ngFor="let item of model; let i = index">
            <li app-menuitem *ngIf="!item.separator" [item]="item" [index]="i" [root]="true"></li>
            <li *ngIf="item.separator" class="menu-separator"></li>
        </ng-container>
    </ul> `
})
export class AppMenuApprover {
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
                label: 'Manage Taxes',
                items: [
                    { label: 'Pending Taxes',  icon: 'pi pi-clock', routerLink: ['/applayout/approver/manage-tax-ho', 'pending'] },
                    { label: 'Rejected Taxes', icon: 'pi pi-times-circle', routerLink: ['/applayout/approver/manage-tax-ho', 'rejected'] },
                    { label: 'Approved Taxes', icon: 'pi pi-check-circle',  routerLink: ['/applayout/approver/manage-tax-ho', 'approved'] },
                ]
            },
            {
                label: 'Announcements',
                items: [
                    { label: 'Ongoing ', icon: 'pi pi-clock', routerLink: ['/applayout/approver/ongoing-announcement'] },
                    { label: 'Archived', icon: 'pi pi-fw pi-trash', routerLink: ['/applayout/approver/archived-announcement'] },
                ]
            },
            {
                label: 'Configurations',
                items: [{ label: 'Tax Type', icon: 'pi pi-fw pi-dollar', routerLink: ['/applayout/approver/manage-tax-category'] }]
            },
            {
                label: 'Tax Information',
                items: [{ label: 'Manege Tax', icon: 'pi pi-fw pi-table', routerLink: ['/applayout/maker/manage-tax'] }  
            ]
            },

            {
                label: 'Report',
                items: [{ label: 'Report', icon: 'pi pi-fw pi-table', routerLink: ['/applayout/approver/report'] }  
            ]
            },
        ];
        
        this.model = this.general_items;
    }
}
