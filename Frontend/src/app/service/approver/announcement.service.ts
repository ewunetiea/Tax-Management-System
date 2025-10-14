import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.prod';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Announcement } from '../../models/approver/announcement';
const baseUrl = environment.backendUrl + '/announcement';

const headers = new HttpHeaders({
    'Content-Type': 'application/json'
});

@Injectable({
  providedIn: 'root'
})


export class AnnouncementService {
    


    constructor(private http: HttpClient) {}



    createAnnouncemet(announcement: any): Observable<any> {
        return this.http.post(baseUrl + '/create', announcement);
    }

    fetchAnnouncemets(Announcement_type: String) {

        return this.http.get(baseUrl + '/fetch/' + `${Announcement_type}`);
    }

    deleteAnnouncemets(id: any) {
        return this.http.delete(baseUrl + '/delete/' + `${id}`);
    }

      deleteSelectedAnnouncemets(anoncment: Announcement[]) {
        return this.http.post(baseUrl + '/delete', anoncment );
    }

     fetchAnnouncemetById(id: any) {
        return this.http.delete(baseUrl + '/' + `${id}`);
    }
}

