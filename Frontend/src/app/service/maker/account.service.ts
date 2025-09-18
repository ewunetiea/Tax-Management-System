



import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';


const baseUrl = environment.backendUrl + '/account';


const headers = new HttpHeaders({
    'Content-Type': 'application/json'
});

@Injectable({
  providedIn: 'root'
})
export class AccountService {
    getProductsData() {

    }

    getProductsWithOrdersData() {

    }


    constructor(private http: HttpClient) {}



    // If you wanted to be explicit (though often not necessary for JSON bodies):

    createAccount(product: any): Observable<any> {
        return this.http.post(baseUrl + '/create', product, { headers: headers });
    }

    fetchAccount() {
        return this.http.get(baseUrl + '/fetch');
    }

    deleteAccount(id: any) {
        return this.http.delete(baseUrl + '/delete/' + `${id}`);
    }
}
