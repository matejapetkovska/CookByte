import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../interfaces/user";
import {RecipeService} from "../../services/recipe.service";
import {ReviewService} from "../../services/review.service";
import {Recipe} from "../../interfaces/recipe";
import {Review} from "../../interfaces/review";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {DeleteDialogComponent} from "../dialog/delete-dialog/delete-dialog.component";
import {EditRecipeDialogComponent} from "../dialog/edit-recipe-dialog/edit-recipe-dialog.component";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit{

  editBtnContainer = false
  @ViewChild('imageInput') imageInput: ElementRef | undefined;
  fileName: string = '';
  previewUrl: string | undefined = '';
  isDialogVisible = false;
  user: User | undefined
  recipes: Recipe[] | undefined
  reviews: Review[] | undefined
  totalReviews = 0;
  totalRecipes = 0;
  editedUser: User | undefined
  image: File | undefined

  constructor(private userService: UserService, private recipeService: RecipeService, private reviewService: ReviewService, private router: Router, public dialog: MatDialog) { }

  ngOnInit(): void {
    const token = localStorage.getItem('token')
    this.userService.getUserFromToken(token).subscribe({
      next: (user) => {
        this.user = user
        this.editedUser = user
        this.addPathToImage(this.user)
        this.previewUrl = user.image
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
              this.addPathToRecipeImagesFromReviews(this.reviews)
              this.totalReviews = this.reviews.length
            }
          })
        }
      }
    })
  }

  editUserProfile(): void {
    this.editBtnContainer = false;
    const token = localStorage.getItem('token');
    const userId = this.editedUser?.id;

    const formData = new FormData();
    formData.append('firstName', this.editedUser?.firstName || '');
    formData.append('lastName', this.editedUser?.lastName || '');

    if (this.image) {
      formData.append('image', this.image, this.image.name);
    }
    this.userService.updateUser(formData, userId, token).subscribe(
      (updatedUser) => {
        if (updatedUser) {
          this.user = { ...this.editedUser };
          window.location.reload()
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
    this.image = file
    const reader = new FileReader();

    this.fileName = file ? file.name : '';

    reader.onloadend = () => {
      this.previewUrl = reader.result as string;
    };

    if (file) {
      reader.readAsDataURL(file);
    } else {
      this.previewUrl = this.user?.image;
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

  editRecipe(recipeId: number) {
    this.dialog.open(EditRecipeDialogComponent, {
      width: '250px',
      enterAnimationDuration: '0ms',
      exitAnimationDuration: '0ms',
      data: {
        recipeId: recipeId,
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

  addPathToRecipeImagesFromReviews(reviews: Review[]) {
    if (reviews != undefined) {
      for (let i = 0; i < reviews.length; i++) {
        // @ts-ignore
        if (!this.reviews[i].recipe.imageUrl.startsWith('http://') && !this.reviews[i].recipe.imageUrl.startsWith('https://')) {
          reviews[i].recipe.imageUrl = "../../../assets/user-uploaded-images/" + reviews[i].recipe.imageUrl;
        }
      }
    }
  }
}
