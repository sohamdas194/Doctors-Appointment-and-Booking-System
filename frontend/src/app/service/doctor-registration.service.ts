import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { API_URL } from '../app.constants';
import { DoctorDto } from '../model/doctor-dto';

@Injectable({
  providedIn: 'root'
})
export class DoctorRegistrationService {

  constructor(private http: HttpClient) { }

  registerDoctor(doctorDto: any): Observable<any> {
    return this.http.post<any>(`${API_URL}/doctor/register`, doctorDto);
  }

  getDoctorByEmail(email: string): Observable<any> {
    return this.http.get<any>(`${API_URL}/doctor/${email}`);
  }

  getUserByID(userId: number): Observable<DoctorDto> {
    return this.http.get(`${API_URL}` + "/doctor/id/" + `${userId}`) as Observable<DoctorDto>;
  }

  updateUserByUserID(userId: number, doctorDetails: DoctorDto): Observable<DoctorDto> {
    return this.http.put(`${API_URL}` + "/doctor/update/" + `${userId}`, doctorDetails) as Observable<DoctorDto>;
  }

  getAllSpecializations(): Observable<string[]> {
    return this.http.get<string[]>(`${API_URL}` + "/doctor/specializations");
  }
}
