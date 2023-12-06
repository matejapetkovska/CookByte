import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {AddRecipeDialogComponent} from "../dialog/add-recipe-dialog/add-recipe-dialog.component";
import {UserService} from "../../services/user.service";
import {User} from "../../interfaces/user";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit{

  editBtnContainer = false
  @ViewChild('imageInput') imageInput: ElementRef | undefined;
  fileName: string = '';
  previewUrl: string = '';
  isDialogVisible = false;
  user: User | undefined

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    const token = localStorage.getItem('token')
    this.userService.getUserFromToken(token).subscribe({
      next: (user) => {
        this.user = user
        this.addPathToImage(this.user)
      }
    })
  }

  toggleEdit() {
    this.editBtnContainer = true;
  }

  saveDetails() {
    this.editBtnContainer = false;
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();

    this.fileName = file ? file.name : '';

    reader.onloadend = () => {
      this.previewUrl = reader.result as string;
    };

    if (file) {
      reader.readAsDataURL(file);
    } else {
      this.previewUrl = '';
    }
  }

  openDialog() {
    this.isDialogVisible = true
  }

  addPathToImage(user: User) {
    user.image = "../../../assets/images/" + user.image;
  }
}
