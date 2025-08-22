import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HospedeService {
  private apiUrl = 'http://localhost:8080/hospede';

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) {}

  getHospedes(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  addHospede(hospede: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, hospede, this.httpOptions);
  }

}