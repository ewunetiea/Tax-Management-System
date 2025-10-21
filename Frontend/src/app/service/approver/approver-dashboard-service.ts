import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
const apiUrl = environment.backendUrl + '/approver/dashboard';

@Injectable({
    providedIn: 'root'
})
export class ApproverDashboardService {

    constructor(private http: HttpClient) { }

    getTaxStatusForApprover(branch_id?: number): Observable<number[]> {
        return this.http.get<number[]>(`${apiUrl}/tax-status-card/${branch_id}`);
    }

     getStackedBarTaxesStatusData(branch_id?: number): Observable<number[]> {
         return this.http.get<number[]>(`${apiUrl}/tax-status-stacked-bar/${branch_id}`);
      }

}
