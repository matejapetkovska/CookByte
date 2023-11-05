package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.domain.Category
import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.domain.User
import com.cookbyte.backend.repository.RecipeRepository
import com.cookbyte.backend.service.CategoryService
import com.cookbyte.backend.service.IngredientService
import com.cookbyte.backend.service.RecipeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

@Service
class RecipeServiceImpl(val recipeRepository: RecipeRepository,
                        val categoryService: CategoryService,
                        val ingredientService: IngredientService) : RecipeService {

    @Value("\${upload.directory}")
    private lateinit var uploadDirectory: String

    override fun findAll(): List<Recipe> {
        return recipeRepository.findAll()
    }

    override fun findRecipeById(recipeId: Long): Recipe? {
        return recipeRepository.findById(recipeId).get()
    }

    override fun findCategoriesForRecipe(recipeId: Long): Set<Category>? {
        val recipe = this.findRecipeById(recipeId)
        return recipe?.categories
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
        val categories = categoryService.findAllByIds(categoryIds).orEmpty()
        val imagePath = generateRandomName() + ".jpg"
        val imagePathWithDirectory = Paths.get(uploadDirectory, imagePath)
        Files.copy(image.inputStream, imagePathWithDirectory)
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
        ingredientIds: List<Long>,
        categoryIds: List<Long>
    ): Recipe? {
        val recipe = this.findRecipeById(id)
        if(recipe != null) {
            recipe.title = title
            recipe.description = description
            val imagePath = generateRandomName() + ".jpg"
            val imagePathWithDirectory = Paths.get(uploadDirectory, imagePath)
            Files.copy(image.inputStream, imagePathWithDirectory)
            recipe.imageUrl = imagePath
            recipe.cookTime = cookTime
            recipe.calories = calories
            recipe.carbohydrates = carbohydrates
            recipe.fats = fats
            recipe.proteins = proteins
            recipe.instructions = instructions

            val categories = recipe.categories.toMutableSet()
            val existingCategories = categories.map { it.id }.toSet()
            val addedCategories = categoryIds.filter { it !in existingCategories }
            val removedCategories = existingCategories.filter { it !in categoryIds }
            categories.addAll(categoryService.findAllByIds(addedCategories).orEmpty())
            categories.removeAll { it.id in removedCategories }

            val ingredients = ingredientService.findByRecipeId(id)
            if(ingredients != null) {
                val existingIngredients = ingredients.map { it.id }.toMutableList()
                val addedIngredients = ingredientIds.filter { it !in existingIngredients }
                val removedIngredients = existingIngredients.filter { it !in ingredientIds }
                for (ingredientId in addedIngredients) {
                    val ingredient = ingredientService.findById(ingredientId)
                    ingredient?.recipe = recipe
                    ingredient?.name?.let { ingredientService.addIngredient(it, recipe) }
                }
                for(ingredientId in removedIngredients) {
                    ingredientService.deleteById(ingredientId)
                }
            }
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