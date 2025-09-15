import { Component } from '@angular/core';
import { AdminDashboardComponent } from '../admin-dashboard/admin-dashboard.component';
import { SharedUiModule } from '../../../../shared-ui';
import { MakerDashboard } from '../maker-dashboard/maker-dashboard';
import { StorageService } from '../../../service/sharedService/storage.service';

@Component({
    standalone: true,
    selector: 'app-dashboard-container',
    imports: [SharedUiModule, AdminDashboardComponent, MakerDashboard],
    templateUrl: './dashboard-container.component.html'
})
export class DashboardContainerComponent {
    isLoggedIn = false;
    private roles: string[] = [];
    admin = false;
    approver = false;
    followup_officer = false;
    reviewer = false;
    auditee = false;
    auditee_division = false;
    auditor = false;
    auditor_followup_officer = false;
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
            this.followup_officer = this.roles.includes('ROLE_FOLLOWUP_OFFICER_IS') || this.roles.includes('ROLE_FOLLOWUP_OFFICER_MGT');
            this.auditor = this.roles.includes('ROLE_AUDITOR_IS') || this.roles.includes('ROLE_AUDITOR_MGT');
            this.auditee = this.roles.includes('ROLE_AUDITEE');
            this.auditee_division = this.roles.includes('ROLE_AUDITEE_DIVISION');
            this.higher_official = this.roles.includes('ROLE_HIGHER_OFFICIAL');
            if (this.auditor && this.followup_officer) {
                this.auditor_followup_officer = true;
            }
            if (this.approver && this.reviewer) {
                this.approver_reviewer = true;
            }
        }
    }
}
