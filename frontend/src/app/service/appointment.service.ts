import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from '../app.constants';
import { Observable } from 'rxjs';
import { AuthenticationService } from './authentication.service';
import { AppointmentDto } from '../model/appointment-dto';
import { FilteredDoctor } from '../model/filtered-doctor';
import { FamilyMemberSummary } from '../model/fam-mem-summ';
import { AppointmentSlots } from '../model/appointment-slots';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  constructor(
    private http: HttpClient,
    private authService: AuthenticationService
  ) { }

  searchDoctors(searchVals:any): Observable<FilteredDoctor[]> {
    if(searchVals.docName === "") searchVals.docName = "_";
    if(searchVals.docSpecialization === "") searchVals.docSpecialization = "_";
    if(searchVals.apmntDate === "") searchVals.apmntDate = "_";
    if(searchVals.docCity === "") searchVals.docCity = "_";
    if(searchVals.docPincode === "") searchVals.docPincode = "_";

    return this.http.get<FilteredDoctor[]>(`${API_URL}/doctor/searchDoctors/name/${searchVals.docName}/specialization/${searchVals.docSpecialization}/experience/${searchVals.docExp}/city/${searchVals.docCity}/pinCode/${searchVals.docPincode}/date/${searchVals.apmntDate}`)
  }

  getPatientFamilyMembers(): Observable<FamilyMemberSummary[]> {
    return this.http.get<FamilyMemberSummary[]>(`${API_URL}/appointment/getMembersSummary/user/${this.authService.getAuthenticatedUserId()}`);
  }

  isDoctorAvailableOnDate(docId: number, apmntDate: string) {
    if(apmntDate === "" || apmntDate == null) apmntDate = "";
    return this.http.get<any>(`${API_URL}/appointment/isDoctorAvailable/doctor/${docId}/date/${apmntDate}`);
  }

  getAvailableAppointmentSlots(docId: number, apmntDate: string): Observable<AppointmentSlots> {
    return this.http.get<AppointmentSlots>(`${API_URL}/appointment/getAvailableAppointmentSlots/doctor/${docId}/date/${apmntDate}`)
  }

  bookAppointment(memberId: number, doctorAvailabilityId: number, appointmentSlot: number, patientCmnt: string) {
    let apmntDto = new AppointmentDto(
      Number(this.authService.getAuthenticatedUserId()),
      memberId,
      doctorAvailabilityId,
      appointmentSlot,
      patientCmnt
    );
    
    return this.http.post<any>(`${API_URL}/appointment/bookAppointment`, apmntDto);
  }
}
