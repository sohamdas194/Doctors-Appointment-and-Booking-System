import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EmailService {
  constructor(private http: HttpClient) {}

  emailIdExists(emailId: string) {
    return this.http.get<boolean>(`api/email/exists/${emailId}`).toPromise().then((exists) => {
      if (exists) {
        return { userExists: true };
      } else {
        return null;
      }
    });
  }
}

