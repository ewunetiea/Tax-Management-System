
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.prod';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tax } from '../../models/maker/tax';
const baseUrl = environment.backendUrl + '/tax';



@Injectable({
  providedIn: 'root'
})


export class TaxService {



  constructor(private http: HttpClient) { }



  createTax(tax: any): Observable<any> {
    return this.http.post(baseUrl + '/create', tax);
  }

  fetchTaxes(maker_name: String) {

    return this.http.get(baseUrl + '/fetch/' + `${maker_name}`);
  }



  deleteSelectedTaxes(anoncment: Tax[]) {
    return this.http.post(baseUrl + '/delete', anoncment);
  }

  fetchTaxById(id: any) {
    return this.http.delete(baseUrl + '/' + `${id}`);
  }
}



