package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.repository.RecipeRepository
import com.cookbyte.backend.service.RecipeService
import org.springframework.stereotype.Service

@Service
class RecipeServiceImpl(val recipeRepository: RecipeRepository) : RecipeService {
    override fun findAll(): List<Recipe> {
        return recipeRepository.findAll()
    }

    override fun getMostFavouriteRecipes(): List<Recipe> {
        val allRecipes = recipeRepository.findAll()
        allRecipes.shuffle()
        return allRecipes.take(3)
    }
}