import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { Tax } from '../../models/maker/tax';
import { TaxableSearchEngine } from '../../models/common/taxable-search-engine';
const baseUrl = `${environment.backendUrl}/maker`;

@Injectable({
  providedIn: 'root'
})
export class TaxableSearchEngineService {
  constructor(private http: HttpClient) { }

  getTaxesFormaker(tax: TaxableSearchEngine): Observable<Tax[]> {
    return this.http.post<Tax[]>(`${baseUrl}/maker/search`, tax);
  }

  getTaxesForReviewer(tax: TaxableSearchEngine): Observable<Tax[]> {
    return this.http.post<Tax[]>(`${baseUrl}/reviewer/search`, tax);
  }

  getTaxesforApprover(tax: TaxableSearchEngine): Observable<Tax[]> {
    return this.http.post<Tax[]>(`${baseUrl}/approver/search`, tax);
  }


}
