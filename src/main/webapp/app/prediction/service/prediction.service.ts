import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class PredictionService {
  private apiUrl = 'http://127.0.0.1:5000/';

  constructor(private http: HttpClient) {}

  getPrediction(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrl+'predict', data);
  }

  getAllData(): Observable<any>{
    return this.http.get(this.apiUrl+'getall');
  }
}
