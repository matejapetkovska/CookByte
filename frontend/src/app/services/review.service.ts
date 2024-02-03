import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Review} from "../interfaces/review";
import {Observable} from "rxjs";
import {Recipe} from "../interfaces/recipe";

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

  getReviewsForUser(userId: number | undefined): Observable<Review[]> {
    return this.httpClient.get<Review[]>(`${this.url}/reviewsByUser/${userId}`);
  }
  addReview(formData: FormData): Observable<any> {
    const headers = new HttpHeaders();
    headers.set('Content-Type', 'multipart/form-data');
    return this.httpClient.post<any>(`${this.url}/addReview`, formData)
  }

  deleteReview(reviewId: number): Observable<any> {
    return this.httpClient.delete(`${this.url}/delete/${reviewId}`)
  }
}
