import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { FamilyDto } from '../model/family-dto';
import { API_URL } from '../app.constants';


@Injectable({
  providedIn: 'root'
})
export class FamilyDetailsService {
  constructor(private httpClient: HttpClient) { }

  addFamilyDetails(userId:number,familyDto: FamilyDto): Observable<FamilyDto>{
    return this.httpClient.post<FamilyDto>(`${API_URL}/FamilyMember/addFamilyMember/${userId}`, familyDto);
  }

  updateFamilyDetails(memberId:number, familyDto: FamilyDto): Observable<Object>{
    return this.httpClient.put(`${API_URL}/FamilyMember/updateMember/${memberId}`, familyDto);
  }

  getFamilyDetails(userId:number): any{ 
    return this.httpClient.get(`${API_URL}/FamilyMember/getFamilyMembers/${userId}`);
  }

  getFamilyMember(memberId:number): Observable<FamilyDto>{
    return this.httpClient.get<FamilyDto>(`${API_URL}/FamilyMember/edit/${memberId}`);
  }

}
