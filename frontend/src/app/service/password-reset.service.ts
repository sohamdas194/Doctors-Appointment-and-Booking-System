import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from '../app.constants';
import { Observable } from 'rxjs';
import { ForgotPasswordBody } from '../model/forgot-password-body';
import { ResetPasswordReq } from '../model/reset-password-req';
import { PasswordResetTokenValidityRes } from '../model/password-reset-token-check-validity';

@Injectable({
  providedIn: 'root'
})
export class PasswordResetService {

  constructor(
    private http: HttpClient
  ) { }

  executeForgotPasswordService(forgotPasswordReq: ForgotPasswordBody): Observable<ForgotPasswordBody> {
    return this.http.post<ForgotPasswordBody>(`${API_URL}/forgotPassword`, forgotPasswordReq);
  }

  executeResetPasswordService(resetPasswordReq: ResetPasswordReq): Observable<any>  {
    return this.http.post<ResetPasswordReq>(`${API_URL}/resetPassword`, resetPasswordReq);
  }

  validatePasswordResetToken(passwordResetToken: string): Observable<PasswordResetTokenValidityRes> {
    return this.http.get<PasswordResetTokenValidityRes>(`${API_URL}/validatePasswordResetToken/${passwordResetToken}`);
  }


}
