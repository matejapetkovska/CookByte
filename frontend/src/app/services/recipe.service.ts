import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Recipe} from "../interfaces/recipe";

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  url = 'http://localhost:8080/recipes'
  constructor(private httpClient: HttpClient) { }

  getMostFavouriteRecipes(): Observable<Recipe[]> {
    return this.httpClient.get<Recipe[]>(`${this.url}/mostFavourite`)
  }

  getAllRecipes(): Observable<Recipe[]> {
    return this.httpClient.get<Recipe[]>(this.url)
  }

  getRecipeDetails(recipeId: number): Observable<Recipe> {
    return this.httpClient.get<Recipe>(`${this.url}/${recipeId}`)
  }

  getRecipesForUser(userId: number | undefined): Observable<Recipe[]> {
    return this.httpClient.get<Recipe[]>(`${this.url}/recipesByUser/${userId}`)
  }

  addRecipe(formData: FormData): Observable<any> {
    return this.httpClient.post<FormData>(`${this.url}/add`, formData);
  }

  editRecipe(recipeId: number | null, formData: FormData): Observable<any> {
    return this.httpClient.put<FormData>(`${this.url}/edit/${recipeId}`, formData);
  }

  deleteRecipe(recipeId: string | null): Observable<any> {
    return this.httpClient.delete(`${this.url}/delete/${recipeId}`)
  }

  cleanText(inputText: string): string {
    return inputText.replace(/\r/g, '')
      .replace(/\n/g, '')
      .replace(/\r\n/g, '')
      .replace(/&#39;/g, "'")
      .replace(/&amp;/g, '&')
      .replace(/&#34;/g, '"');
  }
}
