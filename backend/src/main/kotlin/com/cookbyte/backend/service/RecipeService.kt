package com.cookbyte.backend.service

import com.cookbyte.backend.domain.Recipe
import org.springframework.stereotype.Service

interface RecipeService {
    fun findAll(): List<Recipe>
    fun getMostFavouriteRecipes(): List<Recipe>
}