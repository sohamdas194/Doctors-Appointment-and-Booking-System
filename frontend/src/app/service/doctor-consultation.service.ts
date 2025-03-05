import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DoctorConsultationDto } from '../model/doctorConsultationDto';
import { API_URL } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class DoctorConsultationService {

  constructor(private http: HttpClient) { }

  getAllDoctorConsultations(userId:number):Observable<DoctorConsultationDto[]>{
    return this.http.get<DoctorConsultationDto[]>(`${API_URL}/doctor/completed/consultations/${userId}`);
  }

  getAllDoctorConsultationsInRange(userId:number,fromDate:any,toDate:any,patientName:string):Observable<DoctorConsultationDto[]>{
    let params=new HttpParams();
    if(fromDate){
      params=params.set('fromDate',fromDate);
    }
    if(toDate){
      params=params.set('toDate',toDate);
    }
    if(patientName){
      params=params.set('patientName',patientName);
    }
    return this.http.get<DoctorConsultationDto[]>(`${API_URL}/doctor/consultations/between/${userId}`,{params});
  }

  // /consultations/between/{userId}
}
