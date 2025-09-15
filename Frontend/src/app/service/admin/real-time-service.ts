import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
const rootURL = environment.backendUrl;

@Injectable({
    providedIn: 'root'
})
export class RealTimeService {
    constructor(private http: HttpClient) {}

    insertRealTimeInfo(data: any): Observable<any> {
        return this.http.post(`${rootURL}/insertRealTimeInfo`, data);
    }

    updateRealTimeInfo(data: any): Observable<any> {
        return this.http.post(`${rootURL}/updateRealTimeInfo`, data);
    }

    getRealTimeByUserId(data: any): Observable<any> {
        return this.http.post(`${rootURL}/getRealTimeByUserId`, data);
    }
}
