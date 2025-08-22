import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HospedagemService {
  private hospedagemApiUrl = 'http://localhost:8080/hospedagem';

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) {}
  
  getHospedesAtivos(): Observable<any> {
    return this.http.get<any>(`${this.hospedagemApiUrl}/ativas`);
  }

  getHospedesInativos(): Observable<any> {
    return this.http.get<any>(`${this.hospedagemApiUrl}/inativas`);
  }
  

  createHospedagem(hospedagem: any): Observable<any> {
    return this.http.post<any>(this.hospedagemApiUrl, hospedagem, this.httpOptions);
  }
}