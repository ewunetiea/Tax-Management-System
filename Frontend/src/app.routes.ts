import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Notfound } from './app/pages/notfound/notfound';
import { DashboardContainerComponent } from './app/pages/dashboard/dashboard-container/dashboard-container.component';
import { Login } from './app/pages/auth/login';
import { UserProfileComponent } from './app/pages/admin/user/user-profile/user-profile.component';
import { ManageContactComponent } from './app/pages/admin/user/manage-contact/manage-contact.component';
import { AdminNotificationComponent } from './app/pages/admin/notification/admin-notification/admin-notification.component';
import { ForgetPasswordComponent } from './app/pages/admin/forget-password/forget-password.component';
import { SignupComponent } from './app/pages/auth/signup/signup.component';
import { AuthGuard } from './app/service/admin/auth.guard';

export const appRoutes: Routes = [
    { path: '', component: Login },
    // { path: '', component: LoginComponent },
    { path: 'forget-password', component: ForgetPasswordComponent },
    { path: 'signup', component: SignupComponent },

    {
        path: 'applayout',
        component: AppLayout,
        canActivate: [AuthGuard],
        children: [
            // { path: '', component: Dashboard },
            { path: '', component: DashboardContainerComponent },
            {
                path: 'admin',
                loadChildren: () => import('./app/pages/admin/admin.routes').then(m => m.default),
                canActivate: [AuthGuard]
            },
            {
                path: 'maker',
                loadChildren: () => import('./app/pages/maker/maker.routes').then(m => m.default),
                canActivate: [AuthGuard]
            }, 

            {
                path: 'reviewer',
                loadChildren: () => import('./app/pages/reviewer/checker.routes').then(m => m.default),
                canActivate: [AuthGuard]
            },

              {
                path: 'approver',
                loadChildren: () => import('./app/pages/approver/approver.routes').then(m => m.default),
                canActivate: [AuthGuard]
            }, 
            { path: 'user-profile', component: UserProfileComponent },
            { path: 'manage-contact', component: ManageContactComponent },
            { path: 'admin-notification', component: AdminNotificationComponent },
            { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') }
        ],
    },

 {
        path: 'applayout',
        component: AppLayout,
        canActivate: [AuthGuard],
        children: [
            // { path: '', component: Dashboard },
            { path: '', component: DashboardContainerComponent },
            { path: 'user-profile', component: UserProfileComponent },
            { path: 'manage-contact', component: ManageContactComponent },
            { path: 'admin-notification', component: AdminNotificationComponent },
            { path: 'maker', loadChildren: () => import('./app/pages/maker/maker.routes') },
            { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') }
        ],
    },
    { path: 'notfound', component: Notfound },
    { path: '**', redirectTo: 'notfound' }
];

