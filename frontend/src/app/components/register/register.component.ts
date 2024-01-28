import { Component } from '@angular/core';
import { AuthService } from "../../services/auth.service";
import { Router } from "@angular/router";
import { HttpErrorResponse } from "@angular/common/http";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {

  hidePassword = true;
  hideRepeatPassword = true;
  registerForm: FormGroup;
  errorMessage: String = "";

  constructor(private authService: AuthService, private router: Router, private formBuilder: FormBuilder) {
    this.registerForm = this.createForm();
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    this.registerForm.setValue({
      image: file
    });
  }

  createForm(): FormGroup {
    return this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      repeatPassword: [''],
      image: null,
    });
  }

  onSubmit() {
    const formData = new FormData();
    const { firstName, lastName, username, email, password, image } = this.registerForm.value;
    formData.append('firstName', firstName);
    formData.append('lastName', lastName);
    formData.append('username', username);
    formData.append('email', email);
    formData.append('password', password);
    formData.append('image', image);


    this.authService.register(formData).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['recipes']);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error.message;
      }
    });
  }

  getEmailErrorMessage() {
    if (!this.registerForm) {
      return 'Form not initialized';
    }
    const emailControl = this.registerForm.get('email');
    return emailControl?.hasError('email') ? 'Not a valid email' : '';
  }

  getPasswordErrorMessage() {
    const passwordControl = this.registerForm.get('password');
    if (!passwordControl) {
      return 'Password control not found';
    }
    return passwordControl.hasError('minlength') ? 'Password must be at least 8 characters' : '';
  }

  passwordsMatchValidator() {
    const password = this.registerForm.get('password')?.value;
    const repeatPassword = this.registerForm.get('repeatPassword')?.value;
    return password === repeatPassword ? null : { passwordsNotMatch: true };
  }

  login() {
    this.router.navigate(["login"])
  }
}
