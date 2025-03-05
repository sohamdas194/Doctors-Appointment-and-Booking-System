import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient) {}

  registerDoctor(doctorDto: any): Observable<any> {
    return this.http.post<any>(`${API_URL}/doctor/register`, doctorDto);
  }
}
