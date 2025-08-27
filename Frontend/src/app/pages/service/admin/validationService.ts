

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.prod';
const rootURL = environment.backendUrl;
@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  constructor(private http: HttpClient) { }

  checkUserEmail(userEmail: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/checkUserEmail/${userEmail}`);
  }

  checkUsername(username: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/checkUsername/${username}`);
  }

  checkUserPhoneNumber(phone_number: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/checkUserPhoneNumber/${phone_number}`);
  }

  checkUserEmployeeId(awash_id: any): Observable<any> {
    return this.http.post<any>(`${rootURL}/checkUserEmployeeId`, awash_id);
  }

  checkEmployeeIdSystem(awash_id: any): Observable<any> {
    return this.http.post<any>(`${rootURL}/checkUserEmployeeIdSystem`, awash_id);
  }

  getJobPosition(): Observable<any> {
    return this.http.get<any>(`${rootURL}/job_positions_admin`)
  }

  getTotalJobPositions(): Observable<any> {
    return this.http.get<any>(`${rootURL}/total_job_positions`)
  }

  getJobPositions(): Observable<any> {
    return this.http.get<any>(`${rootURL}/selected_job_position`)
  }

  checkRoleCode(code: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/role/code/${code}`);
  }
  checRoleName(code: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/role/name/${code}`);
  }

  checkJobPositionRole(id: number): Observable<any> {
    return this.http.get<any>(`${rootURL}/jobPosition/byRole/${id}`);
  }
  checkBranchName(name: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/branch/name/${name}`);
  }
  checkBranchCode(code: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/branch/code/${code}`);
  }


}
