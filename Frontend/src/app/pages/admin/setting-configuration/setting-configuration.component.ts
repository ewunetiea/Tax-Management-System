import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { MaxFailedAndJwtControl } from '../../../models/admin/max-failed-and-jwt-control';
import { SharedUiModule } from '../../../../shared-ui';
import { TimeagoModule } from 'ngx-timeago';
import { MaxFailedAndJwtControlService } from '../../../service/admin/max-failed-and-jwt-control.service';

@Component({
    standalone: true,
    selector: 'app-setting-configuration',
    imports: [SharedUiModule],         // ✅ Must be here

    
    templateUrl: './setting-configuration.component.html',
    styleUrls: ['./setting-configuration.component.css'],
})
export class SettingConfigurationComponent {
    setting: MaxFailedAndJwtControl = new MaxFailedAndJwtControl();
    editStatus = false;
    editFlag = false;
    confrimFlag = false;

    editStatusjwt = false;
    editFlagjwt = false;
    constructor(
        private systemSettingService: MaxFailedAndJwtControlService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) { }

    ngOnInit(): void {
        this.getSettings();
    }

    getSettings() {
        this.systemSettingService.getSystemSettings().subscribe({
            next: (data) => {
                if (data) {
                    this.setting = data;
                    if (
                        this.setting.jwt_expiration != null &&
                        this.setting.jwt_expiration != null
                    ) {
                        this.editFlagjwt = true;
                        this.editStatusjwt = true;
                    }
                    this.editFlag = true;
                    this.editStatus = true;
                }
            },
            error: (error) => {
                this.editStatus = false;
                this.editFlag = false;
                this.messageService.add({
                    severity: 'error',
                    summary:
                        error.status == 401
                            ? 'You are not permitted to perform this action!'
                            : 'Something went wrong while fetching setting configuration !',
                    detail: '',
                });
            },
        });
    }

    saveAccountLockSetting() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to update system login status setting ?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.systemSettingService.saveAccountSetting(this.setting).subscribe({
                    next: (data) => {
                        this.messageService.add({
                            severity: 'success',
                            summary:
                                'System login status setting configuration successfully updated!',
                            detail: '',
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.messageService.add({
                            severity: 'error',
                            summary:
                                error.status == 401
                                    ? 'You are not permitted to perform this action!'
                                    : 'Something went wrong while fetching updating system login status setting configuration !',
                            detail: '',
                        });
                    },
                });
            },
        });
    }

    saveJWTSetting() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to update JWT setting ?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.systemSettingService.saveJWTSetting(this.setting).subscribe({
                    next: (data) => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'JWT setting configuration successfully updated!',
                            detail: '',
                        });
                        this.confrimFlag = true;
                    },
                    error: (error: HttpErrorResponse) => {
                        this.messageService.add({
                            severity: 'error',
                            summary:
                                error.status == 401
                                    ? 'You are not permitted to perform this action!'
                                    : 'Something went wrong while fetching updating JWT setting configuration !',
                            detail: '',
                        });
                    },
                });
            },
        });
    }
    enableEditing() {
        this.editFlag = false;
        this.editStatus = false;
    }
    enableEditingjwt() {
        this.editFlagjwt = false;
        this.editStatusjwt = false;
    }
}
