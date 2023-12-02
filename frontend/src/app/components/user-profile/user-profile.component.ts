import { Component, ElementRef, ViewChild } from '@angular/core';
import {AddRecipeDialogComponent} from "../dialog/add-recipe-dialog/add-recipe-dialog.component";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {

  editBtnContainer = false
  @ViewChild('imageInput') imageInput: ElementRef | undefined;
  fileName: string = '';
  previewUrl: string = '';
  isDialogVisible = false;

  constructor() {
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
}
