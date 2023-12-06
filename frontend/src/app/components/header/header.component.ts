import { Component, OnInit } from '@angular/core';
import { User } from "../../interfaces/user";
import { UserService } from "../../services/user.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  user: User | undefined;

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.getUserFromToken();
  }

  getUserFromToken() {
    const token = localStorage.getItem('token');
    if (!token) {
      console.log('Token is missing.');
      return;
    }
    this.userService.getUserFromToken(token).subscribe({
      next: (user) => {
        this.user = user;
        this.addPathToImage(this.user)
        console.log(this.user.image);
      },
      error: () => {
        console.log("Error in getting user from token");
      }
    });
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  addPathToImage(user: User) {
    user.image = "../../../assets/images/" + user.image;
  }
}
