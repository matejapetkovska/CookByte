import {Component, OnInit} from '@angular/core';
import {User} from "../../interfaces/user";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{

  user: User | undefined;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getUserFromToken()
  }

  getUserFromToken() {
    const token = localStorage.getItem('token')
    this.userService.getUserFromToken(token).subscribe({
      next: (user) => {
        this.user = user
        console.log(user)
      },
      error: () => {
        console.log("Error in getting user from token")
      }
    })
  }
}
