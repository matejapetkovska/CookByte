package com.cookbyte.backend.service

import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.domain.User
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

interface RecipeService {
    fun findAll(): List<Recipe>
    fun findRecipeById(recipeId: Long): Recipe?
    fun findAllRecipesByCategory(categoryId: Long): List<Recipe>?
    fun findAllRecipesByUser(userId: Long): List<Recipe>?
    fun getMostFavouriteRecipes(): List<Recipe>
    fun addRecipe(
        title: String, user: User, datePublished: String,
        description: String,
        image: MultipartFile,
        cookTime: Long, calories: String,
        carbohydrates: String,
        fats: String,
        proteins: String,
        instructions: String,
        ingredient: String,
        categoryIds: List<Long>
    ): Recipe?

    fun editRecipe(
        id: Long, title: String, user: User, datePublished: String,
        description: String,
        image: MultipartFile,
        cookTime: Long, calories: String,
        carbohydrates: String,
        fats: String,
        proteins: String,
        instructions: String,
        ingredient: String,
        categoryIds: List<Long>
    ): Recipe?

    fun deleteRecipe(recipeId: Long)
}