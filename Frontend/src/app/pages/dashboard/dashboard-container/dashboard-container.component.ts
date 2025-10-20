import { Component } from '@angular/core';
import { AdminDashboardComponent } from '../admin-dashboard/admin-dashboard.component';
import { SharedUiModule } from '../../../../shared-ui';
import { MakerDashboard } from '../maker-dashboard/maker-dashboard';
import { StorageService } from '../../../service/sharedService/storage.service';
import { HODashboard } from '../ho-dashboard/ho-dashboard';
import { CheckerDashboardComponent } from '../reviewer-dashboard/checker-dashboard.component';

@Component({
    standalone: true,
    selector: 'app-dashboard-container',
    imports: [SharedUiModule, AdminDashboardComponent, MakerDashboard, CheckerDashboardComponent, HODashboard],
    templateUrl: './dashboard-container.component.html'
})
export class DashboardContainerComponent {
    isLoggedIn = false;
    private roles: string[] = [];
    admin = false;
    approver = false;
    maker = false;
    reviewer = false;
    auditee = false;
    auditee_division = false;
    checker = false;
    ho = false;
    approver_reviewer = false;
    higher_official = false;

    constructor(private storageService: StorageService) {}

    ngOnInit(): void {
        this.isLoggedIn = this.storageService.isLoggedIn();
        if (this.isLoggedIn) {
            const user = this.storageService.getUser();
            this.roles = user.roles;
            this.admin = this.roles.includes('ROLE_ADMIN');
            this.ho = this.roles.includes('ROLE_APPROVER') ;
            this.checker = this.roles.includes('ROLE_REVIEWER');
            this.maker = this.roles.includes('ROLE_MAKER');
           
            if (this.approver && this.reviewer) {
                this.approver_reviewer = true;
            }
        }
    }
}
