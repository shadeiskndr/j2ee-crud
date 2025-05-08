import { Injectable, signal } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { User } from "../models/user.model";
import { Observable, tap } from "rxjs";

@Injectable({ providedIn: "root" })
export class AuthService {
  private apiUrl = "/api/auth";
  private _loggedIn = signal(!!this.getToken());

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<{ token: string }> {
    return this.http
      .post<{ token: string }>(`${this.apiUrl}/login`, { username, password })
      .pipe(
        tap((res) => {
          localStorage.setItem("jwt", res.token);
          this._loggedIn.set(true);
        })
      );
  }

  register(user: User): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  logout() {
    localStorage.removeItem("jwt");
    this._loggedIn.set(false);
  }

  getToken(): string | null {
    return localStorage.getItem("jwt");
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  loggedIn = this._loggedIn.asReadonly();
}
