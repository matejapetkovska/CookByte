import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import {UserService} from "../../services/user.service";
import {User} from "../../interfaces/user";
import {RecipeService} from "../../services/recipe.service";
import {ReviewService} from "../../services/review.service";
import {Recipe} from "../../interfaces/recipe";
import {Review} from "../../interfaces/review";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {DeleteDialogComponent} from "../dialog/delete-dialog/delete-dialog.component";

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
  recipes: Recipe[] | undefined
  reviews: Review[] | undefined
  totalReviews = 0;
  totalRecipes = 0;
  editedUser: User | undefined

  constructor(private userService: UserService, private recipeService: RecipeService, private reviewService: ReviewService, private router: Router, public dialog: MatDialog) { }

  ngOnInit(): void {
    const token = localStorage.getItem('token')
    this.userService.getUserFromToken(token).subscribe({
      next: (user) => {
        this.user = user
        this.editedUser = user
        this.addPathToImage(this.user)
        if(this.user) {
          this.recipeService.getRecipesForUser(this.user.id).subscribe({
            next: (recipes) => {
              this.recipes = recipes
              this.addPathToRecipeImages(this.recipes)
              this.totalRecipes = this.recipes.length;
            }
          })
          this.reviewService.getReviewsForUser(this.user.id).subscribe({
            next: (reviews) => {
              this.reviews = reviews
              this.totalReviews = this.reviews.length
            }
          })
        }
      }
    })
  }

  editUserProfile(): void {
    this.editBtnContainer = false;
    const token = localStorage.getItem('token')
    this.userService.updateUser(this.editedUser, token).subscribe(
      (updatedUser) => {
        if (updatedUser) {
          this.user = {...this.editedUser};
        }
      },
      (error) => {
        console.error('Error updating user data:', error);
      }
    );
  }

  toggleEdit() {
    this.editBtnContainer = true;
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

  deleteRecipe(recipeId: number, recipeTitle: string) {
    this.dialog.open(DeleteDialogComponent, {
      width: '250px',
      enterAnimationDuration: '0ms',
      exitAnimationDuration: '0ms',
      data: {
        recipeId: recipeId,
        recipeTitle: recipeTitle
      }
    });

  }


  openDialog() {
    this.isDialogVisible = true
  }

  addPathToImage(user: User) {
    user.image = "../../../assets/user-uploaded-images/" + user.image;
  }

  addPathToRecipeImages(recipes: Recipe[]) {
    for (let i = 0; i < recipes.length; i++) {
      recipes[i].imageUrl = "../../../assets/user-uploaded-images/" + recipes[i].imageUrl;
    }
  }
}
