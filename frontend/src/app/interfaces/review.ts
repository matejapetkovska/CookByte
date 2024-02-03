import {Recipe} from "./recipe";
import {User} from "./user";

export interface Review {
  id: number,
  description: string,
  recipe: Recipe,
  user: User,
  ratingValue: number
}
