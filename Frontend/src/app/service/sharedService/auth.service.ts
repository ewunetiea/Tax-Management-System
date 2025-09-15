import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WebSocketService } from './WebSocketService';
import { User } from '../../models/admin/user';
import { Password } from '../../models/admin/password';
import { environment } from '../../../environments/environment.prod';
const AUTH_API = environment.backendUrl + '/auth';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    withCredentials: true
};

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    constructor(
        private http: HttpClient,
        private webSocketService: WebSocketService
    ) {}

    login(username: string, password: string, userAgent: any): Observable<any> {
        return this.http.post(
            AUTH_API + '/signin',
            {
                username,
                password,
                userAgent
            },
            httpOptions
        );
    }

    forceLogin(username: string, password: string, userAgent: any): Observable<any> {
        return this.http.post(
            AUTH_API + '/force-login',
            {
                username,
                password,
                userAgent
            },
            httpOptions
        );
    }

    logout(id_login_tracker: any): Observable<any> {
        this.webSocketService.disconnect();
        return this.http.get(`${AUTH_API}/signout/${id_login_tracker}`, httpOptions);
    }

    changePassword(password: Password): Observable<any> {
        return this.http.post(AUTH_API + '/changePassword', { password }, httpOptions);
    }

    refreshToken(): Observable<any> {
        return this.http.post(AUTH_API + '/refreshtoken', {}, httpOptions);
    }

    signup(user: User): Observable<any> {
        return this.http.post(AUTH_API + '/signup', user, httpOptions);
    }
}
