package com.cookbyte.backend.controllers

import com.cookbyte.backend.service.scraper.ScraperService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/initialData")
class ScraperController(val recipeScraperService: ScraperService) {
    @PostMapping("/simpleRecipes")
    fun getInitialSimpleRecipes() = recipeScraperService.getSimpleRecipes()

    @PostMapping("/delishRecipes")
    fun getInitialDelishRecipes() = recipeScraperService.getDelishRecipes()

    @PostMapping("/allBrunchAndBreakfastRecipes")
    fun getInitialBrunchAndBreakfastRecipes() = recipeScraperService.getAllBrunchAndBreakfastRecipes()

    @PostMapping("/allLunchRecipes")
    fun getInitialLunchRecipes() = recipeScraperService.getAllLunchRecipes()
}