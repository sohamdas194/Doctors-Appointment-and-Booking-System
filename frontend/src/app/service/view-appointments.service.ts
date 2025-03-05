import { Injectable } from '@angular/core';
import { API_URL } from '../app.constants';
import { HttpClient } from '@angular/common/http';
import { ViewAppointmentsDto } from '../model/view-appointments-dto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ViewAppointmentsService {
 

  constructor(private httpClient: HttpClient) { }
  getAppointmentsList(userId:number): Observable<any>{ 
    return this.httpClient.get(`${API_URL}/appointment/getFamilyMembersAppointmentDetails/${userId}`);
  }
  
  cancelAppointment(appointmentId:number): any{
    return this.httpClient.patch(`${API_URL}/appointment/cancelAppointment/${appointmentId}`,'');
  
  }

  getDateFilter(userId:number,fromDate:Date,toDate:Date): Observable<any>{
    return this.httpClient.get(`${API_URL}/appointment/getBetweenDates/${userId}/${fromDate}/${toDate}`);
  }
}
