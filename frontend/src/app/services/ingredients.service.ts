import { Injectable } from '@angular/core';
import {Ingredient} from "../interfaces/ingredient";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class IngredientsService {

  ingredient: Ingredient | undefined

  url = 'http://localhost:8080/ingredients'

  constructor(private httpClient: HttpClient) { }

  getAllIngredients(): Observable<Ingredient[]> {
    return this.httpClient.get<Ingredient[]>(`${this.url}`)
  }

  getIngredientsForRecipe(recipeId: number): Observable<Ingredient[]> {
    return this.httpClient.get<Ingredient[]>(`${this.url}/${recipeId}`)
  }
}
