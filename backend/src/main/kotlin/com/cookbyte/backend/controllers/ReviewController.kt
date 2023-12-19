package com.cookbyte.backend.controllers

import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.domain.Review
import com.cookbyte.backend.service.RecipeService
import com.cookbyte.backend.service.ReviewService
import com.cookbyte.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reviews")
class ReviewController(private val reviewService: ReviewService, private val userService: UserService, private val recipeService: RecipeService) {

    @GetMapping
    fun getAllReviews(): List<Review>? = reviewService.findAll()

    @GetMapping("/{recipeId}")
    fun getReviewByRecipeId(@PathVariable recipeId: Long): List<Review>? = reviewService.getReviewByRecipeId(recipeId)

    @GetMapping("/reviewsByUser/{userId}")
    fun getReviewsByUserId(@PathVariable userId: Long): List<Review>? {
        println("Received request for user ID: $userId");
        return reviewService.getReviewsByUserId(userId)
    }

    @PostMapping("/addReview")
    fun addReview(@RequestParam ratingValue: String,
                  @RequestParam reviewDescription: String,
                  @RequestParam recipeId: String,
                  @RequestParam token: String): ResponseEntity<Any> {
        val user = userService.getUserFromToken(token) ?: return ResponseEntity.badRequest().body(Error("Error in adding review. Please log in first."))
        val recipe = recipeService.findRecipeById(recipeId.toLong()) ?: return ResponseEntity.badRequest().body(Error("Error in adding review. Please log in first."))
        val review = reviewService.addReview(reviewDescription, user, recipe, ratingValue.toInt())
        return ResponseEntity.ok(review)
    }

}