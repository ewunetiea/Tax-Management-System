import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.prod';
import { HttpClient } from '@angular/common/http';
const baseUrl = environment.backendUrl + '/maker/download';

@Injectable({
  providedIn: 'root'
})

export class FileDownloadService {

  constructor(
    private http: HttpClient
  ) { }

fetchTaxFileByFileName(fileName: string) {
    return this.http.get(`${baseUrl}/${fileName}`, { responseType: 'blob' });
}

fetcAnnouncementhFileByFileName(fileName: string) {
    return this.http.get(`${baseUrl}/announcement/${fileName}`, { responseType: 'blob' });
}


}



