import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecentActivity } from '../../models/admin/recent-activity';
import { environment } from '../../../environments/environment.prod';
const apiUrl = environment.backendUrl + '/maker/dashboard';


export interface PolarChartData {
  drafted: number;
  waiting: number;
  reviewed: number;
  approved: number;
}


export interface PieChartData {
  waiting: number;
  reviewer_rejected: number;
  approver_rejected: number;
}

@Injectable({
  providedIn: 'root'
})

export class MakerDashboardService {


  constructor(private http: HttpClient) { }

  // Get Card Data
  getCardData(): Observable<number[]> {
    return this.http.get<number[]>(`${apiUrl}/card-data`);
  }



  // Get Bar Chart Data
  getBarChartData(): Observable<number[][]> {
    return this.http.get<number[][]>(`${apiUrl}/bar-chart`);
  }

  // Get Horizontal Bar Chart Data

polarChartData(): Observable<PolarChartData> {
  return this.http.get<PolarChartData>(`${apiUrl}/polar-chart`);
}

  // Get Radar Age Data
  getRadarAgeData(): Observable<any> {
    return this.http.get<any>(`${apiUrl}/radar`);
  }

  // Get Line Chart Data per Region
 getPieChartData(): Observable<PieChartData> {
  return this.http.get<PieChartData>(`${apiUrl}/pie-chart`);
}

  // Get Recent Activity
  getRecentActivity(userId: number): Observable<RecentActivity[]> {
    return this.http.get<RecentActivity[]>(`${apiUrl}/recent-activity/${userId}`);
  }
}
