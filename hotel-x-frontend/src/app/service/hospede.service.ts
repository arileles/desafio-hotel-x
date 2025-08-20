import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HospedeService {
  // URL do backend (ajuste conforme seu endpoint)
  private apiUrl = 'http://localhost:8080/hospede';

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) {}

  // GET: busca todos os hóspedes
  getHospedes(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  // POST: adiciona um novo hóspede
  addHospede(hospede: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, hospede, this.httpOptions);
  }
}
