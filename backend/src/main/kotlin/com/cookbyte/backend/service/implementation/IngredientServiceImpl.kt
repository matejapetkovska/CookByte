package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.domain.Ingredient
import com.cookbyte.backend.domain.Recipe
import com.cookbyte.backend.repository.IngredientRepository
import com.cookbyte.backend.service.IngredientService
import org.springframework.stereotype.Service

@Service
class IngredientServiceImpl(val ingredientRepository: IngredientRepository): IngredientService {
    override fun addIngredient(name: String, recipe: Recipe): Ingredient? {
        return ingredientRepository.save(Ingredient(0, name, recipe))
    }
}