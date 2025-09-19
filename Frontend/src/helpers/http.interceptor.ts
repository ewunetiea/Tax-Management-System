import { HttpInterceptorFn, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { EventData } from '../app/models/admin/event-data';
import { AuthService } from '../app/service/sharedService/auth.service';
import { EventBusService } from '../app/service/sharedService/event-bus.service';
import { StorageService } from '../app/service/sharedService/storage.service';

let isRefreshing = false; // keep same as your class field

export const httpRequestInterceptor: HttpInterceptorFn = (req, next) => {
    const storageService = inject(StorageService);
    const authService = inject(AuthService);
    const eventBusService = inject(EventBusService);

    req = req.clone({
        withCredentials: true,
        setHeaders: { 'Content-Type': 'application/json' }
    });


    return next(req).pipe(
        catchError((error) => {

            console.log(error.message)
            if (error instanceof HttpErrorResponse && req.url.includes('auth/signin') && error.status === 500) {

                return throwError(() => {
                    const err: HttpErrorResponse = new HttpErrorResponse({
                        error: new Error('Invalid credentials. Please enter correct credentaials !'),
                        status: 0
                    });
                    return err;
                });
            }
            if (error instanceof HttpErrorResponse && !req.url.includes('auth/signin') && error.status === 401) {
                return handle401Error(req, next, storageService, authService, eventBusService);
            }


            if ((error.status === 0 || error.error instanceof TypeError) || error.error instanceof ErrorEvent) {
                if (!window.navigator.onLine) {
                    return throwError(() => {
                        const err: HttpErrorResponse = new HttpErrorResponse({
                            error: new Error('Not internet connection'),
                            status: 0
                        });
                        return err;
                    });
                }
                return throwError(() => {
                    const err: HttpErrorResponse = new HttpErrorResponse({
                        error: new Error('Server not available'),
                        status: 0
                    });
                    return err;
                });
            }
            else if (error instanceof HttpErrorResponse && error.status === 400) {
                return handle400Error();
            } else if (error instanceof HttpErrorResponse && error.status === 404) {
                return handle404Error();
            } else if (error instanceof HttpErrorResponse && error.status === 500) {
                return handle500Error();
            }
            return throwError(() => error);
        })
    );
};


function handle401Error(
    request: HttpRequest<any>,
    next: any,
    storageService: StorageService,
    authService: AuthService,
    eventBusService: EventBusService
): Observable<any> {
    if (!isRefreshing) {
        isRefreshing = true;
        if (storageService.isLoggedIn()) {
            return authService.refreshToken().pipe(
                switchMap(() => {
                    isRefreshing = false;
                    return next(request);
                }),
                catchError((error: HttpErrorResponse) => {
                    isRefreshing = false;
                    if (error.status == 403) {
                        eventBusService.emit(new EventData('logout', null));
                    }
                    return throwError(() => {
                        const err: HttpErrorResponse = new HttpErrorResponse({
                            error: new Error('You have no permission!'),
                            status: 401
                        });
                        return err;
                    });
                })
            );
        }
    }
    return next(request);
}

function handle500Error() {
    return throwError(() => {
        const err: HttpErrorResponse = new HttpErrorResponse({
            error: new Error('Internal server error!'),
            status: 500
        });
        return err;
    });
}

function handle404Error() {
    return throwError(() => {
        const err: HttpErrorResponse = new HttpErrorResponse({
            error: new Error('Not found!'),
            status: 404
        });
        return err;
    });
}

function handle400Error() {
    return throwError(() => {
        const err: HttpErrorResponse = new HttpErrorResponse({
            error: new Error('Error'),
            status: 400
        });
        return err;
    });
}
