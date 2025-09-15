import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { MaxFailedAndJwtControl } from '../../models/admin/max-failed-and-jwt-control';

const baseUrl = environment.backendUrl;

@Injectable({
    providedIn: 'root'
})
export class MaxFailedAndJwtControlService {
    constructor(private httpClient: HttpClient) {}

    getSystemSettings(): Observable<any> {
        return this.httpClient.get<any>(baseUrl + '/setting');
    }

    saveAccountSetting(faildControl: any): Observable<any> {
        return this.httpClient.post(baseUrl + '/setting/account', faildControl);
    }
    saveJWTSetting(faildControl: MaxFailedAndJwtControl): Observable<any> {
        return this.httpClient.post(baseUrl + '/setting/jwt', faildControl);
    }

    getAllLogRecord(): Observable<any> {
        return this.httpClient.get<any>(baseUrl + '/log');
    }
    deleteLog(logs: any[]): Observable<any> {
        return this.httpClient.post(baseUrl + '/log', logs);
    }
}
