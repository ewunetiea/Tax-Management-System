import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { Tax } from '../../models/maker/tax';
import { PaginatorPayLoad } from '../../models/admin/paginator-payload';

const baseUrl = `${environment.backendUrl}/reviewer/manage-tax`;

@Injectable({
  providedIn: 'root'
})
export class ManageTaxService {
  constructor(private http: HttpClient) { }

  getPendingTaxes(paginatorPayLoad: PaginatorPayLoad): Observable<Tax[]> {
    return this.http.post<Tax[]>(`${baseUrl}/pending`, paginatorPayLoad);
  }

  getRejectedTaxes(paginatorPayLoad: PaginatorPayLoad): Observable<Tax[]> {
    return this.http.post<Tax[]>(`${baseUrl}/rejected`, paginatorPayLoad);
  }

  getApprovedTaxes(paginatorPayLoad: PaginatorPayLoad): Observable<Tax[]> {
    return this.http.post<Tax[]>(`${baseUrl}/settled`, paginatorPayLoad);
  }

  getSentTaxes(paginatorPayLoad: PaginatorPayLoad): Observable<Tax[]> {
    return this.http.post<Tax[]>(`${baseUrl}/sent`, paginatorPayLoad);
  }

  reviewTaxes(taxes: Tax[]): Observable<any> {
    return this.http.post(`${baseUrl}/review`, taxes);
  }

  rejectReviewerTax(tax: Tax): Observable<any> {
    return this.http.post(`${baseUrl}/reject`, tax);
  }

  backToWaitingState(tax: Tax): Observable<any> {
    return this.http.post(`${baseUrl}/backtoWaitingState`, tax);
  }


}
