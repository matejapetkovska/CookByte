package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.domain.Category
import com.cookbyte.backend.domain.Ingredient
import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.domain.User
import com.cookbyte.backend.repository.IngredientRepository
import com.cookbyte.backend.repository.RecipeRepository
import com.cookbyte.backend.repository.ReviewRepository
import com.cookbyte.backend.service.CategoryService
import com.cookbyte.backend.service.IngredientService
import com.cookbyte.backend.service.RecipeService
import com.cookbyte.backend.service.ReviewService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.util.Date
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.transaction.Transactional

@Service
class RecipeServiceImpl(
    val recipeRepository: RecipeRepository,
    val categoryService: CategoryService,
    val ingredientService: IngredientService,
    val reviewService: ReviewService,
    val reviewRepository: ReviewRepository
) : RecipeService {

    val objectMapper = ObjectMapper()

    @Value("\${upload.directory}")
    private lateinit var uploadDirectory: String

    override fun findAll(): List<Recipe> {
        return recipeRepository.findAll()
    }

    override fun findRecipeById(recipeId: Long): Recipe? {
        return recipeRepository.findById(recipeId).get()
    }

    override fun findRecipeByTitle(title: String): Recipe? {
        return recipeRepository.findRecipeByTitle(title)
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
    ): Recipe? {
        val categoryList: List<Long> = objectMapper.readValue(categoryIds, object : TypeReference<List<Long>>() {})
        val categories: Set<Category> = categoryService.findAllByIds(categoryList).orEmpty().toSet()
        val imagePath = generateRandomName() + ".jpg"
        val imagePathWithDirectory = Paths.get(uploadDirectory, imagePath)
        Files.copy(image.inputStream, imagePathWithDirectory)
        val recipe = Recipe(
            0,
            title,
            user,
            LocalDate.now().toString(),
            description,
            imagePath,
            cookTime,
            calories,
            carbohydrates,
            fats,
            proteins,
            instructions,
            categories
        )
        val savedRecipe = recipeRepository.save(recipe)
        val ingredientsList: List<Ingredient> = objectMapper.readValue(ingredients)
        ingredientService.addIngredients(ingredientsList, recipe)
        return savedRecipe
    }


    override fun editRecipe(
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
    ): Recipe? {
        val recipe = this.findRecipeById(id) ?: return null
        recipe.title = title ?: recipe.title
        recipe.description = description ?: recipe.description
        if (image != null) {
            val imagePath = generateRandomName() + ".jpg"
            val imagePathWithDirectory = Paths.get(uploadDirectory, imagePath)
            Files.copy(image.inputStream, imagePathWithDirectory)
            recipe.imageUrl = imagePath
        }
        recipe.cookTime = cookTime ?: recipe.cookTime
        recipe.calories = calories ?: recipe.calories
        recipe.carbohydrates = carbohydrates ?: recipe.calories
        recipe.fats = fats ?: recipe.fats
        recipe.proteins = proteins ?: recipe.proteins
        recipe.instructions = instructions ?: recipe.instructions

        if (categoryIds != null) {
            val categoryList: List<Long> = objectMapper.readValue(categoryIds, object : TypeReference<List<Long>>() {})
            val categories: Set<Category> = categoryService.findAllByIds(categoryList).orEmpty().toSet()
            recipe.categories = categories
        }
        if (ingredientIds != null) {
            val ingredientsList: List<Ingredient> = objectMapper.readValue(ingredientIds)
            ingredientService.addIngredients(ingredientsList, recipe)
        }
        return recipeRepository.save(recipe)
    }

    @Transactional
    override fun deleteRecipe(recipeId: Long) {
        val ingredients = ingredientService.findByRecipeId(recipeId)
        val reviews = reviewService.getReviewByRecipeId(recipeId)

        ingredients?.forEach { ingredient ->
            ingredientService.deleteById(ingredient.id)
        }
        reviews?.forEach { review ->
            review.recipe = null
            reviewRepository.save(review)
        }
        recipeRepository.deleteById(recipeId)
    }

    fun generateRandomName(): String {
        val sb = StringBuilder()
        for (i in 0..8) {
            val rand = listOf(('a'..'z'), ('A'..'Z')).flatten().random()
            sb.append(rand)
        }
        return sb.toString()
    }
}