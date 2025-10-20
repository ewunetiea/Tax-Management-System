import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { RecentActivity } from '../../models/admin/recent-activity';
const rootURL = environment.backendUrl;
@Injectable({
    providedIn: 'root'
})
export class RecentActivityService {
    constructor(private http: HttpClient) {}

    addRecentActivity(data: any): Observable<any> {
        return this.http.post(`${rootURL}/addRecentActivity`, data);
    }

    getRecentActivity(id: number): Observable<any> {
        return this.http.get(`${rootURL}/getRecentActivity/${id}`);
    }

    getRecentActivityAdmin(report: any): Observable<any> {
        return this.http.post(`${rootURL}/getRecentActivityAdmin`, report);
    }

    getAllRecentActivityByUserId(report: any): Observable<any> {
        return this.http.post(`${rootURL}/getAllRecentActivityByUserId`, report);
    }

    getActivityByDateAndContent(report: any): Observable<any> {
        return this.http.post(`${rootURL}/getActivity`, report);
    }

  getRecentAllActivity(recentActivity: RecentActivity): Observable<any> {
    return this.http.post(`${rootURL}/get_all_Activity`, recentActivity);
  }


  getUsersForActivity(): Observable<any> {
    return this.http.get(`${rootURL}/users_activity`);
  }
}
