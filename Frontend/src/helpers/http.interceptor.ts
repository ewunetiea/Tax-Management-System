import { HttpInterceptorFn, HttpRequest, HttpErrorResponse, HttpEvent } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { EventData } from '../app/models/admin/event-data';
import { AuthService } from '../app/service/sharedService/auth.service';
import { EventBusService } from '../app/service/sharedService/event-bus.service';
import { StorageService } from '../app/service/sharedService/storage.service';
import { MessageService } from 'primeng/api';

let isRefreshing = false;

export const httpRequestInterceptor: HttpInterceptorFn = (req, next) => {
    const storageService = inject(StorageService);
    const authService = inject(AuthService);
    const eventBusService = inject(EventBusService);
    const messageService = inject(MessageService);

    req = req.clone({
        withCredentials: true,
        setHeaders: { 'Content-Type': 'application/json' }
    });

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            console.error('HTTP Error:', error);

            // 🔹 Signin error
            if (error instanceof HttpErrorResponse && req.url.includes('auth/signin') && error.status === 500) {
                const customError = new HttpErrorResponse({
                    error: new Error('Invalid credentials. Please enter correct credentials!'),
                    status: 0
                });
                showToast(messageService, customError.error.message, 'error');
                return throwError(() => customError);
            }

            // 🔹 Unauthorized
            if (error instanceof HttpErrorResponse && !req.url.includes('auth/signin') && error.status === 401) {
                return handle401Error(req, next, storageService, authService, eventBusService, messageService);
            }

            // 🔹 Offline or server not available
            if ((error.status === 0 || error.error instanceof TypeError) || error.error instanceof ErrorEvent) {
                if (!window.navigator.onLine) {
                    const customError = new HttpErrorResponse({
                        error: new Error('No internet connection'),
                        status: 0
                    });
                    showToast(messageService, customError.error.message, 'error');
                    return throwError(() => customError);
                }

                const customError = new HttpErrorResponse({
                    error: new Error('Server not available'),
                    status: 0
                });
                showToast(messageService, customError.error.message, 'error');
                return throwError(() => customError);
            }

            // 🔹 Bad request
            if (error instanceof HttpErrorResponse && error.status === 400) {
                const customError = new HttpErrorResponse({
                    error: new Error('Bad request. Please check your input and try again'),
                    status: 400
                });
                showToast(messageService, customError.error.message, 'error');
                return throwError(() => customError);
            }

            // 🔹 Unauthorized request
            if (error instanceof HttpErrorResponse && error.status === 401) {
                const customError = new HttpErrorResponse({
                    error: new Error('Unauthorized. Please login again!'),
                    status: 400
                });
                showToast(messageService, customError.error.message, 'error');
                return throwError(() => customError);
            }

            // 🔹 Forbidden request
            if (error instanceof HttpErrorResponse && error.status === 403) {
                const customError = new HttpErrorResponse({
                    error: new Error('Forbidden request. You are not allowed to perform this action'),
                    status: 400
                });
                showToast(messageService, customError.error.message, 'error');
                return throwError(() => customError);
            }

            // 🔹 Not found
            if (error instanceof HttpErrorResponse && error.status === 404) {
                const customError = new HttpErrorResponse({
                    error: new Error('Requested resource not found'),
                    status: 404
                });
                showToast(messageService, customError.error.message, 'error');
                return throwError(() => customError);
            }

            // 🔹 Internal server error
            if (error instanceof HttpErrorResponse && error.status === 500) {
                const customError = new HttpErrorResponse({
                    error: new Error('Internal server error!'),
                    status: 500
                });
                
                showToast(messageService, customError.error.message, 'error');
                return throwError(() => customError);
            }

            // 🔹 Fallback
            const fallbackError = new HttpErrorResponse({
                error: new Error(error?.message || 'Unexpected error occurred'),
                status: error.status || 0
            });

            showToast(messageService, fallbackError.error.message, 'error');
            return throwError(() => fallbackError);
        })
    );
};

// 🔹 Handle 401 Unauthorized with token refresh
function handle401Error(
    request: HttpRequest<any>,
    next: (req: HttpRequest<any>) => Observable<HttpEvent<any>>,
    storageService: StorageService,
    authService: AuthService,
    eventBusService: EventBusService,
    messageService: MessageService
): Observable<HttpEvent<any>> {
    if (!isRefreshing) {
        isRefreshing = true;
        if (storageService.isLoggedIn()) {
            return authService.refreshToken().pipe(
                switchMap((): Observable<HttpEvent<any>> => {
                    isRefreshing = false;
                    return next(request);
                }),
                catchError((error: HttpErrorResponse) => {
                    isRefreshing = false;
                    if (error.status === 403) {
                        eventBusService.emit(new EventData('logout', null));
                    }
                    showToast(messageService, 'You have no permission!', 'error');
                    return throwError(() => error);
                })
            );
        }
    }
    return next(request);
}

// 🔹 Helper function for showing PrimeNG toast
function showToast(
    messageService: MessageService,
    detail: string,
    severity: 'error' | 'warn' | 'info' | 'success'
) {
    messageService.add({
        severity,
        summary: severity === 'error' ? 'Error' : severity === 'warn' ? 'Warning' : 'Info',
        detail,
        life: 4000
    });
}
