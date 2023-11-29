import {Recipe} from "./recipe";

export interface Ingredient {
  id: number,
  name: string,
  recipe: Recipe
}
