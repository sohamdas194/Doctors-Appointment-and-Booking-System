import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ViewAppointmentsDto } from 'src/app/model/view-appointments-dto';
import { API_URL } from '../app.constants';
// import { HttpClient } from '@angular/common/http';


@Injectable({
 providedIn: 'root'

})

export class DoctorDashboardService {

//  private apiUrl = 'https://api/appointments';


 constructor(private http: HttpClient) { }

 getAppointmentsByCurrentDate(userId:number) {
  return this.http.get<ViewAppointmentsDto>(`${API_URL}/doctor/dashboard/currentDate/${userId}`);
 }

getAppointmentsByNextDate(userId:number) {
  return this.http.get<ViewAppointmentsDto>(`${API_URL}/doctor/dashboard/nextDate/${userId}`);
 }

getAllAppointments(userId:number) {
  return this.http.get<ViewAppointmentsDto>(`${API_URL}/doctor/dashboard/allAppointments/${userId}`);
 }

getAppointmentBetween(userId:number,fromDate:any,toDate:any) {
  return this.http.get<ViewAppointmentsDto>(`${API_URL}/doctor/dashboard/searchBetweenDates/${userId}/${fromDate}/${toDate}`);
 }

updateDoctorComment(appointmentId:number, viewAppointment:any) {
  console.log(`${API_URL}/doctor/dashboard/updateDoctorComment/${appointmentId}`)
  return this.http.put<any>(`${API_URL}/doctor/dashboard/updateDoctorComment/${appointmentId}`,viewAppointment);
  }
}
