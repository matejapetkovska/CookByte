package com.cookbyte.backend.repository

import com.cookbyte.backend.domain.Ingredient
import org.springframework.data.jpa.repository.JpaRepository

interface IngredientRepository: JpaRepository<Ingredient, Long> {
    fun findAllByRecipeId(recipeId: Long): List<Ingredient>?
}