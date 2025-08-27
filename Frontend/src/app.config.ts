
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
import Aura from '@primeng/themes/aura';
import { providePrimeNG } from 'primeng/config';
import { appRoutes } from './app.routes';
import { httpInterceptorProviders } from './app/pages/helpers/http.interceptor';
import { MessageService } from 'primeng/api';

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
                (req, next) => {
                    // 👇 Add withCredentials for all requests
                    const cloned = req.clone({ withCredentials: true });
                    return next(cloned);
                }
            ])
        ),
        provideAnimationsAsync(),
        providePrimeNG({ theme: { preset: Aura, options: { darkModeSelector: '.app-dark' } } }),
        httpInterceptorProviders,
        MessageService   // 👈 add MessageService as provider

    ]
};
