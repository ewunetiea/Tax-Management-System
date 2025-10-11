import { Routes } from '@angular/router';
import { MakerDashboard } from '../dashboard/maker-dashboard/maker-dashboard';
import { Crud } from './crud/crud';
import { ManageTax } from './tax/manage-tax';
import { AccountDataComponent } from './account-data/account-data.component';
import { TransactionComponent } from './transaction/transaction.component';
import { ManageTaxCategoryComponent } from './tax-category/manage-tax-category/manage-tax-category.component';
import { AuthGuard } from '../../service/admin/auth.guard';
import { TaxCreateEditComponent } from './tax-rule/tax-create-edit.component';

export default [
    // { path: 'button', data: { breadcrumb: 'Button' }, component: ButtonDemo },
    { path: 'charts', data: { breadcrumb: 'Charts' }, component: MakerDashboard },
    { path: 'manage-customer', data: { breadcrumb: 'Manage Customer' }, component: Crud },
    { path: 'manage-tax', data: { breadcrumb: 'Tax Management' }, component: ManageTax },
    { path: 'tax', data: { breadcrumb: 'Tax Form' }, component: TaxCreateEditComponent },
    { path: 'account', data: { breadcrumb: 'Tax Form' }, component: AccountDataComponent },
    { path: 'transaction', data: { breadcrumb: 'Tax Form' }, component: TransactionComponent },

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
