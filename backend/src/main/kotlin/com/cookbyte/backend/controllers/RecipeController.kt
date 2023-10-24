package com.cookbyte.backend.controllers

import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.service.RecipeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recipes")
class RecipeController(val recipeService: RecipeService) {
    @GetMapping
    fun getAllRecipes(): List<Recipe> {
        return recipeService.findAll()
    }
}