import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {LoginRequest} from "../../interfaces/login-request";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  request: LoginRequest = {
    username: '',
    password: ''
  }

  errorMessage = ''

  constructor(private authService: AuthService, private router: Router) {  }

  onSubmit() {
    this.authService.login(this.request).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['recipes'])
      },
      error: (error: HttpErrorResponse) => {
        console.log("error in logging in")
        this.errorMessage = error.error.message;
      }
    })
  }

}
