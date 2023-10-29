export interface Recipe {
  id: number,
  title: string,
  user: string, //change
  datePublished: Date,
  description: string,
  imageUrl: string,
  cookTime: number,
  calories: string,
  carbohydrates: string,
  fats: string,
  proteins: string,
  instructions: string,
  categories: string //change
}
