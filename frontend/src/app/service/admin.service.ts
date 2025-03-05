import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthenticationService } from './authentication.service';
import { FilteredAppointment } from '../model/filtered-appointment';
import { API_URL } from '../app.constants';
import { Observable } from 'rxjs';
import { FilteredDoctor } from '../model/filtered-doctor';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  today: string;

  constructor(
    private http: HttpClient,
    private authService: AuthenticationService
  ) { 
    let todayDate = new Date();
    this.today = todayDate.getFullYear() + '-' + (todayDate.getMonth()+1).toString().padStart(2, '0') + '-' + todayDate.getDate().toString().padStart(2, '0');
  }

  searchAppointments(searchVals: any, upcomingFlag: boolean): Observable<FilteredAppointment[]> {
    let name = searchVals.name;
    if(searchVals.name === "") name = "_";
    if(searchVals.fromDate === "") {
      if(upcomingFlag) searchVals.fromDate = this.today;
      else searchVals.fromDate = "_";
    } 
    if(searchVals.toDate === "") {
      if(upcomingFlag) searchVals.toDate = "_";
      else searchVals.toDate = this.today;
    }

    return this.http.get<FilteredAppointment[]>(`${API_URL}/admin/searchAppointments/name/${name}/fromDate/${searchVals.fromDate}/toDate/${searchVals.toDate}/upcoming/${upcomingFlag}`);
  }

  searchDoctors(searchVals: any): Observable<FilteredDoctor[]>  {
    if(searchVals.name === "") searchVals.name = "_";

    return this.http.get<FilteredDoctor[]>(`${API_URL}/admin/searchDoctors/name/${searchVals.name}`);
  }

  updateConsultationFee(doctorId: number, newConsultationFee: number): Observable<any> {
    return this.http.patch<any>(`${API_URL}/admin/updateDoctorConsultationFee/`, {
      doctorId: doctorId,
      consultationFee: newConsultationFee
    });
  }

  updateAppointmentStatus(appointmentId: number, newAppointmentStatus: string): Observable<any> {
    return this.http.patch<any>(`${API_URL}/admin/updateAppointmentStatus/`, {
      appointmentStatus: newAppointmentStatus,
      appointmentId: appointmentId
    });
  }

  notify(appointmentId: number): Observable<any> {
    return this.http.get<any>(`${API_URL}/admin/notifyDoctorAndPatient/appointment/${appointmentId}`);
  }
}
