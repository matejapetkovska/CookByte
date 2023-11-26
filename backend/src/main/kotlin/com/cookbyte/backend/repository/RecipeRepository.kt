package com.cookbyte.backend.repository

import com.cookbyte.backend.domain.Recipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository: JpaRepository<Recipe, Long> {
    fun findRecipesByUserId(userId: Long): List<Recipe>?
    fun findRecipeByTitle(title: String): Recipe?
}