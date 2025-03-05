import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TransactionDto } from '../model/transaction-dto';
import { API_URL } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(
    private http: HttpClient,
  ) { }

  createTransaction(appointmentId: number): Observable<TransactionDto> {
    return this.http.post<TransactionDto>(`${API_URL}/transaction/createTransaction/${appointmentId}`, {});
  }

  closeTransaction(transactionId: number): Observable<any> {
    return this.http.post<any>(`${API_URL}/transaction/closeTransaction/${transactionId}`, {});
  }
}
