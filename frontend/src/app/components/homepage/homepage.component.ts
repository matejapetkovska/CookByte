import {Component, OnInit} from '@angular/core';
import {RecipeService} from "../../services/recipe.service";
import {Recipe} from "../../interfaces/recipe";
import {Router} from "@angular/router";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  recipes: Recipe[] | undefined

  constructor(private recipeService: RecipeService, private router: Router) {
  }

  ngOnInit(): void {
    this.recipeService.getMostFavouriteRecipes().subscribe({
      next: (recipes) => {
        this.recipes = recipes
        for(const recipe of this.recipes) {
          recipe.title = this.recipeService.cleanText(recipe.title)
          recipe.description = this.recipeService.cleanText(recipe.description)
        }
      }, error: () => {
        console.log("error in getting recipes")
      }
    })
  }

  onClick(){
    this.router.navigateByUrl("/recipes")
  }
}
