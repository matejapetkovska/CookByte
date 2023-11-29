package com.cookbyte.backend.controllers

import com.cookbyte.backend.domain.Ingredient
import com.cookbyte.backend.service.IngredientService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ingredients")
class IngredientsController(private val ingredientService: IngredientService) {
    @GetMapping("/{recipeId}")
    fun getAllIngredientsForRecipe(@PathVariable recipeId: Long): List<Ingredient>? = ingredientService.findByRecipeId(recipeId)
}