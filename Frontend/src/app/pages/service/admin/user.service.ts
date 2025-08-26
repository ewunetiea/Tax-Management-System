import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.prod';
import { User } from '../../../models/admin/user';
import { Role } from '../../../models/admin/role';
import { OnlineFailedUsers } from '../../../models/admin/online-failed-users';
import { PaginatorPayLoad } from '../../../models/admin/paginator-payload';
const API_URL = environment.backendUrl;

@Injectable({
    providedIn: 'root'
})
export class UserService {
    constructor(private http: HttpClient) {}

    getUsers(): Observable<User[]> {
        return this.http.get<User[]>(API_URL + '/user');
    }

    replaceHRData(): Observable<User[]> {
        return this.http.get<any>(API_URL + '/user/replaceHRData');
    }

    getUsersStatus(): Observable<User[]> {
        return this.http.get<User[]>(API_URL + '/user_status');
    }

    getJobPositionsByRole(role: Role): Observable<any> {
        return this.http.post<any>(API_URL + '/job_position/byRole', role);
    }

    addUser(user: User, image?: File): Observable<any> {
        const formData = new FormData();
        if (image) {
            formData.append('image', image);
            formData.append(
                'user_data',
                new Blob([JSON.stringify(user)], {
                    type: 'application/json'
                })
            );
            return this.http.put<User>(API_URL + '/user', formData);
        } else {
            return this.http.post<User>(API_URL + '/user', user);
        }
    }

    getUserById(id: any): Observable<User> {
        return this.http.get<User>(`${API_URL}/user/${id}`);
    }

    getUserIdByEmail(email: any): Observable<any> {
        return this.http.get<any>(`${API_URL}/getUserIdByEmail/${email}`);
    }

    getOnlineFailedUsers(paginatorPayload: PaginatorPayLoad): Observable<OnlineFailedUsers[]> {
        return this.http.post<any>(API_URL + '/loginStatus', paginatorPayload);
    }

    updateLoginStatus(loginStatus: OnlineFailedUsers[]): Observable<any> {
        return this.http.post(API_URL + '/login_status', loginStatus);
    }

    unlockUserAccount(user: User): Observable<any> {
        return this.http.post<User>(API_URL + '/user/account', user);
    }

    manageRoles(user: User): Observable<any> {
        return this.http.post<User>(API_URL + '/user/roles', user);
    }

    generateUsers(user: User): Observable<any> {
        return this.http.post<User>(API_URL + '/user/generate', user);
    }

    transferBranch(user: User): Observable<any> {
        return this.http.post<User>(API_URL + '/user/branch', user);
    }

    makeUserSpecial(users: User[]): Observable<any> {
        return this.http.post<User>(API_URL + '/user/special', users);
    }

    manageMultipleUserRole(users: User[]): Observable<any> {
        return this.http.post<User>(API_URL + '/user/multiple_user_role', users);
    }

    drawBarChartUsersPerRegion(user: User): Observable<any> {
        return this.http.post(API_URL + '/admin/dashboard', user);
    }
}
