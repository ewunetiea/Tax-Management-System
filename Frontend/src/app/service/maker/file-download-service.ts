
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.prod';
import { HttpClient } from '@angular/common/http';
const baseUrl = environment.backendUrl + '/download';

@Injectable({
  providedIn: 'root'
})

export class FileDownloadService {

  constructor(private http: HttpClient) { }





fetchFileByFileName(fileName: string) {
    return this.http.get(`${baseUrl}/${fileName}`, { responseType: 'blob' });
}
}



