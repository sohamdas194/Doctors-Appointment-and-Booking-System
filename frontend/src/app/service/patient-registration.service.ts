import { Injectable } from '@angular/core';
import { PatientDto } from '../model/patient-dto';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class PatientRegistrationService {

  constructor(private httpClient: HttpClient) { }

  createPatient(patientDto: PatientDto): Observable<Object>{
    return this.httpClient.post(`${API_URL}/patient/save`, patientDto);
  }

  getPatientByEmail(email:any): Observable<any>{
    return this.httpClient.get<any>(`${API_URL}/patient/checkEmail/${email}`);
  }


  getPatient(patientId:number){
    return this.httpClient.get<PatientDto>(`${API_URL}/patient/getPatient/${patientId}`);
   }


   updateprofile(id:number,patient: PatientDto){
    console.warn(patient);
    return this.httpClient.put<PatientDto>(`${API_URL}/patient/update/${id}`, patient);
   }
}
