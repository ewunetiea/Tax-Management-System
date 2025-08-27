import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environments/environment.prod';
import { Observable } from 'rxjs';
import { RecentActivity } from 'app/models/shared/recent_activity/recent-activity';

const rootURL = environment.backendUrl;
@Injectable({
  providedIn: 'root',
})
export class RecentActivityService {
  constructor(private http: HttpClient) { }

  addRecentActivity(data: any): Observable<any> {
    return this.http.post(`${rootURL}/addRecentActivity`, data);
  }


  getRecentAllActivity(recentActivity: RecentActivity): Observable<any> {
    return this.http.post(`${rootURL}/get_all_Activity`, recentActivity);
  }

  getRecentActivityAdmin(report: any): Observable<any> {
    return this.http.post(`${rootURL}/getRecentActivityAdmin`, report);
  }

  getUsersForActivity(): Observable<any> {
    return this.http.get(`${rootURL}/users_activity`);
  }
}
