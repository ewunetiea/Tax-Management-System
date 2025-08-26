import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HTTP_INTERCEPTORS,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, switchMap, filter, take } from 'rxjs/operators';
import { AuthService } from '../service/admin/auth.service';
import { StorageService } from '../service/admin/storage.service';
import { EventBusService } from '../service/admin/event-bus.service';
import { EventData } from '../../models/admin/event-data';
import { MessageService } from 'primeng/api';

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject = new BehaviorSubject<boolean>(false);
  private shownErrors = new Set<string>();

  constructor(
    private storageService: StorageService,
    private authService: AuthService,
    private eventBusService: EventBusService,
    private messageService: MessageService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    req = req.clone({ withCredentials: true });

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        // Backend offline or unreachable
        if (error.status === 0) {
          this.handleBackendOffline(error);
          return throwError(() => error);
        }

        // Expired token (401) — skip for login requests
        if (!req.url.includes('auth/signin') && error.status === 401) {
          return this.handle401Error(req, next);
        }

        // Handle other errors globally
        this.handleHttpError(error);
        return throwError(() => error);
      })
    );
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(false);

      if (this.storageService.isLoggedIn()) {
        return this.authService.refreshToken().pipe(
          switchMap(() => {
            this.isRefreshing = false;
            this.refreshTokenSubject.next(true);
            return next.handle(request);
          }),
          catchError((error) => {
            this.isRefreshing = false;
            this.logoutUser();
            this.showError('Session Expired', 'Please log in again.');
            return throwError(() => error);
          })
        );
      } else {
        this.logoutUser();
        this.showError('Session Expired', 'Please log in again.');
        return throwError(() => new HttpErrorResponse({ error: 'Session expired', status: 401 }));
      }
    } else {
      // Queue requests until refresh finishes
      return this.refreshTokenSubject.pipe(
        filter(refreshed => refreshed),
        take(1),
        switchMap(() => next.handle(request))
      );
    }
  }

  private handleBackendOffline(error: HttpErrorResponse) {
    if (!navigator.onLine) {
      this.showError('No Internet Connection', 'Please check your network.');
    } else if (error.error instanceof ProgressEvent) {
      this.showError('Server Unreachable', 'Unable to connect to the server.');
    } else {
      this.showError('Backend Not Running', 'The backend server cannot be reached.');
    }
  }

  private handleHttpError(error: HttpErrorResponse) {
    switch (error.status) {
      case 400:
        this.showError('Bad Request', 'The request was invalid.');
        break;
      case 403:
        this.showError('Forbidden', 'You do not have permission to perform this action.');
        break;
      case 404:
        this.showError('Not Found', 'The requested resource was not found.');
        break;
      case 408:
        this.showError('Request Timeout', 'The request timed out.');
        break;
      case 409:
        this.showError('Conflict', 'A conflict occurred.');
        break;
      case 429:
        this.showError('Too Many Requests', 'You have sent too many requests.');
        break;
      case 502:
        this.showError('Bad Gateway', 'Received an invalid response from the upstream server.');
        break;
      case 503:
        this.showError('Service Unavailable', 'The server is currently unavailable.');
        break;
      case 504:
        this.showError('Gateway Timeout', 'The server took too long to respond.');
        break;
      case 500:
        this.showError('Internal Server Error', 'An unexpected error occurred.');
        break;
      default:
        this.showError('Error', error.message || `Unexpected error occurred (Status: ${error.status})`);
        break;
    }
  }

  private logoutUser() {
    this.storageService.clean();
    this.eventBusService.emit(new EventData('logout', null));
  }

  private showError(summary: string, detail: string) {
    const key = `${summary}:${detail}`;
    if (this.shownErrors.has(key)) return;

    this.shownErrors.add(key);
    this.messageService.add({ severity: 'error', summary, detail, life: 3000 });

    setTimeout(() => {
      this.shownErrors.delete(key);
    }, 3000);
  }
}

export const httpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },
];
