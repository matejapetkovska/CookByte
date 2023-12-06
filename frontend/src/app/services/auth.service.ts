import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, tap} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoginRequest} from "../interfaces/login-request";
import {AuthenticationResponse} from "../interfaces/authentication-response";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.isAuthenticated())
  private url = 'http://localhost:8080/api/auth'

  constructor(private httpClient: HttpClient) { }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token')
    return !!token
  }

  register(request: FormData): Observable<AuthenticationResponse> {
    return this.httpClient.post<AuthenticationResponse>(`${this.url}/register`, request)
  }

  login(request: LoginRequest): Observable<AuthenticationResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.httpClient.post<AuthenticationResponse>(`${this.url}/authenticate`, request, { headers })
      .pipe(
        tap(response => {
          // Assuming the server returns a token in the response
          const token = response.token;

          // Store the token in local storage
          localStorage.setItem('token', token);

          // Update the authentication state
          this.isAuthenticatedSubject.next(true);
        })
      );
  }


  isAuthenticated$(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }
}
