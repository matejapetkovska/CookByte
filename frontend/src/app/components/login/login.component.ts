import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {LoginRequest} from "../../interfaces/login-request";
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

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
  loginForm: FormGroup
  hidePassword = true;

  errorMessage = ''

  constructor(private authService: AuthService, private router: Router,  private formBuilder: FormBuilder) {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    })
  }

  onSubmit() {
    this.authService.login(this.loginForm.value).subscribe({
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
