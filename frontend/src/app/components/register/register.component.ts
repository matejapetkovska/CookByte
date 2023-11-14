import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  firstName = ''
  lastName = ''
  username = ''
  email  = ''
  password = ''
  image: File | undefined

  errorMessage: String = "";

  constructor(private authService: AuthService, private router: Router) { }
  onFileChange(event: any) {
    this.image = event.target.files[0];
  }
  createForm(): FormData {
    const formData = new FormData()
    if(this.firstName != '' || this.lastName != '' || this.username != '' || this.email != '' || this.password != '') {
      formData.append('firstName', this.firstName)
      formData.append('lastName', this.lastName)
      formData.append('username', this.username)
      formData.append('email', this.email)
      formData.append('password', this.password)
      if(this.image!==undefined)
        formData.append('image', this.image)
    }
    return formData
  }

  onSubmit() {
    this.authService.register(this.createForm()).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token)
        this.router.navigate(['recipes'])
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error.message
      }
    })
  }
}
