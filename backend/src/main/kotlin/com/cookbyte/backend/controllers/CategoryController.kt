package com.cookbyte.backend.controllers

import com.cookbyte.backend.domain.Category
import com.cookbyte.backend.service.CategoryService
import com.cookbyte.backend.service.IngredientService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController(private val categoryService: CategoryService) {
    @GetMapping
    fun getAllCategories(): List<Category>? = categoryService.findAll()
}