import { Routes } from '@angular/router';
import { MakerDashboard } from '../dashboard/maker-dashboard/maker-dashboard';
import { ManageTaxComponent } from './tax/manage-tax/manage-tax.component';
import { AuthGuard } from '../../service/admin/auth.guard';
import { TaxCreateEditComponent } from './tax/tax-create-edit/tax-create-edit.component';

export default [
    { path: 'charts', data: { breadcrumb: 'Charts' }, component: MakerDashboard },
            
            {
        path: 'drafted-tax', component: ManageTaxComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'drafted'
        }
    },

     {
        path: 'submited-tax', component: ManageTaxComponent,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'submited'
        }
    },


    
     {
        path: 'sent-tax', component: ManageTaxComponent,

        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'sent'
        }
    },
    
     {
        path: 'setteled-tax', component: ManageTaxComponent,

        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'setteled'
        }
    },


     {
        path: 'rejected-tax', component: ManageTaxComponent,

        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'rejected'
        }
    },
     {
        path: 'general-tax-status', component: ManageTaxComponent,

        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'generalstatus'
        }
    },
    { path: 'tax', data: { breadcrumb: 'Tax Form' }, component: TaxCreateEditComponent },

   
] as Routes;
