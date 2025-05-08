import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Member } from "../models/member.model";
import { Observable } from "rxjs";

@Injectable({ providedIn: "root" })
export class MemberService {
  private apiUrl = "/api/members";

  constructor(private http: HttpClient) {}

  getMembers(params?: any): Observable<any> {
    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach((key) => {
        if (params[key] !== undefined && params[key] !== null)
          httpParams = httpParams.set(key, params[key]);
      });
    }
    return this.http.get<any>(this.apiUrl, { params: httpParams });
  }

  getMember(id: number): Observable<Member> {
    return this.http.get<Member>(`${this.apiUrl}/${id}`);
  }

  createMember(member: Member): Observable<Member> {
    return this.http.post<Member>(this.apiUrl, member);
  }

  updateMember(id: number, member: Member): Observable<Member> {
    return this.http.put<Member>(`${this.apiUrl}/${id}`, member);
  }

  deleteMember(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
