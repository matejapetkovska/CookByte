package com.cookbyte.backend.service

import com.cookbyte.backend.domain.Category
import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.domain.User
import org.springframework.web.multipart.MultipartFile

interface RecipeService {
    fun findAll(): List<Recipe>
    fun findRecipeById(recipeId: Long): Recipe?
    fun findRecipeByTitle(title: String): Recipe?
    fun findCategoriesForRecipe(recipeId: Long): Set<Category>?
    fun findAllRecipesByUser(userId: Long): List<Recipe>?
    fun getMostFavouriteRecipes(): List<Recipe>
    fun addRecipe(
        title: String, user: User,
        description: String,
        image: MultipartFile,
        cookTime: Long,
        calories: String,
        carbohydrates: String,
        fats: String,
        proteins: String,
        instructions: String,
        ingredients: String,
        categoryIds: String
    ): Recipe?

    fun editRecipe(
        id: Long, title: String?, datePublished: String,
        description: String?,
        image: MultipartFile?,
        cookTime: Long?, calories: String?,
        carbohydrates: String?,
        fats: String?,
        proteins: String?,
        instructions: String?,
        ingredientIds: String?,
        categoryIds: String?
    ): Recipe?

    fun deleteRecipe(recipeId: Long)
    fun searchRecipes(searchTerm: String): List<Recipe>?
}