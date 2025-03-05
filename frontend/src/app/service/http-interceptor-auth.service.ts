import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorAuthService implements HttpInterceptor{

  constructor(
    private authService: AuthenticationService
  ) { }


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    // let bearerToken = this.authService.getAuthenticatedToken();

    // if(bearerToken) {
    //   req = req.clone({
    //     setHeaders: {
    //       Authorization : bearerToken
    //     }
    //   })
    // }

    return next.handle(req);
  }
}
