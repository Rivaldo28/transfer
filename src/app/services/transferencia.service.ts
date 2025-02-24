import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Page } from '../model/page.model';
import { Transfer } from '../model/transfer.model';

@Injectable({
  providedIn: 'root'
})
export class TransferenciaService {
  private apiUrl = 'http://localhost:8080/transfer';

  constructor(private http: HttpClient) {}

  public obterExtrato(page: number = 0, size: number = 10): Observable<Page<Transfer>> {
    return this.http.get<Page<Transfer>>(`${this.apiUrl}?page=${page}&size=${size}`).pipe(
      catchError(this.handleError)
    );
  }

    public agendarTransferencia(transfer: Transfer): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}`, transfer);
    }
  
  private handleError(error: any): Observable<never> {
    console.error('Erro na requisição:', error);
    return throwError(() => new Error('Erro ao realizar a operação. Tente novamente.'));
  }
}
