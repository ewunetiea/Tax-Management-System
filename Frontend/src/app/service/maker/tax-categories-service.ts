import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { TaxCategory } from '../../models/maker/tax-category';
const baseUrl = `${environment.backendUrl}/maker/tax-category`;

@Injectable({
    providedIn: 'root'
})
export class TaxCategoriesService {
    constructor(private http: HttpClient) {}

    createTaxCategory(taxCategories: TaxCategory): Observable<object> {
        return this.http.post(`${baseUrl}/create-edit`, taxCategories);
    }

    getTaxCategories(): Observable<TaxCategory[]> {
        return this.http.get<TaxCategory[]>(`${baseUrl}/fetch`);
    }

    getActiveTaxCategoriesList(): Observable<TaxCategory[]> {
        return this.http.get<TaxCategory[]>(`${baseUrl}/active`);
    }


    getTaxCategoryById(id: number): Observable<TaxCategory> {
        return this.http.get<TaxCategory>(`${baseUrl}/${id}`);
    }

    deleteTaxCategories(taxCategories: TaxCategory[]): Observable<object> {
        return this.http.post(`${baseUrl}/delete`, taxCategories);
    }

    activateTaxCategories(taxCategories: TaxCategory[]): Observable<object> {
        return this.http.post(`${baseUrl}/activate`, taxCategories);
    }
}
