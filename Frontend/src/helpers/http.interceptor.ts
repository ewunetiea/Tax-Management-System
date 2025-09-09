import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HTTP_INTERCEPTORS, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { EventData } from '../app/models/admin/event-data';
import { AuthService } from '../app/pages/service/admin/auth.service';
import { EventBusService } from '../app/pages/service/admin/event-bus.service';
import { StorageService } from '../app/pages/service/admin/storage.service';

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
    private isRefreshing = false;

    constructor(
        private storageService: StorageService,
        private authService: AuthService,
        private eventBusService: EventBusService
    ) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        req = req.clone({
            withCredentials: true,
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });

        return next.handle(req).pipe(
            catchError((error) => {
                if (error instanceof HttpErrorResponse && !req.url.includes('auth/signin') && error.status === 401) {
                    return this.handle401Error(req, next);
                }
                if ((error.status === 0 && error.error instanceof ErrorEvent) || error.error instanceof ProgressEvent) {
                    // Use this if backend is in remote
                    if (!window.navigator.onLine) {
                        return throwError(() => {
                            const error: HttpErrorResponse = new HttpErrorResponse({
                                error: new Error('Not internet connection'),
                                status: 0
                            });
                            // const error: any = new Error(`Not connected to the internet`);
                            // error.error.message = "Not connected to the internet";
                            // error.timestamp = Date.now();
                            return error;
                            // throw new Error("Not connected to the internet");
                        });
                    }
                    return throwError(() => {
                        const error: HttpErrorResponse = new HttpErrorResponse({
                            error: new Error('Server not available'),
                            status: 0
                        });
                        // error.error.message = "Connection Error!";
                        // error.timestamp = Date.now();
                        return error;
                        // throw new Error("Connection Error!");
                    });
                } else if (error instanceof HttpErrorResponse && error.status === 400) {
                    return this.handle400Error(req, next);
                } else if (error instanceof HttpErrorResponse && error.status === 404) {
                    return this.handle404Error(req, next);
                } else if (error instanceof HttpErrorResponse && error.status === 500) {
                    return this.handle500Error(req, next);
                }

                return throwError(() => error);
            })
        );
    }

    private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
        if (!this.isRefreshing) {
            this.isRefreshing = true;
            if (this.storageService.isLoggedIn()) {
                return this.authService.refreshToken().pipe(
                    switchMap(() => {
                        this.isRefreshing = false;
                        return next.handle(request);
                    }),
                    catchError((error: HttpErrorResponse) => {
                        this.isRefreshing = false;
                        if (error.status == 403) {
                            this.eventBusService.emit(new EventData('logout', null));
                        }
                        return throwError(() => {
                            const error: HttpErrorResponse = new HttpErrorResponse({
                                error: new Error('You have no permission!'),
                                status: 401
                            });
                            // const error: any = new Error(`Not connected to the internet`);
                            // error.error.message = "Not connected to the internet";
                            // error.timestamp = Date.now();
                            return error;
                            // throw new Error("Not connected to the internet");
                        });
                    })
                );
            }
        }
        return next.handle(request);
    }
    private handle500Error(request: HttpRequest<any>, next: HttpHandler) {
        return throwError(() => {
            const error: HttpErrorResponse = new HttpErrorResponse({
                error: new Error('Internal server error!'),
                status: 500
            });
            // const error: any = new Error(`Not connected to the internet`);
            // error.error.message = "Not connected to the internet";
            // error.timestamp = Date.now();
            return error;
            // throw new Error("Not connected to the internet");
        });
    }
    private handle404Error(request: HttpRequest<any>, next: HttpHandler) {
        return throwError(() => {
            const error: HttpErrorResponse = new HttpErrorResponse({
                error: new Error('Not found!'),
                status: 404
            });
            // const error: any = new Error(`Not connected to the internet`);
            // error.error.message = "Not connected to the internet";
            // error.timestamp = Date.now();
            return error;
            // throw new Error("Not connected to the internet");
        });
    }
    private handle400Error(request: HttpRequest<any>, next: HttpHandler) {
        return throwError(() => {
            const error: HttpErrorResponse = new HttpErrorResponse({
                error: new Error('Error'),
                status: 400
            });
            // const error: any = new Error(`Not connected to the internet`);
            // error.error.message = "Not connected to the internet";
            // error.timestamp = Date.now();
            return error;
            // throw new Error("Not connected to the internet");
        });
    }
}
export const httpInterceptorProviders = [{ provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true }];
