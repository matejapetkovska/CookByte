package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.domain.User
import com.cookbyte.backend.repository.RecipeRepository
import com.cookbyte.backend.service.CategoryService
import com.cookbyte.backend.service.IngredientService
import com.cookbyte.backend.service.RecipeService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

@Service
class RecipeServiceImpl(val recipeRepository: RecipeRepository, val categoryService: CategoryService, val ingredientService: IngredientService) : RecipeService {
    override fun findAll(): List<Recipe> {
        return recipeRepository.findAll()
    }

    override fun findRecipeById(recipeId: Long): Recipe? {
        return recipeRepository.findById(recipeId).get()
    }

    override fun findAllRecipesByCategory(categoryId: Long): List<Recipe>? {
        return recipeRepository.findRecipesByCategoryId(categoryId)
    }

    override fun findAllRecipesByUser(userId: Long): List<Recipe>? {
        return recipeRepository.findRecipesByUserId(userId)
    }

    override fun getMostFavouriteRecipes(): List<Recipe> {
        val allRecipes = recipeRepository.findAll()
        allRecipes.shuffle()
        return allRecipes.take(3)
    }

    override fun addRecipe(
        title: String,
        user: User,
        datePublished: String,
        description: String,
        image: MultipartFile,
        cookTime: Long,
        calories: String,
        carbohydrates: String,
        fats: String,
        proteins: String,
        instructions: String,
        ingredient: String,
        categoryIds: List<Long>
    ): Recipe? {
        val categories = categoryService.findAllById(categoryIds).orEmpty()
        val imagePath = generateRandomName() + ".jpg"
        Files.copy(image.inputStream, Paths.get("CookByte\\frontend\\src\\assets\\user-uploaded-images", imagePath))
        val recipe = Recipe(0, title, user, datePublished, description, imagePath, cookTime, calories, carbohydrates, fats, proteins, instructions, categories)
        this.ingredientService.addIngredient(ingredient, recipe)
        return recipeRepository.save(recipe)
    }

    override fun editRecipe(
        id: Long,
        title: String,
        user: User,
        datePublished: String,
        description: String,
        image: MultipartFile,
        cookTime: Long,
        calories: String,
        carbohydrates: String,
        fats: String,
        proteins: String,
        instructions: String,
        ingredient: String,
        categoryIds: List<Long>
    ): Recipe? {
        val recipe = this.findRecipeById(id)
        if(recipe != null) {
            recipe.title = title
            recipe.description = description
            val imagePath = generateRandomName() + ".jpg"
            Files.copy(image.inputStream, Paths.get("CookByte\\frontend\\src\\assets\\user-uploaded-images", imagePath))
            recipe.imageUrl = imagePath
            recipe.cookTime = cookTime
            recipe.calories = calories
            recipe.carbohydrates = carbohydrates
            recipe.fats = fats
            recipe.proteins = proteins
            recipe.instructions = instructions
            //TODO: Check categories and ingredients
            return recipeRepository.save(recipe)
        }
        return null
    }

    override fun deleteRecipe(recipeId: Long) {
        recipeRepository.deleteById(recipeId)
    }

    fun generateRandomName(): String {
        val allowedChars = listOf(('a'..'z'),('A'..'Z'),(0..9))
        return (1..8)
            .map { allowedChars.random() }
            .joinToString("")
    }
}