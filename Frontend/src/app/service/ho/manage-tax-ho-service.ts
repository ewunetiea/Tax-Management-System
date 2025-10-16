import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { Tax } from '../../models/maker/tax';

const baseUrl = `${environment.backendUrl}/ho/manage-tax`;

@Injectable({
  providedIn: 'root'
})
export class ManageTaxHoService {
  constructor(private http: HttpClient) {}

  approveTaxes(taxes: Tax[]): Observable<any> {
    return this.http.post(`${baseUrl}/review`, taxes);
  }

  rejectApproverTax(tax: Tax): Observable<any> {
    return this.http.post(`${baseUrl}/reject`, tax);
  }

}
