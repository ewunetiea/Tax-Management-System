

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.prod';
import { User } from '../../../models/admin/user';
const rootURL = environment.backendUrl;
@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  constructor(private http: HttpClient) { }

  institutionEmailExistanceCheck(email: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/institutionEmailExistanceCheck/${email}`);
  }
  institutionAccountExistanceCheck(accountNumber: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/institutionAccountExistanceCheck/${accountNumber}`);
  }
  rmEmailExistanceCheck(email: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/rmEmailExistanceCheck/${email}`);
  }
  institutionPhoneNumberxistanceCheck(phone_number: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/institutionPhoneNumberExistanceCheck/${phone_number}`);
  }
  institutionNameExistanceCheck(name: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/institutionNameExistanceCheck/${name}`);
  }
  institutionCodeExistanceCheck(code: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/institutionCodeExistanceCheck/${code}`);
  }

  checkAccount(account_number: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/checkAccount/${account_number}`);
  }
  checkUserEmail(userEmail: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/checkUserEmail/${userEmail}`);
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

  checkStaffTinNo(tinNo: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/checkStaffTinNo/${tinNo}`);
  }
  getJobPositions(): Observable<any> {
    return this.http.get<any>(`${rootURL}/selected_job_position`)
  }

  checkStaffPhoneNumber(phone_number: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/checkStaffPhoneNumber/${phone_number}`);
  }

  checkStaffAccountNumber(account_number: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/checkStaffAccountNumber/${account_number}`);
  }
  checkStaffAccountNumberExistance(account_number: string): Observable<any> {
    return this.http.get<any>(`${rootURL}/checkStaffAccountNumberExistance/${account_number}`);
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


  fetchUserBranchAndPositionFromHrSystem(user: User): Observable<any> {
    return this.http.post<any>(`${rootURL}/fetchUserBranchAndPositionFromHrSystem`, user);
  }
}
