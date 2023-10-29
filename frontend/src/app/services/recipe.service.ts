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

  cleanText(inputText: string): string {
    return inputText.replace(/\r/g, '')
      .replace(/\n/g, '')
      .replace(/\r\n/g, '')
      .replace(/&#39;/g, "'")
      .replace(/&amp;/g, '&')
      .replace(/&#34;/g, '"');
  }
}
