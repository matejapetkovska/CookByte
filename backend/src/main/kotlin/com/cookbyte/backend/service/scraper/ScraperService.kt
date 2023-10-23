@file:Suppress("NAME_SHADOWING")

package com.cookbyte.backend.service.scraper

import com.cookbyte.backend.domain.*
import com.cookbyte.backend.repository.*
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class ScraperService(
    val recipeRepository: RecipeRepository,
    val ingredientRepository: IngredientRepository,
    val userRepository: UserRepository,
    val reviewRepository: ReviewRepository,
    val categoryRepository: CategoryRepository
) {

    private val MAX_RECIPE_LIMIT = 50
    var scrapedSimpleRecipesCount = 0
    var scrapedDelishRecipesCount = 0

    @Scheduled(cron = "0 0 1 * * ?")
    fun getSimpleRecipes() {
        val url = "https://www.simplyrecipes.com/recipes-5090746"
        try {
            val doc: Document = Jsoup.connect(url).get()
            val scriptElement = doc.select("script[type=application/ld+json]").first()

            val jsonParser = JsonParser()
            val jsonElement = jsonParser.parse(scriptElement?.data())

            val jsonArray = jsonElement.asJsonArray
            for (element in jsonArray) {
                val itemListElement = element.asJsonObject.get("itemListElement")
                val list = itemListElement.asJsonArray
                for (element in list) {
                    val elementUrl = element.asJsonObject.get("url").toString().replace("\"", "")

                    val doc: Document = Jsoup.connect(elementUrl).get()
                    val scriptElement = doc.select("script[type=application/ld+json]").first()

                    val jsonParser = JsonParser()
                    val jsonArray = jsonParser.parse(scriptElement?.data()).asJsonArray
                    for (element in jsonArray) {
                        createRecipeModel(element)
                        scrapedSimpleRecipesCount++
                    }
                }
            }
            if (scrapedSimpleRecipesCount >= MAX_RECIPE_LIMIT) {
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    fun getDelishRecipes() {
        val url = "https://www.delish.com/cooking/recipe-ideas/"
        try {
            val doc: Document = Jsoup.connect(url).get()
            val scriptElement = doc.select("script[type=application/ld+json]").first()

            val jsonParser = JsonParser()
            val jsonElement = jsonParser.parse(scriptElement?.data())

            val list = jsonElement.asJsonObject.get("itemListElement").asJsonArray
            for (element in list) {
                val elementUrl = element.asJsonObject.get("url").toString().replace("\"", "")
                val doc: Document = Jsoup.connect(elementUrl).get()
                val scriptElement = doc.select("script[type=application/ld+json]").first()

                val jsonParser = JsonParser()
                val element = jsonParser.parse(scriptElement?.data()).asJsonObject
                createRecipeModel(element)
                scrapedDelishRecipesCount++
            }
            if (scrapedDelishRecipesCount >= MAX_RECIPE_LIMIT) {
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createRecipeModel(element: JsonElement) {
        val title = element.asJsonObject.get("headline").toString().replace("\"", "")
        var author = ""
        if (element.asJsonObject.get("author").isJsonArray) {
            author = element.asJsonObject.get("author").asJsonArray.get(0).asJsonObject.get("name").toString()
                .replace("\"", "")
        } else if (element.asJsonObject.get("author").isJsonObject) {
            author = element.asJsonObject.get("author").asJsonObject.get("name").toString().replace("\"", "")
        }
        val datePublished = element.asJsonObject.get("datePublished").toString().replace("\"", "")
        val description = element.asJsonObject.get("description").toString().replace("\"", "")
        var imageUrl = ""
        if (element.asJsonObject.get("image").isJsonArray) {
            imageUrl = element.asJsonObject.get("image").asJsonArray.get(0).asJsonObject.get("url").toString()
                .replace("\"", "") //!!!
        } else if (element.asJsonObject.get("image").isJsonObject) {
            imageUrl = element.asJsonObject.get("image").asJsonObject.get("url").toString().replace("\"", "")
        }
        val cookTime = isoDurationToTotalMinutes(element.asJsonObject.get("totalTime").toString().replace("\"", ""))
        val nutrition = element.asJsonObject.get("nutrition")?.asJsonObject
        val calories = nutrition?.get("calories")?.toString()?.replace("\"", "") ?: ""
        val carbohydrates = nutrition?.get("carbohydrateContent")?.toString()?.replace("\"", "") ?: ""
        val fats = nutrition?.get("fatContent")?.toString()?.replace("\"", "") ?: ""
        val proteins = nutrition?.get("proteinContent")?.toString()?.replace("\"", "") ?: ""
        val jsonCategories = element.asJsonObject.get("recipeCategory").asJsonArray
        val categoryEntities = mutableListOf<Category>()
        for (element in jsonCategories) {
            val jsonCategory = element.asString.replace("\"", "")
            val category = Category(0, jsonCategory)
            categoryEntities.add(category)
        }
        //TODO: Check duplicate categories
        categoryRepository.saveAll(categoryEntities)
        val jsonIngredients = element.asJsonObject.get("recipeIngredient").asJsonArray
        var ingredient = Ingredient(0, "")
        for (element in jsonIngredients) {
            ingredient = Ingredient(0, element.asString.replace("\"", ""))
            ingredientRepository.save(ingredient)
        }
        val jsonInstructions = element.asJsonObject.get("recipeInstructions").asJsonArray
        val instructions = StringBuilder()
        for (element in jsonInstructions) {
            var instruction = ""
            if (element.asJsonObject.has("itemListElement")) {
                val elementList = element.asJsonObject.get("itemListElement").asJsonArray
                for (item in elementList) {
                    instruction = item.asJsonObject.get("text").toString().replace("\"", "")
                    instructions.append(instruction)
                }
            } else {
                instruction = element.asJsonObject.get("text").toString().replace("\"", "")
                instructions.append(instruction)
            }
        }
        val user = User(0, author, null, null, null, null, null)
        userRepository.save(user)
        val recipe = Recipe(
            0,
            title,
            user,
            datePublished,
            description,
            imageUrl,
            cookTime,
            calories,
            carbohydrates,
            fats,
            proteins,
            ingredient,
            instructions.toString()
        )
        recipe.setCategories(categoryEntities)
        recipeRepository.save(recipe)
        if (element.asJsonObject.has("review")) {
            val reviews = element.asJsonObject.get("review").asJsonArray
            var review = Review(0, null, null, null)
            for (element in reviews) {
                val reviewAuthor =
                    element.asJsonObject.get("author").asJsonObject.get("name").toString().replace("\"", "")
                val reviewDescription = element.asJsonObject.get("reviewBody").toString().replace("\"", "")
                val user = User(0, reviewAuthor, null, null, null, null, null)
                userRepository.save(user)
                review = Review(0, reviewDescription, user, recipe)
            }
            reviewRepository.save(review)
        }
    }

    fun isoDurationToTotalMinutes(isoDuration: String): Long {
        val duration = Duration.parse(isoDuration)
        return duration.toHours() * 60 + (duration.toMinutes() % 60)
    }
}