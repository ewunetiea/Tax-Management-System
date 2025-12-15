import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { Tax } from '../../models/maker/tax';
import { TaxableSearchEngine } from '../../models/common/taxable-search-engine';
const baseUrl = `${environment.backendUrl}/search`;

@Injectable({
  providedIn: 'root'
})
export class TaxableSearchEngineService {
  constructor(private http: HttpClient) { }

  getTaxesForReviewer(tax: TaxableSearchEngine): Observable<Tax[]> {
    return this.http.post<Tax[]>(`${baseUrl}/reviewer`, tax);
  }

  getTaxesforApprover(tax: TaxableSearchEngine): Observable<Tax[]> {
    return this.http.post<Tax[]>(`${baseUrl}/approver`, tax);
  }

  getTaxesforAdmin(tax: TaxableSearchEngine): Observable<Tax[]> {
    console.log("Adminnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn", tax);
    return this.http.post<Tax[]>(`${baseUrl}/admin`, tax);
  }


}
