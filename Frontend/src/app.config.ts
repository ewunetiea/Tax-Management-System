// import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
// import { ApplicationConfig } from '@angular/core';
// import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
// import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
// import Aura from '@primeng/themes/aura';
// import { providePrimeNG } from 'primeng/config';
// import { appRoutes } from './app.routes';
// import { MessageService } from 'primeng/api';
// import { httpInterceptorProviders } from './helpers/http.interceptor';
// import { TimeagoClock, TimeagoDefaultClock, TimeagoDefaultFormatter, TimeagoFormatter, TimeagoIntl } from 'ngx-timeago';

// export const appConfig: ApplicationConfig = {
//     providers: [
//         provideRouter(appRoutes, withInMemoryScrolling({ anchorScrolling: 'enabled', scrollPositionRestoration: 'enabled' }), withEnabledBlockingInitialNavigation()),
//         provideHttpClient(
//             withFetch(),
//             withInterceptors([
//                 (req, next) => {
//                     // 👇 Add withCredentials for all requests
//                     const cloned = req.clone({ withCredentials: true });
//                     return next(cloned);
//                 }
//             ])
//         ),
//         provideAnimationsAsync(),
//         providePrimeNG({ theme: { preset: Aura, options: { darkModeSelector: '.app-dark' } } }),
//         httpInterceptorProviders,
//         MessageService,

//         { provide: TimeagoFormatter, useClass: TimeagoDefaultFormatter },
//         { provide: TimeagoClock, useClass: TimeagoDefaultClock },
//         TimeagoIntl
//     ]
// };



import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
import Aura from '@primeng/themes/aura';
import { providePrimeNG } from 'primeng/config';
import { appRoutes } from './app.routes';
import { MessageService } from 'primeng/api';
import { TimeagoClock, TimeagoDefaultClock, TimeagoDefaultFormatter, TimeagoFormatter, TimeagoIntl } from 'ngx-timeago';
import { httpRequestInterceptor } from './helpers/http.interceptor';

export const appConfig: ApplicationConfig = {
    providers: [
        provideRouter(
            appRoutes,
            withInMemoryScrolling({ anchorScrolling: 'enabled', scrollPositionRestoration: 'enabled' }),
            withEnabledBlockingInitialNavigation()
        ),
        provideHttpClient(
            withFetch(),
            withInterceptors([
                httpRequestInterceptor // ✅ use your functional interceptor here
            ])
        ),
        provideAnimationsAsync(),
        providePrimeNG({ theme: { preset: Aura, options: { darkModeSelector: '.app-dark' } } }),
        MessageService,

        { provide: TimeagoFormatter, useClass: TimeagoDefaultFormatter },
        { provide: TimeagoClock, useClass: TimeagoDefaultClock },
        TimeagoIntl
    ]
};
