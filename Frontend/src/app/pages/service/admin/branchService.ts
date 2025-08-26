import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.prod';
import { Branch } from '../../../models/admin/branch';
import { PaginatorPayLoad } from '../../../models/admin/paginator-payload';
const baseUrl = environment.backendUrl;

@Injectable({
  providedIn: 'root'
})
export class BranchService {

    constructor(private http: HttpClient) { }

    // getBranches(paginatorPayLoad : PaginatorPayLoad):Observable<Branch[]>{
    //   return this.http.post<Branch[]>(baseUrl + '/branches', paginatorPayLoad);
    // }

    getBranches(): Observable<Branch[]> {
    return this.http.get<Branch[]>(`${baseUrl}/branch`);
  }
  
    getActiveBranchesList(): Observable<Branch[]> {
      return this.http.get<Branch[]>(`${baseUrl}/branch/active`);
    }

    createBranch(branches: Branch): Observable<object> {
      return this.http.post(`${baseUrl}/branch`, branches)
    }

    getBranchById(id: number): Observable<Branch> {
      return this.http.get<Branch>(`${baseUrl}/branch/${id}`);
    }

    deleteBranches(branches: Branch[]): Observable<object> {
      return this.http.post(`${baseUrl}/branch/delete`, branches)
    }
    
    activateBranches(branches: Branch[]): Observable<object> {
      return this.http.post(`${baseUrl}/branch/activate`, branches)
    }
}
