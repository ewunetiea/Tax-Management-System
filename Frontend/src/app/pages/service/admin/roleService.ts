import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.prod';
import { Rights } from '../../../models/admin/rights';
import { Role } from '../../../models/admin/role';
const rootURL = environment.backendUrl;

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  constructor(private http: HttpClient) {}

  createRole(data: any): Observable<any> {
    return this.http.post(`${rootURL}/role`, data);
  }

  manageJobPositions(data: any): Observable<any> {
    return this.http.post(`${rootURL}/jobPosition/manageJobPositions`, data);
  }

  createRight(data: Object): Observable<any> {
    return this.http.post(`${rootURL}/saveRight`, data);
  }

  getAllRights(): Observable<any> {
    return this.http.get<any>(`${rootURL}/getAllRights`);
  }

  getMappedJobPositions(): Observable<any> {
    return this.http.get<any>(`${rootURL}/jobPositions`);
  }

  getRoles(): Observable<any> {
    return this.http.get<any>(`${rootURL}/role`);
  }
  
  getCommonRoles(): Observable<any> {
    return this.http.get<any>(`${rootURL}/roles`);
  }

  getRolesCategory(category:any): Observable<any> {
    return this.http.get<Role>(`${rootURL}/roles/${category}`);
  }

  getRoleById(id: any): Observable<Role> {
    return this.http.get<Role>(`${rootURL}/role/${id}`);
  }

  getRightById(id: any): Observable<Rights> {
    return this.http.get<Rights>(`${rootURL}/getRightById/${id}`);
  }

  deleteRole(role: Role[]): Observable<any> {
    return this.http.post(`${rootURL}/role/delete`, role);
  }

  activate_role(role: Role[]): Observable<any> {
    return this.http.post(`${rootURL}/role/activate`, role);
  }

  deleteRightById(id: any): Observable<any> {
    return this.http.delete(`${rootURL}/deleteRight/${id}`);
  }

  updateRight(id: any, data: any): Observable<any> {
    return this.http.put(`${rootURL}/updateRight/${id}`, data);
  }

  deactivateRole(selected_role_id: any): Observable<Boolean> {
    return this.http.get<Boolean>(
      rootURL + '/role/deactivate/' + selected_role_id
    );
  }

  activateUser(user_id: any): Observable<Boolean> {
    return this.http.get<Boolean>(rootURL + '/activate_user/' + user_id);
  }

  activateRole(selected_role_id: any): Observable<Boolean> {
    return this.http.get<Boolean>(
      rootURL + '/role/activate/' + selected_role_id
    );
  }
}
