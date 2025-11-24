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


    getRegions(): Observable<Region[]> {
        return this.httpClient.get<Region[]>(baseUrl + '/region');
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
        return this.httpClient.post(baseUrl + '/region/admin', region);
    }

    deleteRegion(region: Region[]): Observable<any> {
        return this.httpClient.post(baseUrl + '/region/delete/admin', region);
    }

    activateRegion(region: Region[]): Observable<any> {
        return this.httpClient.post(baseUrl + '/region/activate/admin', region);
    }

    checkRegionNameExist(region: Region): Observable<any> {
        return this.httpClient.post(baseUrl + '/region/name/admin', region);
    }

    checkRegionCodeExist(region: Region): Observable<any> {
        return this.httpClient.post(baseUrl + '/region/code/admin', region);
    }

    drawRegionLineChart(): Observable<any> {
        return this.httpClient.get(`${baseUrl}/region/drawRegionLineChart`);
    }
}
