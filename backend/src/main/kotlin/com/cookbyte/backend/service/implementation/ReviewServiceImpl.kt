package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.domain.Review
import com.cookbyte.backend.domain.User
import com.cookbyte.backend.repository.ReviewRepository
import com.cookbyte.backend.service.RecipeService
import com.cookbyte.backend.service.ReviewService
import org.springframework.stereotype.Service

@Service
class ReviewServiceImpl(private val reviewRepository: ReviewRepository): ReviewService {
    override fun findAll(): List<Review>? {
        return reviewRepository.findAll()
    }

    override fun getReviewByRecipeId(recipeId: Long): List<Review>? {
        return reviewRepository.getReviewByRecipeId(recipeId)
    }

    override fun getReviewsByUserId(userId: Long): List<Review>? {
        return reviewRepository.getReviewsByUserId(userId)
    }

    override fun addReview(description: String, user: User, recipe: Recipe, ratingValue: Int): Review? {
        return reviewRepository.save(Review(0, description, user, recipe, ratingValue))
    }
}