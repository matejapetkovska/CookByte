package com.cookbyte.backend.service

import com.cookbyte.backend.domain.Ingredient
import com.cookbyte.backend.domain.Recipe


interface IngredientService {
    fun addIngredient(name: String, recipe: Recipe): Ingredient?
}