import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../interfaces/user";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  user: User | undefined

  constructor(private userService: UserService) {}
  ngOnInit(): void {
    this.getUserFromToken()
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
      },
      error: () => {
        console.log("Error in getting user from token");
      }
    });
  }
}
