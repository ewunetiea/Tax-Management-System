import { Component, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { AppMenuAdmin } from '../side-bar-items/app.admin_menu';
import { AppMenuMaker } from '../side-bar-items/app.maker_menu';
import { SharedUiModule } from '../../../../shared-ui';
import { StorageService } from '../../../service/sharedService/storage.service';

@Component({
    selector: 'app-sidebar',
    standalone: true,
    imports: [AppMenuAdmin, AppMenuMaker, SharedUiModule],
    templateUrl: './app.sidebar.component.html'
})
export class AppSidebar {
    isLoggedIn = false;
    role_type: string = '';
    roles: string[] = [];
    loading = true;

    constructor(
        public el: ElementRef,
        private storageService: StorageService,
        public router: Router
    ) {}

    ngOnInit() {
        setTimeout(() => {
            this.loading = false;
        }, 1000);

        this.isLoggedIn = this.storageService.isLoggedIn();
        if (this.isLoggedIn) {
            const user = this.storageService.getUser();
            this.roles = user?.roles || [];
            if (this.roles.includes('ROLE_ADMIN')) {
                this.role_type = 'admin';
            } else if (this.roles.includes('ROLE_AUDITOR_INS')) {
                this.role_type = 'maker';
            }
        }
    }
}
