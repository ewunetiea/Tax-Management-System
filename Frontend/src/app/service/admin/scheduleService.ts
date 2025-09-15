import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
const baseUrl = environment.backendUrl;

@Injectable({
    providedIn: 'root'
})
export class ScheduleService {
    constructor(private httpClient: HttpClient) {}

    getSchedules(): Observable<any> {
        return this.httpClient.get<any>(baseUrl + '/schedule');
    }

    updateScheduleStatus(schedule_status: any): Observable<any> {
        return this.httpClient.put<any>(baseUrl + '/schedule', JSON.stringify(schedule_status));
    }
}
