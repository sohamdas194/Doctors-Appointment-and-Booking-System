import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';        
import { DoctorAvailabilityDto } from '../model/DoctorAvailabilityDto';
import { API_URL } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class DoctorAvailabilityService {
  constructor(private http: HttpClient) {}

  saveAllDoctorDates(userId: number, doctorAvailable: DoctorAvailabilityDto[]): Observable<DoctorAvailabilityDto[]> {
    return this.http.post<DoctorAvailabilityDto[]>(`${API_URL}/doctor/doctorAvailable/${userId}`, doctorAvailable);
  }

  getAllDates(userId: number): Observable<DoctorAvailabilityDto[]> {
    return this.http.get<DoctorAvailabilityDto[]>(`${API_URL}/doctor/getAllDates/${userId}`);
  }

  updateDate(userId: number, date: Date, updateAvailability: DoctorAvailabilityDto): Observable<DoctorAvailabilityDto> {
    return this.http.put<DoctorAvailabilityDto>(`${API_URL}/doctor/updateDate/${userId}/${date}`, updateAvailability);
  }

  getAllDoctorDates(userId: number): Observable<DoctorAvailabilityDto[]> {
    return this.http.get<DoctorAvailabilityDto[]>(`${API_URL}/doctor/getAllDoctorDates/${userId}`);
  }
}

