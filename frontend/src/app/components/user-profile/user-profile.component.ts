import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {AddRecipeDialogComponent} from "../dialog/add-recipe-dialog/add-recipe-dialog.component";
import {UserService} from "../../services/user.service";
import {User} from "../../interfaces/user";
import {RecipeService} from "../../services/recipe.service";
import {ReviewService} from "../../services/review.service";
import {Recipe} from "../../interfaces/recipe";
import {Review} from "../../interfaces/review";

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

  constructor(private userService: UserService, private recipeService: RecipeService, private reviewService: ReviewService) { }

  ngOnInit(): void {
    const token = localStorage.getItem('token')
    this.userService.getUserFromToken(token).subscribe({
      next: (user) => {
        this.user = user
        this.editedUser = user
        this.addPathToImage(this.user)
      }
    })
    if(this.user) {
      this.recipeService.getRecipesForUser(this.user.id).subscribe({
        next: (recipes) => {
          this.recipes = recipes
          this.totalRecipes = this.recipes.length;
          console.log(this.recipes)
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

  saveDetails() {
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
