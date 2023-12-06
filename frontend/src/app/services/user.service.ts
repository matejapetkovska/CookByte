import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../interfaces/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  public isLoggedIn(): boolean {
    const user = window.sessionStorage.getItem('auth-user');
    return !!user;
  }

  getUserFromToken(token: string | null): Observable<User> {
    return this.http.get<User>(`http://localhost:8080/auth/user/token?token=${token}`)
  }

  // updateUser(user: User | undefined, token: string | null): Observable<User | null> {
  //   return this.http.put<User>(`http://localhost:8081/api/user/${user?.id}?token=${token}`, {
  //     first_name: user?.first_name,
  //     last_name: user?.last_name,
  //     email: user?.email,
  //     userName: user?.userName,
  //     passw: user?.password,
  //     role: user?.role,
  //     image: user?.image,
  //   });
  // }

}
