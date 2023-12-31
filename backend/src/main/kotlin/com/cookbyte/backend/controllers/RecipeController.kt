package com.cookbyte.backend.controllers

import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.service.RecipeService
import com.cookbyte.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/recipes")
class RecipeController(val recipeService: RecipeService, val userService: UserService) {
    @GetMapping
    fun getAllRecipes(): List<Recipe> = recipeService.findAll()

    @GetMapping("/mostFavourite")
    fun getMostFavouriteRecipes(): List<Recipe> = recipeService.getMostFavouriteRecipes()

    @GetMapping("/{recipeId}")
    fun getRecipe(@PathVariable recipeId: Long): Recipe? = recipeService.findRecipeById(recipeId)

    @GetMapping("/recipesByUser/{userId}")
    fun findAllRecipesByUser(@PathVariable userId: Long): List<Recipe>? = recipeService.findAllRecipesByUser(userId)

    @PostMapping("/add")
    fun createRecipe(@RequestParam title: String,
                     @RequestParam file: MultipartFile,
                     @RequestParam description: String,
                     @RequestParam cookTime: String,
                     @RequestParam calories: String,
                     @RequestParam carbohydrates: String,
                     @RequestParam fats: String,
                     @RequestParam proteins: String,
                     @RequestParam instructions: String,
                     @RequestParam categoryIds: String,
                     @RequestParam ingredients: String,
                     @RequestParam token: String): ResponseEntity<Any> {
        val user = userService.getUserFromToken(token) ?: return ResponseEntity.badRequest().body(Error("Error in saving recipe. Please log in first."))
        val recipe = recipeService.addRecipe(title, user, description, file, cookTime.toLong(), calories, carbohydrates, fats, proteins, instructions, ingredients, categoryIds)
        return ResponseEntity.ok(recipe)
    }

    @DeleteMapping("/delete/{recipeId}")
    fun deleteRecipe(@PathVariable recipeId: Long) = recipeService.deleteRecipe(recipeId)
}