import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { Documentation } from './app/pages/documentation/documentation';
import { Notfound } from './app/pages/notfound/notfound';
import { AuthGuard } from './app/pages/service/admin/auth.guard';
import { LoginComponent } from './app/pages/login/login.component';
import { DashboardContainerComponent } from './app/pages/dashboard/dashboard-container/dashboard-container.component';
import { Login } from './app/pages/auth/login';
import { UserProfileComponent } from './app/pages/admin/user/user-profile/user-profile.component';
import { ManageContactComponent } from './app/pages/admin/user/manage-contact/manage-contact.component';
import { AdminNotificationComponent } from './app/pages/admin/notification/admin-notification/admin-notification.component';
import { ForgetPasswordComponent } from './app/pages/admin/forget-password/forget-password.component';
import { SignupComponent } from './app/pages/admin/signup/signup.component';

export const appRoutes: Routes = [
    // {   path: '',
    //     component: AppLayout,
    //     children: [
    //         { path: '', component: Dashboard },
    //         { path: 'uikit', loadChildren: () => import('./app/pages/uikit/uikit.routes') },
    //         { path: 'documentation', component: Documentation },
    //         { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') }
    //     ],
    // },
    // { path: 'landing', component: Landing },
    // { path: 'notfound', component: Notfound },
    // { path: 'auth', loadChildren: () => import('./app/pages/auth/auth.routes') },
    // { path: '**', redirectTo: '/notfound' },

    { path: '', component: Login },
    // { path: '', component: LoginComponent },
    { path: 'forget-password', component: ForgetPasswordComponent } ,
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
            { path: 'user-profile', component: UserProfileComponent } ,
            { path: 'manage-contact', component: ManageContactComponent } ,
            { path: 'admin-notification', component: AdminNotificationComponent } ,
            { path: 'uikit', loadChildren: () => import('./app/pages/uikit/uikit.routes') },
            { path: 'documentation', component: Documentation },
            { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') }
        ],
    },
    { path: 'pages-login', component: LoginComponent },
    { path: 'notfound', component: Notfound },
    { path: '**', redirectTo: 'notfound' }
];

