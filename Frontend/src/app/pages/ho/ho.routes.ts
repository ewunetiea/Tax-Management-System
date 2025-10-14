import { Routes } from '@angular/router';
import { MakerDashboard } from '../dashboard/maker-dashboard/maker-dashboard';

import { AuthGuard } from '../../service/admin/auth.guard';
import { ManageTax } from '../maker/tax/manage-tax/manage-tax';
import { AnnouncementComponent } from './announcement/announcement.component';
import { ManageTaxHoComponent } from './manage-tax-ho/manage-tax-ho.component';
import { ManageTaxCategoryComponent } from './tax-category/manage-tax-category/manage-tax-category.component';

export default [
    {
        path: 'ongoing-announcement',
        component: AnnouncementComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_HO'],
            status: 'ongoing'
        }
    },

    {
        path: 'archived-announcement',
        component: AnnouncementComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_HO'],
            status: 'archived'
        }
    },

    {
        path: 'manage-tax-category',
        component: ManageTaxCategoryComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_HO'],
        }
    },

    {
        path: 'manage-tax-ho/:status',
        component: ManageTaxHoComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_HO']
        }
    },
] as Routes;
