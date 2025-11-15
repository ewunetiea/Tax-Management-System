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
  getCardData(user_id: number): Observable<number[]> {
    return this.http.get<number[]>(`${apiUrl}/card-data/` +`${user_id}`);
  }



  // Get Bar Chart Data
  getBarChartData(user_id :number): Observable<number[][]> {
    return this.http.get<number[][]>(`${apiUrl}/bar-chart/`+ `${user_id}` );
  }

  // Get Horizontal Bar Chart Data

polarChartData(user_id: number): Observable<PolarChartData> {
  return this.http.get<PolarChartData>(`${apiUrl}/polar-chart/`+`${user_id}`);
}

  // Get Radar Age Data
  getRadarAgeData(user_id: number): Observable<any> {
    return this.http.get<any>(`${apiUrl}/radar/`+`${user_id}`);
  }

  // Get Line Chart Data per Region
 getPieChartData(user_id: number): Observable<PieChartData> {
  return this.http.get<PieChartData>(`${apiUrl}/pie-chart/`+`${user_id}`);
}

  // Get Recent Activity
  getRecentActivity(userId: number): Observable<RecentActivity[]> {
    return this.http.get<RecentActivity[]>(`${apiUrl}/recent-activity/${userId}`);
  }
}
