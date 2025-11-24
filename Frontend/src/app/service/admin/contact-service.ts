import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { Contact } from '../../models/admin/contact';
import { Feedback } from '../../models/admin/feedback';
const baseUrl = environment.backendUrl;

@Injectable({
    providedIn: 'root'
})
export class ContactService {
    constructor(private httpClient: HttpClient) {}
    saveContact(contact: Contact): Observable<any> {
        return this.httpClient.post(baseUrl + '/contact/admin', contact);
    }

    deleteContacts(contacts: Contact[]): Observable<any> {
        return this.httpClient.post(baseUrl + '/contact/delete/admin', contacts);
    }

    getContacts(): Observable<Contact[]> {
        return this.httpClient.get<Contact[]>(baseUrl + '/contacts');
    }
    
    getContactById(id: any): Observable<any> {
        return this.httpClient.get<any>(baseUrl + '/contact/' + `${id}`);
    }

    saveFeedback(feedback: Feedback): Observable<any> {
        return this.httpClient.post(baseUrl + '/feedback', feedback);
    }

    respondFeedbacks(feedbacks: Feedback[]): Observable<any> {
        return this.httpClient.post(baseUrl + '/feedback/respond', feedbacks);
    }

    closeFeedbacks(feedbacks: Feedback[]): Observable<any> {
        return this.httpClient.post(baseUrl + '/feedback/close', feedbacks);
    }

    deleteFeedbacks(feedbacks: Feedback[]): Observable<any> {
        return this.httpClient.post(baseUrl + '/feedback/delete', feedbacks);
    }

    getFeedbacks(): Observable<any> {
        return this.httpClient.get(baseUrl + '/feedback');
    }

    getFeedbacksByUserID(id: any): Observable<any> {
        return this.httpClient.get<any>(baseUrl + '/feedback/' + `${id}`);
    }
}
