import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.prod';
const rootURL = environment.backendUrl + "/backup";

@Injectable({
  providedIn: 'root'
})
export class BackupService {
  constructor(private http: HttpClient) {}

  createBackup(data: any): Observable<any> {
    // const header = { Accept: "application/octet-stream" };
    // return this.http.post(`${rootURL}/getBackup`, data, {headers:header, responseType: "blob", observe: "response"})
    // .pipe(
    //   map((resp:HttpResponse<Blob>) => {
    //     const blob = resp.body
    //     console.log(data)
    //     if(blob){
    //       const url = window.URL.createObjectURL(blob);
    //       const a = document.createElement('a');
    //       // console.log(backup)
    //       document.body.appendChild(a);
    //       a.href = url;
    //       a.download = resp.headers.get('content-disposition')?.split(";")[1].slice(11).replace('"', "")!
    //       a.click();
    //       window.URL.revokeObjectURL(url);
    //     }
    //     })
    // );
    return this.http.post(`${rootURL}/getBackup`, data);

  }

  getBackupByUserId(id: any): Observable<any> {
    return this.http.get(`${rootURL}/getBackupByUserId/${id}`);
  }
}
