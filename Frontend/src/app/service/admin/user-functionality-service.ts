import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { User } from '../../models/admin/user';
import { Functionalities } from '../../models/admin/functionalities';
const baseUrl = environment.backendUrl + '/user-permission';

@Injectable({
    providedIn: 'root'
})
export class UserFunctionalityService {
    constructor(private httpClient: HttpClient) {}

    activatePermissions(functionalities: Functionalities[]): Observable<any> {
        return this.httpClient.post<any>(baseUrl + '/activate', functionalities);
    }

    deactivatePermissions(user: User): Observable<any> {
        return this.httpClient.post<any>(baseUrl + '/deactivate', user);
    }

    getUsers(user: User): Observable<User[]> {
        return this.httpClient.post<any>(baseUrl + '/users', user);
    }
}
