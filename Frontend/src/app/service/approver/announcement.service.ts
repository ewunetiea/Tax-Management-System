import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.prod';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Announcement } from '../../models/approver/announcement';
import { AnnouncementPayload } from 'app/models/approver/announcementPayload';
const baseUrl = environment.backendUrl + '/approver/announcement';

const headers = new HttpHeaders({
  'Content-Type': 'application/json'
});

@Injectable({
  providedIn: 'root'
})

export class AnnouncementService {
  constructor(private http: HttpClient) { }

  createAnnouncemet(announcement: any): Observable<any> {
    return this.http.post(baseUrl + '/create', announcement);
  }

  fetchAnnouncemets(announcementPayload: AnnouncementPayload) {
    return this.http.post(baseUrl + '/fetch', announcementPayload);
  }

  fetchAnnouncemetForDashBoard(role_type: String) {
    return this.http.get(baseUrl + '/fetch/dashboard/' + `${role_type}`);
  }

  deleteSelectedAnnouncemets(anoncment: Announcement[]) {
    return this.http.post(baseUrl + '/delete', anoncment);
  }

  fetchAnnouncemetById(id: any) {
    return this.http.delete(baseUrl + '/' + `${id}`);
  }
}

