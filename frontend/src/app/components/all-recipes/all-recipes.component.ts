import {Component, OnInit} from '@angular/core';
import {RecipeService} from "../../services/recipe.service";
import {Recipe} from "../../interfaces/recipe";

@Component({
  selector: 'app-all-recipes',
  templateUrl: './all-recipes.component.html',
  styleUrls: ['./all-recipes.component.css']
})
export class AllRecipesComponent implements OnInit {

  recipes: Recipe[] | undefined
  randomRecipe: Recipe | undefined

  constructor(private recipeService: RecipeService) {
  }

  ngOnInit(): void {
    this.recipeService.getAllRecipes().subscribe({
      next: (recipes) => {
        this.recipes = recipes
        this.randomRecipe = this.recipes[Math.floor(Math.random() * this.recipes.length)];
        console.log(this.randomRecipe)
        for(const recipe of this.recipes) {
          recipe.title = this.recipeService.cleanText(recipe.title)
        }
      }, error: () => {
        console.log("error")
      }
    })
  }
}
