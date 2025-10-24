import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
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

getPieChartData(userId: number): Observable<number[]> {
  const params = new HttpParams().set('userId', userId.toString());

  return this.http.get<{ [key: string]: number }>(`${apiUrl}/dougnut`, { params })
    .pipe(
      map(data => Object.values(data)) // convert Map to number[]
    );
}
  // Get Bar Chart Data
getBranchPerRegion(): Observable<{ regions: string[]; branchCounts: number[] }> {
  return this.http.get<{ regions: string[]; branchCounts: number[] }>(`${apiUrl}/branch-per-region`);
}

  
 

  // Get Recent Activity
  getRecentActivity(userId: number): Observable<RecentActivity[]> {
    return this.http.get<RecentActivity[]>(`${apiUrl}/recent-activity/${userId}`);
  }
}
