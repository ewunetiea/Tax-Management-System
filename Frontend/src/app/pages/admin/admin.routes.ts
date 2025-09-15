import { Routes } from '@angular/router';
import { ManageRegionComponent } from './region/manageRegion/manage-region/manage-region.component';
import { ManagebranchComponent } from './branch/managebranch/managebranch.component';
import { ManageRoleComponent } from './role/manage-role/manage-role.component';
import { SchedulesComponent } from './schedule/schedules/schedules.component';
import { ManageJobPositionComponent } from './jobPosition/manage-job-position/manage-job-position.component';
import { RoleFunctionalitiesComponent } from './role/role-functionalities/role-functionalities.component';
import { ManageRoleFunctionalitiesComponent } from './role/manage-role-functionalities/manage-role-functionalities.component';
import { ManageUserComponent } from './user/manage-user/manage-user.component';
import { ManageUserStatusComponent } from './user/manage-user-status/manage-user-status/manage-user-status.component';
import { ReplaceHRDataComponent } from './user/replaceHRData/replace-hrdata/replace-hrdata.component';
import { UserActivityComponent } from './user/user-activity/user-activity/user-activity.component';
import { UserRecentActivityComponent } from './user/user-recent-activity/user-recent-activity/user-recent-activity.component';
import { BackupComponent } from './backup/backup/backup.component';
import { ManageUserPermissionsComponent } from './role/manage-user-permissions/manage-user-permissions.component';
import { MenuheadersComponent } from './menu/menuheaders/menuheaders.component';
import { MenuitemsComponent } from './menu/menuitems/menuitems.component';
import { AuthGuard } from '../../service/admin/auth.guard';

export default [
    {
        path: 'manage-region',
        component: ManageRegionComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'manage-branch',
        component: ManagebranchComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'manage-role',
        component: ManageRoleComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'manage-schedules',
        component: SchedulesComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'manage-job-position',
        component: ManageJobPositionComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'role_functionalities',
        component: RoleFunctionalitiesComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'manage_role_functionalities',
        component: ManageRoleFunctionalitiesComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'manage_user_permissions',
        component: ManageUserPermissionsComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'manage_user',
        component: ManageUserComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'manage-user-status',
        component: ManageUserStatusComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'menu-headers',
        component: MenuheadersComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'menu-items',
        component: MenuitemsComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'replace-hr-data',
        component: ReplaceHRDataComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'user-login-status',
        component: UserActivityComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'user-recent-activity',
        component: UserRecentActivityComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    },
    {
        path: 'backup',
        component: BackupComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_ADMIN'],
        }
    }
] as Routes;
