package com.cookbyte.backend.controllers

import com.cookbyte.backend.domain.Review
import com.cookbyte.backend.service.ReviewService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reviews")
class ReviewController(private val reviewService: ReviewService) {

    @GetMapping
    fun getAllReviews(): List<Review>? = reviewService.findAll()


    @GetMapping("/{recipeId}")
    fun getReviewByRecipeId(@PathVariable recipeId: Long): List<Review>? = reviewService.getReviewByRecipeId(recipeId)
}