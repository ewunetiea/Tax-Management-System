import { Component } from '@angular/core';
import { User } from '../../../../models/admin/user';
import { Functionalities } from '../../../../models/admin/functionalities';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';

import { Table } from 'primeng/table';
import { UserSearchEngineComponent } from '../../user/user-search-engine/user-search-engine.component';
import { SharedUiModule } from '../../../../../shared-ui';
import { UserFunctionalityService } from '../../../../service/admin/user-functionality-service';

@Component({
  standalone: true,
  selector: 'app-manage-user-permissions',
  imports: [SharedUiModule, UserSearchEngineComponent],
  templateUrl: './manage-user-permissions.component.html',
  styleUrl: './manage-user-permissions.component.scss',
  providers: [ConfirmationService, MessageService]

})
export class ManageUserPermissionsComponent {
  user: User = new User();
  users: User[] = [];
  selectedUsers: User[] = [];
  functionalities: Functionalities[] = [];
  assignedFunctionalities: Functionalities[] = [];
  selectedFunctionalities: Functionalities[] = [];
  loading: boolean = true;
  functionalityDialog: boolean = false;
  functionalityLoading: boolean = false;
  functionalityStatusDialog: boolean = false;
  fetching: boolean = false;
  sizes!: any[];
  selectedSize: any = 'normal';
  breadcrumbText: string = 'Manage User Permissions';
  items: MenuItem[] | undefined;
  home: MenuItem | undefined;

  constructor(
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private userFunctionalityService: UserFunctionalityService
  ) { }

  ngOnInit(): void {
    this.home = { icon: 'pi pi-home', routerLink: '/' };
    this.items = [{ label: this.breadcrumbText }];
    this.sizes = [
      { name: 'Small', value: 'small' },
      { name: 'Normal', value: 'normal' },
      { name: 'Large', value: 'large' }
    ];
  }

  onViewFunctionalities(user: User): void {
    this.user = user;
    this.functionalityStatusDialog = true;
    this.assignedFunctionalities = this.user.functionalities || [];


    console.log(this.assignedFunctionalities)
  }

  closeDialog(): void {
    this.functionalityDialog = false;
    this.functionalities = [];
  }

  onDataGenerated(data: User[]) {
    this.loading = false;
    if (data != null) {
      this.fetching = true;
      this.users = data;
    }
  }

  onGlobalFilter(table: Table, event: Event) {
    const input = event.target as HTMLInputElement;
    table.filterGlobal(input.value, 'contains');
  }

  clear(table: Table) {
    table.clear();
  }

  revokeFunctionalities(): void {
    this.user.functionalities = this.selectedFunctionalities || [];
    this.confirmationService.confirm({
      message: 'Are you sure you want to revoke selected permissions?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.userFunctionalityService.deactivatePermissions(this.user).subscribe({
          next: (data) => {
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: 'Permissions revoked successfully!',
            });

          },
          error: () => {
            this.loading = false;
          }
        });
      },
    });
  }

  grantFunctionalities(): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to grant selected permissions?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.userFunctionalityService
          .activatePermissions(this.selectedFunctionalities)
          .subscribe({
            next: (data) => {
              this.messageService.add({
                severity: 'success',
                summary: 'Success',
                detail: 'Permissions granted successfully!',
              });
            },
            error: () => {
              this.loading = false;
            }
          });
      },
    });
  }


}
