import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { State } from "../models/state.model";
import { Observable } from "rxjs";

@Injectable({ providedIn: "root" })
export class StateService {
  private apiUrl = "/api/states";

  constructor(private http: HttpClient) {}

  getStates(): Observable<State[]> {
    return this.http.get<State[]>(this.apiUrl);
  }
}
