import {Category} from "./category";
import {User} from "./user";

export interface Recipe {
  id: number,
  title: string,
  user: User,
  datePublished: string,
  description: string,
  imageUrl: string,
  cookTime: number,
  calories: string,
  carbohydrates: string,
  fats: string,
  proteins: string,
  instructions: string,
  categories: Category[],
  averageRating: number
}
