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

    // req = req.clone({
    //     withCredentials: true,
    //     setHeaders: { 'Content-Type': 'application/json' }
    // });

    req = req.clone({
        withCredentials: true,
        setHeaders: req.body instanceof FormData ? {} : { 'Content-Type': 'application/json' }
    });


    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            console.error('HTTP Error:', error);

            if (error instanceof HttpErrorResponse && req.url.includes('auth/signin') && error.status === 500) {
                let toastMessage = 'Invalid credentials. Please enter correct credentials!';

                // Check backend error message
                if (error.error && error.error.message) {
                    const backendMsg: string = error.error.message;

                    if (backendMsg.includes('User is disabled')) {
                        toastMessage = 'Your account is inactive. Please contact the system administrator!';
                    }
                }

                const customError = new HttpErrorResponse({
                    error: new Error(toastMessage),
                    status: 0
                });

                showToast(messageService, customError.error.message, 'error');
                return throwError(() => customError);
            }



            if (req.url.includes('auth/signin') && error.status === 500) {
                let toastMessage = 'An unexpected error occurred. Please try again!';

                if (error.error && error.error.message) {
                    const backendMsg: string = error.error.message;

                    if (backendMsg.includes('authentication')) {
                        toastMessage = 'Invalid credentials. Please enter correct credentials!';
                    } else {
                        // fallback to backend message
                        toastMessage = backendMsg;
                    }
                }

                showToast(messageService, toastMessage, 'error');
                return throwError(() => error);
            }


            //   if (error instanceof HttpErrorResponse && req.url.includes('auth/signin') && error.status === 500) {
            //     const customError = new HttpErrorResponse({
            //         error: new Error('Invalid credentials. Please enter correct credentials!'),
            //         status: 0
            //     });
            //     showToast(messageService, customError.error.message, 'error');
            //     return throwError(() => customError);
            // }

            // An unexpected error occurred: User is disabled
            // An error occurred during authentication. Please try again later.

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
//             if (error instanceof HttpErrorResponse && error.status === 401) {

// console.log("____________________badd__Credr_______________________")
//                 console.log(error.message)
//                 const customError = new HttpErrorResponse({
//                     error: new Error('Unauthorized. Please login again!'),
//                     status: 400
//                 });
//                 showToast(messageService, customError.error.message, 'error');
//                 return throwError(() => customError);
//             }





if (error instanceof HttpErrorResponse && error.status === 401) {

    const backendMessage = error.error?.message;

    if (backendMessage === 'Bad credentials') {
        showToast(
            messageService,
            'Invalid username or password',
            'error'
        );
    } else {
        showToast(
            messageService,
            'Unauthorized. Please login again!',
            'error'
        );
    }

    return throwError(() => error);
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

            // if (error.status === 409) {
            //     // Custom frontend message
            //     const customMsg = 'File name already exists. Please rename or upload another';

            //     // Show toast
            //     showToast(messageService, customMsg, 'error');

            //     // Create a new HttpErrorResponse with your custom message
            //     const modifiedError = new HttpErrorResponse({
            //         error: { message: customMsg },
            //         headers: '' as any,
            //         status: error.status,
            //         statusText: " File name  already exists Please rename your file name",
            //         url: ''
            //     });

            //     // Re-throw the modified error
            //     return throwError(() => modifiedError);
            // }

            if (error.status === 409) {

                const customMessage = 'File name already exists. Please rename or upload another file.';

                // Show toast as WARNING
                messageService.add({
                    severity: 'warn',        // 🔹 changed
                    summary: 'Duplicate File',
                    detail: customMessage,
                    life: 4000
                });

                // Create custom clean error
                const modifiedError = new HttpErrorResponse({
                    error: { message: customMessage },
                    status: 409,
                    statusText: customMessage,
                    url: 'duplicate-file',
                });

                // Override Angular internal message
                (modifiedError as any).message = customMessage;
                (modifiedError as any)._message = customMessage;

                return throwError(() => modifiedError);
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


function showToast2(
    messageService: MessageService,
    detail: string,
    severity: 'error' | 'warn' | 'info' | 'success'
) {
    messageService.add({
        severity,
        summary: severity === 'error' ? 'Error' : 'Warning',
        detail,
        life: 4000
    });
}
