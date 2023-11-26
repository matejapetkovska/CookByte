import {Recipe} from "./recipe";
import {User} from "./user";

export interface Review {
  description: string,
  recipe: Recipe,
  user: User,
  ratingValue: number
}
