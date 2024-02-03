package com.cookbyte.backend.service

import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.domain.Review
import com.cookbyte.backend.domain.User

interface ReviewService {
    fun findAll(): List<Review>?
    fun getReviewByRecipeId(recipeId: Long): List<Review>?
    fun getReviewsByUserId(userId: Long): List<Review>?
    fun addReview(description: String, user: User, recipe: Recipe, ratingValue: Int): Review?
    fun deleteReview(reviewId: Long)
}