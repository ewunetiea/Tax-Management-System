import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecentActivity } from '../../models/admin/recent-activity';
import { environment } from '../../../environments/environment.prod';
const apiUrl = environment.backendUrl + '/admin/dashboard';

@Injectable({
  providedIn: 'root'
})
export class AdminDashboardService {


  constructor(private http: HttpClient) { }

  // Get Card Data
  getCardData(): Observable<number[]> {
    return this.http.get<number[]>(`${apiUrl}/card-data`);
  }

  // Get Polar Data
  getPolarData(userId: number): Observable<number[]> {
    const params = new HttpParams().set('userId', userId.toString());
    return this.http.get<number[]>(`${apiUrl}/polar-data`, { params });
  }

  // Get Bar Chart Data
  getBarChartData(): Observable<number[]> {
    return this.http.get<number[]>(`${apiUrl}/bar-chart`);
  }

  // Get Horizontal Bar Chart Data
  getHorizontalBarChartData(): Observable<number[]> {
    return this.http.get<number[]>(`${apiUrl}/horizontal-bar-chart`);
  }

  // Get Line Chart Data per Region
  getLineChartData(): Observable<Map<string, number[]>> {
    return this.http.get<Map<string, number[]>>(`${apiUrl}/line-chart-data`);
  }

  // Get Recent Activity
  getRecentActivity(userId: number): Observable<RecentActivity[]> {
    return this.http.get<RecentActivity[]>(`${apiUrl}/recent-activity/${userId}`);
  }
}
