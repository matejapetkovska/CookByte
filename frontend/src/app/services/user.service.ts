import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../interfaces/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  url = 'http://localhost:8080/auth/user'

  constructor(private http: HttpClient) { }

  public isLoggedIn(): boolean {
    const user = window.sessionStorage.getItem('auth-user');
    return !!user;
  }

  getUserFromToken(token: string | null): Observable<User> {
    return this.http.get<User>(`${this.url}/token?token=${token}`)
  }

  updateUser(formData: FormData, userId: number | undefined, token: string | null): Observable<User | null> {
    return this.http.put<User>(`${this.url}/${userId}?token=${token}`, formData);
  }
}
