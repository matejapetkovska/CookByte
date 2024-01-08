import {Component, OnInit} from '@angular/core';
import {RecipeService} from "../../services/recipe.service";
import {UserService} from "../../services/user.service";
import {ActivatedRoute} from "@angular/router";
import {Recipe} from "../../interfaces/recipe";
import {DatePipe} from '@angular/common';
import {Review} from "../../interfaces/review";
import {ReviewService} from "../../services/review.service";
import {IngredientsService} from "../../services/ingredients.service";
import {Ingredient} from "../../interfaces/ingredient";
import {User} from "../../interfaces/user";

@Component({
  selector: 'app-recipe-details',
  templateUrl: './recipe-details.component.html',
  styleUrls: ['./recipe-details.component.css'],
  providers: [DatePipe]
})
export class RecipeDetailsComponent implements OnInit {

  recipe: Recipe | undefined
  userLoggedIn = false
  user: User | undefined
  reviews: Review[] | undefined
  recipeRatingMap = new Map<number, { totalRating: number, count: number }>();
  ingredients: Ingredient[] | undefined
  showReviewContainer = false
  ratingValue: number | undefined
  reviewDescription: string = ''

  constructor(private recipeService: RecipeService,
              private userService: UserService,
              private reviewService: ReviewService,
              private ingredientService: IngredientsService,
              private route: ActivatedRoute,
              private datePipe: DatePipe) {
  }


  ngOnInit(): void {
    this.userLoggedIn = this.userService.isLoggedIn()
    this.route.paramMap.subscribe(params => {
      const recipeId = Number(params.get('recipeId'));
      this.fetchRecipeDetails(recipeId);
    });
    this.getLoggedInUserId()
  }


  fetchRecipeDetails(recipeId: number | null): void {
    if (!recipeId)
      return;
    this.recipeService.getRecipeDetails(recipeId).subscribe((recipe) => {
        this.recipe = recipe;
        if (!this.recipe.imageUrl.startsWith('http://') && !this.recipe.imageUrl.startsWith('https://')) {
          this.addPathToRecipeImages(recipe);
        }
        this.addPathToUserImagesFromRecipes(this.recipe)
        if (this.recipe && this.recipe.datePublished) {
          const originalDate = new Date(this.recipe.datePublished);
          this.recipe.datePublished = originalDate.toLocaleDateString('en-US', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
          });
        }
        this.ingredientService.getIngredientsForRecipe(recipeId).subscribe({
          next: (ingredients) => {
            this.ingredients = ingredients
          }
        })
        this.reviewService.getReviewForRecipe(recipeId).subscribe({
          next: (reviews) => {
            this.reviews = reviews
            console.log(this.reviews)
            this.addPathToUserImagesFromReviews(this.reviews)
            this.reviews.forEach(review => {
              const recipeId = review.recipe.id;
              const ratingValue = review.ratingValue;
              if (this.recipeRatingMap.has(recipeId)) {
                const existingData = this.recipeRatingMap.get(recipeId)!;
                existingData.totalRating += ratingValue;
                existingData.count += 1;
              } else {
                this.recipeRatingMap.set(recipeId, {totalRating: ratingValue, count: 1});
              }
            });
            if (this.recipe) {
              const recipeId = this.recipe.id;
              const ratingData = this.recipeRatingMap.get(recipeId);
              if (ratingData) {
                this.recipe.averageRating = Number((ratingData.totalRating / ratingData.count).toFixed(2));
              } else {
                this.recipe.averageRating = 0;
              }
            }
          }
        })
      },
      (error) => {
        console.error('Error fetching recipe details:', error);
      }
    );
  }

  getLoggedInUserId() {
    const token = localStorage.getItem('token')
    this.userService.getUserFromToken(token).subscribe({
      next: (user) => {
        this.user = user
        this.addPathToUserImage(this.user)
      },
      error: () => {
        console.log("error in getting user from token")
      }
    })
  }

  getReviewCount(): number {
    const recipeId = this.recipe?.id || 0;
    return this.recipeRatingMap.get(recipeId)?.count || 0;
  }

  toggleReview() {
    if(this.user != undefined)
      this.user.image = "../../../assets/images/" + this.user.image;
    this.showReviewContainer = true
  }

  closeReviewDialog() {
    this.showReviewContainer = false
  }

  addPathToUserImagesFromRecipes(recipe: Recipe) {
    recipe.user.image = "../../../assets/images/" + recipe.user.image;
  }

  addPathToUserImagesFromReviews(reviews: Review[]) {
    for (let i = 0; i < reviews.length; i++) {
      reviews[i].user.image = "../../../assets/images/" + reviews[i].user.image;
    }
  }

  addPathToUserImage(user: User) {
    user.image = "../../../assets/images/" + user.image;
  }

  addPathToRecipeImages(recipes: Recipe) {
    recipes.imageUrl = "../../../assets/user-uploaded-images/" + recipes.imageUrl;
  }

  addReview() {
    const token = localStorage.getItem('token')
    const formData = new FormData()
    if (this.ratingValue != null && this.reviewDescription != null && this.recipe != undefined &&
      token != null) {
      formData.append('ratingValue', this.ratingValue.toString())
      formData.append('reviewDescription', this.reviewDescription.toString())
      formData.append('recipeId', this.recipe?.id.toString());
      formData.append('token', token.toString())
    }
    this.reviewService.addReview(formData).subscribe({
      next: (newReview) => {
        this.reviews?.push(newReview)
        this.addPathToUserImagesFromReviews(this.reviews!!)
        this.reviewDescription = ''
        this.ratingValue = undefined
      }
    })
    this.getReviewCount()
  }

  allUsersNotNull(reviews: any[]): boolean {
    return reviews.every(review => review.user !== null);
  }

}
