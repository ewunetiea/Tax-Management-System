import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.prod';
import { PasswordAndToken } from '../../../models/admin/password-and-token';
const rootURL = environment.backendUrl + '/password';

@Injectable({
  providedIn: 'root',
})
export class PasswordService {
  constructor(private http: HttpClient) {}

  changeMyPassword(data: any): Observable<any> {
    return this.http.post(`${rootURL}/changeMyPassword`, data);
  }

  forgotPassword(user_agent: any, data: any): Observable<any> {
    return this.http.post(`${rootURL}/forgotPassword`, user_agent, {
      params: {email: data.email, phone_number: data.phone_number, authenthication_media: data.authenthication_media,
      },
    });
  }

  getUserByPasswordResetToken(token: string) {
    return this.http.get(`${rootURL}/getUserByResetToken`, {
      params: { token: token,},
    });
  }

  changePasswordWithToken(data: PasswordAndToken) {
    return this.http.post(`${rootURL}/user/savePassword`, data);
  }

  verifyOTP(data: string): Observable<any> {
    return this.http.get(`${rootURL}/verifyOTP`, {
      params: {token: data,},
    });
  }
  

}
