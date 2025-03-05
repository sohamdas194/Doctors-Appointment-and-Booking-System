import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from '../app.constants';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppointmentHistoryService {
  constructor(private http: HttpClient) {}

  getPatientAppointmentHistory(userId: number) {
    console.log('Fetching appointment history for patient:', userId);
    return this.http.get(`${API_URL}/patient/${userId}/appointments/history`);
  }
  getAppointmentsByPatientIdInDateRange(userId:number, fromDate:string, toDate:string): Observable<any>{
return this.http.get(`${API_URL}/patient/appointments/history/${userId}/${fromDate}/${toDate}`);
  }

}
