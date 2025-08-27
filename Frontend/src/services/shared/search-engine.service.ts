import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Audit_ISM } from 'app/models/auditor/audit_ISM';
import { environment } from 'environments/environment.prod';
import { Observable } from 'rxjs';

const rootURL = environment.backendUrl + '/searchEngine';
@Injectable({
  providedIn: 'root',
})
export class SearchEngineService {
  constructor(private http: HttpClient) {}

  generateProgressFindings(auditISM: Audit_ISM): Observable<any> {
    return this.http.post(`${rootURL}/progress`, auditISM);
  }
  getScheduleRectificationFindings(auditISM: Audit_ISM): Observable<any> {
    return this.http.post(`${rootURL}/scheduled`, auditISM);
  }

  delegateUsers(auditISMs: Audit_ISM[]): Observable<any> {
    return this.http.post(`${rootURL}/delegate`, auditISMs);
  }

  manageAuditees(auditISM: Audit_ISM): Observable<any> {
    return this.http.post(`${rootURL}/auditee`, auditISM);
  }
}
