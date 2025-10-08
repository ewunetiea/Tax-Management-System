import { Component } from '@angular/core';
import { AdminDashboardComponent } from '../admin-dashboard/admin-dashboard.component';
import { SharedUiModule } from '../../../../shared-ui';
import { MakerDashboard } from '../maker-dashboard/maker-dashboard';
import { StorageService } from '../../../service/sharedService/storage.service';
import { HODashboard } from '../ho-dashboard/ho-dashboard';

@Component({
    standalone: true,
    selector: 'app-dashboard-container',
    imports: [SharedUiModule, AdminDashboardComponent, MakerDashboard,HODashboard],
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
            this.approver = this.roles.includes('ROLE_APPROVER_IS') || this.roles.includes('ROLE_APPROVER_MGT');
            this.reviewer = this.roles.includes('ROLE_REVIEWER_IS') || this.roles.includes('ROLE_REVIEWER_MGT');
            this.ho = this.roles.includes('ROLE_HO') ;
            this.checker = this.roles.includes('ROLE_CHECKER');
        this.maker = this.roles.includes('ROLE_MAKER');

            this.auditee = this.roles.includes('ROLE_AUDITEE');
            this.auditee_division = this.roles.includes('ROLE_AUDITEE_DIVISION');
            this.higher_official = this.roles.includes('ROLE_HIGHER_OFFICIAL');
           
            if (this.approver && this.reviewer) {
                this.approver_reviewer = true;
            }
        }
    }
}
