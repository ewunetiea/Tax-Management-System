import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { Tax } from '../../models/maker/tax';
import { TaxableSearchEngine } from '../../models/maker/taxable-search-engine';
const baseUrl = `${environment.backendUrl}/maker`;

@Injectable({
  providedIn: 'root'
})
export class TaxableSearchEngineService {
  constructor(private http: HttpClient) {}

  getTaxes(paginatorPayLoad: TaxableSearchEngine): Observable<Tax[]> {
    console.log("Ffffffffffffffffffffffffffffffffffffffffffffffffffff :", paginatorPayLoad);
    return this.http.post<Tax[]>(`${baseUrl}/search`, paginatorPayLoad);
  }


}
