import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Branch } from '../../models/admin/branch';
import { environment } from '../../../environments/environment.prod';
import { TaxCategory } from '../../models/maker/tax-category';
const baseUrl = environment.backendUrl;

@Injectable({
    providedIn: 'root'
})
export class TaxCategoriesService {
    constructor(private http: HttpClient) {}

    getTaxCategories(): Observable<TaxCategory[]> {
        return this.http.get<TaxCategory[]>(`${baseUrl}/tax-categories`);
    }

    getActiveTaxCategoriesList(): Observable<TaxCategory[]> {
        return this.http.get<TaxCategory[]>(`${baseUrl}/tax-categories/active`);
    }

    createTaxCategory(taxCategories: TaxCategory): Observable<object> {
        return this.http.post(`${baseUrl}/tax-categories`, taxCategories);
    }

    getTaxCategoryById(id: number): Observable<TaxCategory> {
        return this.http.get<TaxCategory>(`${baseUrl}/tax-categories/${id}`);
    }

    deleteTaxCategories(taxCategories: TaxCategory[]): Observable<object> {
        return this.http.post(`${baseUrl}/tax-categories/delete`, taxCategories);
    }

    activateTaxCategories(taxCategories: TaxCategory[]): Observable<object> {
        return this.http.post(`${baseUrl}/tax-categories/activate`, taxCategories);
    }
}
