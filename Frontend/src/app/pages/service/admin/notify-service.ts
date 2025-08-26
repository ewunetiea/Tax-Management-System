import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NotifyAdmin } from '../../../models/admin/notify-admin';
import { environment } from '../../../../environments/environment.prod';

const rootURL = environment.backendUrl;

@Injectable({
  providedIn: 'root'
})
export class NotifyMeService {
  constructor(private http: HttpClient) { }

  notifyAdmin(data:any):Observable<any>{
    return this.http.post(`${rootURL}/notifyAdmin`, data);
  }
  
  viewedNotificationsByAdmin(data: NotifyAdmin[]): Observable<any> {
    return this.http.post(`${rootURL}/viewedNotificationsByAdmin`, data);
  }

}
