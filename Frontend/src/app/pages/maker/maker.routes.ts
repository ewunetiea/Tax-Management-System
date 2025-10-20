import { Routes } from '@angular/router';
import { MakerDashboard } from '../dashboard/maker-dashboard/maker-dashboard';
import { Crud } from './crud/crud';
import { ManageTax } from './tax/manage-tax/manage-tax';
import { AuthGuard } from '../../service/admin/auth.guard';
import { TaxCreateEditComponent } from './tax/tax-create-edit/tax-create-edit.component';

export default [
    { path: 'charts', data: { breadcrumb: 'Charts' }, component: MakerDashboard },
    { path: 'manage-customer', data: { breadcrumb: 'Manage Customer' }, component: Crud },
    {
        path: 'drafted-tax', component: ManageTax,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'drafted'
        }
    },

     {
        path: 'submited-tax', component: ManageTax,
        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'submited'
        }
    },

    
     {
        path: 'approved-tax', component: ManageTax,

        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'approved'
        }
    },


     {
        path: 'rejected-tax', component: ManageTax,

        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'rejected'
        }
    },
     {
        path: 'general-tax-status', component: ManageTax,

        canActivate: [AuthGuard],
        data: {
            roles: ['ROLE_MAKER'],
            status:'generalstatus'
        }
    },
    { path: 'tax', data: { breadcrumb: 'Tax Form' }, component: TaxCreateEditComponent },

    // { path: 'file', data: { breadcrumb: 'File' }, component: FileDemo },
    // { path: 'formlayout', data: { breadcrumb: 'Form Layout' }, component: FormLayoutDemo },
    // { path: 'input', data: { breadcrumb: 'Input' }, component: InputDemo },
    // { path: 'list', data: { breadcrumb: 'List' }, component: ListDemo },
    // { path: 'media', data: { breadcrumb: 'Media' }, component: MediaDemo },
    // { path: 'message', data: { breadcrumb: 'Message' }, component: MessagesDemo },
    // { path: 'misc', data: { breadcrumb: 'Misc' }, component: MiscDemo },
    // { path: 'panel', data: { breadcrumb: 'Panel' }, component: PanelsDemo },
    // { path: 'timeline', data: { breadcrumb: 'Timeline' }, component: TimelineDemo },
    // { path: 'table', data: { breadcrumb: 'Table' }, component: TableDemo },
    // { path: 'overlay', data: { breadcrumb: 'Overlay' }, component: OverlayDemo },
    // { path: 'tree', data: { breadcrumb: 'Tree' }, component: TreeDemo },
    // { path: 'menu', data: { breadcrumb: 'Menu' }, component: MenuDemo },
    // { path: '**', redirectTo: '/notfound' }
] as Routes;
