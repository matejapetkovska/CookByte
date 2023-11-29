import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Review} from "../interfaces/review";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  url = 'http://localhost:8080/reviews'

  reviews: Review[] | undefined

  constructor(private httpClient: HttpClient) {
  }

  getAllReviews(): Observable<Review[]> {
    return this.httpClient.get<Review[]>(this.url)
  }

  getReviewForRecipe(recipeId: number): Observable<Review[]> {
    return this.httpClient.get<Review[]>(`${this.url}/${recipeId}`)
  }
}