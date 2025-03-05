import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL, AUTHENTICATED_USER_ID, AUTHENTICATED_USER_EMAIL, AUTHENTICATED_USER_ROLE, AUTHENTICATED_USER_TOKEN } from '../app.constants';
import { Observable, map, skip } from 'rxjs';
import { AuthenticatedUser } from '../model/authenticated-user';
import { Router } from '@angular/router';
import { UserCredentials } from '../model/user-credentials';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  executeAuthenticationService(userCreds: UserCredentials): Observable<AuthenticatedUser> {
    return this.http.post<AuthenticatedUser>(`${API_URL}/authenticate`, userCreds)
      .pipe(
        map(
          authenticatedUser => {
            localStorage.setItem(AUTHENTICATED_USER_ID, authenticatedUser.userId.toString());
            localStorage.setItem(AUTHENTICATED_USER_EMAIL, authenticatedUser.userEmail);
            localStorage.setItem(AUTHENTICATED_USER_ROLE, authenticatedUser.userRole.toLowerCase());
            localStorage.setItem(AUTHENTICATED_USER_TOKEN, authenticatedUser.authToken);
            return authenticatedUser;
          }
        )
      );
  }

  isUserLoggedIn(): boolean {
    let user = localStorage.getItem(AUTHENTICATED_USER_ID);
    return !(user === null)
  }

  getAuthenticatedUserId() {
    if(this.isUserLoggedIn())
      return localStorage.getItem(AUTHENTICATED_USER_ID);
    return null;
  }

  getAuthenticateduserEmail() {
    if(this.isUserLoggedIn())
      return localStorage.getItem(AUTHENTICATED_USER_EMAIL);
    return null;
  }

  getAuthenticatedUserRole() {
    if(this.isUserLoggedIn())
      return localStorage.getItem(AUTHENTICATED_USER_ROLE);
    return null;
  }

  getAuthenticatedToken() {
    if(this.isUserLoggedIn())
      return localStorage.getItem(AUTHENTICATED_USER_TOKEN);
    return null;
  }

  logout() {
    localStorage.removeItem(AUTHENTICATED_USER_ID);
    localStorage.removeItem(AUTHENTICATED_USER_EMAIL);
    localStorage.removeItem(AUTHENTICATED_USER_ROLE);
    localStorage.removeItem(AUTHENTICATED_USER_TOKEN);
    // sessionStorage.clear();

    this.router.navigate(['login']);
  }

}
