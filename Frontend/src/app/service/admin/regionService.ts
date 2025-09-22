import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Region } from '../../models/admin/region';
import { environment } from '../../../environments/environment.prod';

const baseUrl = environment.backendUrl;

@Injectable({
    providedIn: 'root'
})
export class RegionService {
    constructor(private httpClient: HttpClient) {}

    // getRegions(paginatorPayLoad : PaginatorPayLoad):Observable<Region[]>{
    //   return this.httpClient.post<Region[]>(baseUrl + '/regions', paginatorPayLoad);
    // }

    getRegions(): Observable<Region[]> {
        return this.httpClient.get<Region[]>(baseUrl + '/regionsss');
    }

    getAllRegions(): Observable<Region[]> {
        return this.httpClient.get<Region[]>(baseUrl + '/regions');
    }

    getActiveRegions(): Observable<Region[]> {
        return this.httpClient.get<Region[]>(baseUrl + '/region/active');
    }

    getRegionById(id: any): Observable<Region> {
        return this.httpClient.get<Region>(baseUrl + '/region/' + `${id}`);
    }

    saveRegion(region: Region): Observable<any> {
        return this.httpClient.post(baseUrl + '/region', region);
    }

    deleteRegion(region: Region[]): Observable<any> {
        return this.httpClient.post(baseUrl + '/region/delete', region);
    }

    activateRegion(region: Region[]): Observable<any> {
        return this.httpClient.post(baseUrl + '/region/activate', region);
    }

    checkRegionNameExist(region: Region): Observable<any> {
        return this.httpClient.post(baseUrl + '/region/name', region);
    }

    checkRegionCodeExist(region: Region): Observable<any> {
        return this.httpClient.post(baseUrl + '/region/code', region);
    }

    drawRegionLineChart(): Observable<any> {
        return this.httpClient.get(`${baseUrl}/region/drawRegionLineChart`);
    }
}
