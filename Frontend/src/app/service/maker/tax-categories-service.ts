import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { TaxCategory } from '../../models/maker/tax-category';
const baseUrl = environment.backendUrl;

@Injectable({
    providedIn: 'root'
})
export class TaxCategoriesService {
    constructor(private http: HttpClient) {}

    createTaxCategory(taxCategories: TaxCategory): Observable<object> {
        return this.http.post(`${baseUrl}/tax-category/create-edit`, taxCategories);
    }

    getTaxCategories(): Observable<TaxCategory[]> {
        return this.http.get<TaxCategory[]>(`${baseUrl}/tax-category/fetch`);
    }

    getActiveTaxCategoriesList(): Observable<TaxCategory[]> {
        return this.http.get<TaxCategory[]>(`${baseUrl}/tax-category/active`);
    }


    getTaxCategoryById(id: number): Observable<TaxCategory> {
        return this.http.get<TaxCategory>(`${baseUrl}/tax-category/${id}`);
    }

    deleteTaxCategories(taxCategories: TaxCategory[]): Observable<object> {
        return this.http.post(`${baseUrl}/tax-category/delete`, taxCategories);
    }

    activateTaxCategories(taxCategories: TaxCategory[]): Observable<object> {
        return this.http.post(`${baseUrl}/tax-category/activate`, taxCategories);
    }
}
