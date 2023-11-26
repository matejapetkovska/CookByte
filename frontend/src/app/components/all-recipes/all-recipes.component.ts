import {Component, OnInit} from '@angular/core';
import {RecipeService} from "../../services/recipe.service";
import {Recipe} from "../../interfaces/recipe";
import {ReviewService} from "../../services/review.service";
import {Review} from "../../interfaces/review";

@Component({
  selector: 'app-all-recipes',
  templateUrl: './all-recipes.component.html',
  styleUrls: ['./all-recipes.component.css']
})
export class AllRecipesComponent implements OnInit {

  recipes: Recipe[] | undefined
  randomRecipe: Recipe | undefined
  reviews: Review[] | undefined

  constructor(private recipeService: RecipeService, private reviewService: ReviewService) {
  }

  ngOnInit(): void {
    this.reviewService.getAllReviews().subscribe({
      next: (reviews) => {
        this.reviews = reviews
        const recipeRatingMap = new Map<number, { totalRating: number, count: number }>();
        this.reviews.forEach(review => {
          const recipeId = review.recipe.id;
          const ratingValue = review.ratingValue;
          if (recipeRatingMap.has(recipeId)) {
            const existingData = recipeRatingMap.get(recipeId)!;
            existingData.totalRating += ratingValue;
            existingData.count += 1;
          } else {
            recipeRatingMap.set(recipeId, { totalRating: ratingValue, count: 1 });
          }
        });
        this.recipes?.forEach((recipe) => {
          const recipeId = recipe.id;
          const ratingData = recipeRatingMap.get(recipeId);
          if(ratingData) {
            recipe.averageRating = Number((ratingData.totalRating / ratingData.count).toFixed(2));
          }
          else {
            recipe.averageRating = 0;
          }
        })
      }
    })
    this.recipeService.getAllRecipes().subscribe({
      next: (recipes) => {
        this.recipes = recipes
        this.randomRecipe = this.recipes[Math.floor(Math.random() * this.recipes.length)];
        for(const recipe of this.recipes) {
          recipe.title = this.recipeService.cleanText(recipe.title)
        }
      }, error: () => {
        console.log("error")
      }
    })
  }
}
