import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.prod';
import { Role } from '../../../models/admin/role';
import { Functionalities } from '../../../models/admin/functionalities';
const baseUrl = environment.backendUrl + '/permission';

@Injectable({
    providedIn: 'root'
})
export class RoleFunctionalityService {
    constructor(private httpClient: HttpClient) {}

    getFunctionalitiesByRoleCategory(role: Role): Observable<any> {
        return this.httpClient.post<any>(baseUrl + '/functionalities', role);
    }

    getFunctionalitiesByRoleId(roleId: number): Observable<any> {
        return this.httpClient.get<any>(`${baseUrl}/functionalities/${roleId}`);
    }

    assignFunctionalities(role: Role): Observable<any> {
        return this.httpClient.post<any>(baseUrl + '/assignFunctionalities', role);
    }

    updateFunctionalitiesRoleMapping(role: Role): Observable<any> {
        return this.httpClient.post<any>(baseUrl + '/updateFunctionalitiesByRole', role);
    }

    updateFunctionalityStatus(functionalities_status: Record<number, boolean>): Observable<any> {
        return this.httpClient.post<any>(baseUrl + '/updateFunctionalityStatus', functionalities_status);
    }

    activatePermissions(functionalities: Functionalities[]): Observable<any> {
        return this.httpClient.post<any>(baseUrl + '/activate', functionalities);
    }
    savePermissions(functionality: Functionalities): Observable<any> {
        return this.httpClient.post<any>(baseUrl + '/save', functionality);
    }

    deactivatePermissions(functionalities: Functionalities[]): Observable<any> {
        return this.httpClient.post<any>(baseUrl + '/deactivate', functionalities);
    }

    deletePermissions(functionalities: Functionalities[]): Observable<any> {
        return this.httpClient.post<any>(baseUrl + '/delete', functionalities);
    }

    getAllRoleFunctionalities(): Observable<any> {
        return this.httpClient.get<any>(baseUrl + '/getAllRoleFunctionalities');
    }
}
