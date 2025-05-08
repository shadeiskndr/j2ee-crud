import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Postcode } from "../models/postcode.model";
import { Observable } from "rxjs";

@Injectable({ providedIn: "root" })
export class PostcodeService {
  private apiUrl = "/api/postcodes";

  constructor(private http: HttpClient) {}

  getPostcodes(params?: any): Observable<any> {
    return this.http.get<any>(this.apiUrl, { params });
  }
}
