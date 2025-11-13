
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.prod';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tax } from '../../models/maker/tax';
import { TaxableSearchEngine } from '../../models/common/taxable-search-engine';
import { MakerSearchPayload } from '../../models/payload/maker-search-payload';
const baseUrl = environment.backendUrl + '/tax';



@Injectable({
  providedIn: 'root'
})


export class TaxService {



  constructor(private http: HttpClient) { }



  createTax(tax: any): Observable<any> {
    return this.http.post(baseUrl + '/create', tax);
  }
  fetchTaxesBasedOnStatus(payload: MakerSearchPayload): Observable<Tax[]> {
    console.log("_______G_____________________________________")
    return this.http.post<Tax[]>(baseUrl + '/fetchTaxBasedonStatus', payload);
  }


  fetchTaxProgress(payload: MakerSearchPayload): Observable<Tax[]> {
    console.log("Tax Progress____________________________")
    return this.http.post<Tax[]>(baseUrl + '/fetchTaxProgress', payload);
  }


  deleteSelectedTaxes(anoncment: Tax[]) {
    return this.http.post(baseUrl + '/delete', anoncment);
  }

  fetchTaxById(id: any) {
    return this.http.delete(baseUrl + '/' + `${id}`);
  }

  submitToBranchManager(anoncment: Tax[]) { // update status to 0
    return this.http.post(baseUrl + '/submit', anoncment);
  }

  backtoDraftState(anoncment: Tax[]) { // update status to 0
    return this.http.post(baseUrl + '/back', anoncment);
  }

}



