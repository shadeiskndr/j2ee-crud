import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface InitiateResetResponse {
  message: string;
}

export interface VerifyCodeResponse {
  message: string;
  resetToken: string;
}

export interface ResetPasswordResponse {
  message: string;
}

@Injectable({
  providedIn: "root",
})
export class PasswordResetService {
  private apiUrl = "/api/auth/password-reset";

  constructor(private http: HttpClient) {}

  initiatePasswordReset(email: string): Observable<InitiateResetResponse> {
    return this.http.post<InitiateResetResponse>(`${this.apiUrl}/initiate`, {
      email,
    });
  }

  verifyCode(verificationCode: string): Observable<VerifyCodeResponse> {
    return this.http.post<VerifyCodeResponse>(`${this.apiUrl}/verify-code`, {
      verificationCode,
    });
  }

  resetPassword(
    resetToken: string,
    newPassword: string
  ): Observable<ResetPasswordResponse> {
    return this.http.post<ResetPasswordResponse>(`${this.apiUrl}/reset`, {
      resetToken,
      newPassword,
    });
  }
}
