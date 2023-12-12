package com.cookbyte.backend.repository

import com.cookbyte.backend.domain.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository: JpaRepository<Review, Long> {
    fun getReviewByRecipeId(recipeId: Long): List<Review>?
    fun getReviewsByUserId(userId: Long): List<Review>?
}