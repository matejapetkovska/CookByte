@file:Suppress("NAME_SHADOWING")

package com.cookbyte.backend.service.scraper

import com.cookbyte.backend.domain.*
import com.cookbyte.backend.repository.*
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.sun.jdi.IntegerValue
import jakarta.persistence.NonUniqueResultException
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

    private val MAX_RECIPE_LIMIT = 100

    @Scheduled(cron = "0 0 1 * * ?")
    fun getSimpleRecipes() {
        val url = "https://www.simplyrecipes.com/recipes-5090746"
        var scrapedRecipesCount = 0
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
                    try {
                        val elementUrl = element.asJsonObject.get("url").toString().replace("\"", "")

                        val doc: Document = Jsoup.connect(elementUrl).get()
                        val scriptElement = doc.select("script[type=application/ld+json]").first()

                        val jsonParser = JsonParser()
                        val jsonArray = jsonParser.parse(scriptElement?.data()).asJsonArray
                        for (element in jsonArray) {
                            if (scrapedRecipesCount >= MAX_RECIPE_LIMIT)
                                return
                            createRecipeModel(element)
                            scrapedRecipesCount++
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    fun getDelishRecipes() {
        val url = "https://www.delish.com/cooking/recipe-ideas/"
        var scrapedRecipesCount = 0
        try {
            val doc: Document = Jsoup.connect(url).get()
            val scriptElement = doc.select("script[type=application/ld+json]").first()

            val jsonParser = JsonParser()
            val jsonElement = jsonParser.parse(scriptElement?.data())

            val list = jsonElement.asJsonObject.get("itemListElement").asJsonArray
            for (element in list) {
                try {
                    val elementUrl = element.asJsonObject.get("url").toString().replace("\"", "")
                    val doc: Document = Jsoup.connect(elementUrl).get()
                    val scriptElement = doc.select("script[type=application/ld+json]").first()

                    val jsonParser = JsonParser()
                    val element = jsonParser.parse(scriptElement?.data()).asJsonObject
                    if (scrapedRecipesCount >= MAX_RECIPE_LIMIT)
                        return
                    createRecipeModel(element)
                    scrapedRecipesCount++
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    fun getAllBrunchAndBreakfastRecipes() {
        val url = "https://www.allrecipes.com/recipes/78/breakfast-and-brunch/"
        var scrapedRecipesCount = 0
        try {
            val doc: Document = Jsoup.connect(url).get()
            val scriptElement = doc.select("script[type=application/ld+json]").first()

            val jsonParser = JsonParser()
            val jsonElement = jsonParser.parse(scriptElement?.data())

            val jsonArray = jsonElement.asJsonArray.get(0)
            val itemsList = jsonArray.asJsonObject.get("itemListElement").asJsonArray
            for (element in itemsList) {
                try {
                    val elementUrl = element.asJsonObject.get("url").toString().replace("\"", "")

                    val doc: Document = Jsoup.connect(elementUrl).get()
                    val scriptElement = doc.select("script[type=application/ld+json]").first()

                    val jsonParser = JsonParser()
                    val jsonArray = jsonParser.parse(scriptElement?.data()).asJsonArray
                    for (element in jsonArray) {
                        if (scrapedRecipesCount >= MAX_RECIPE_LIMIT)
                            return
                        createRecipeModel(element)
                        scrapedRecipesCount++
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    fun getAllLunchRecipes() {
        val url = "https://www.allrecipes.com/recipes/17561/lunch/"
        var scrapedRecipesCount = 0
        try {
            val doc: Document = Jsoup.connect(url).get()
            val scriptElement = doc.select("script[type=application/ld+json]").first()

            val jsonParser = JsonParser()
            val jsonElement = jsonParser.parse(scriptElement?.data())

            val jsonArray = jsonElement.asJsonArray.get(0)
            val itemsList = jsonArray.asJsonObject.get("itemListElement").asJsonArray
            for (element in itemsList) {
                try {
                    val elementUrl = element.asJsonObject.get("url").toString().replace("\"", "")

                    val doc: Document = Jsoup.connect(elementUrl).get()
                    val scriptElement = doc.select("script[type=application/ld+json]").first()

                    val jsonParser = JsonParser()
                    val jsonArray = jsonParser.parse(scriptElement?.data()).asJsonArray
                    for (element in jsonArray) {
                        if (scrapedRecipesCount >= MAX_RECIPE_LIMIT)
                            return
                        createRecipeModel(element)
                        scrapedRecipesCount++
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createRecipeModel(element: JsonElement) {
        val title = element.asJsonObject.get("headline").toString().replace("\"", "")

        val existingRecipe = recipeRepository.findRecipeByTitle(title)
        if (existingRecipe != null) {
            println("Recipe with title '$title' already exists. Skipping...")
            return
        }

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
                .replace("\"", "")
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
            val jsonCategory = element.asString.replace("\"", "").toLowerCase()
            val existingCategory = categoryRepository.findFirstByName(jsonCategory)
            if (existingCategory == null) {
                val newCategory = Category(0, jsonCategory)
                categoryEntities.add(newCategory)
            } else {
                try {
                    categoryEntities.add(existingCategory)
                } catch (ex: NonUniqueResultException) {
                    println("Non-unique category found: $jsonCategory")
                }
            }
        }
        categoryRepository.saveAll(categoryEntities)
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

        var user = userRepository.findByFirstName(author)
        if (user == null) {
            val existingUser = userRepository.findByFirstName(author);
            if (existingUser == null) {
                user = User(0, author, null, null, null, null, null);
                userRepository.save(user);
            } else {
                try {
                    user = existingUser;
                } catch (ex: NonUniqueResultException) {
                    println("Non-unique user found: $user")
                }
            }
        }
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
            instructions.toString()
        )
        recipe.setCategories(categoryEntities)
        recipeRepository.save(recipe)
        val jsonIngredients = element.asJsonObject.get("recipeIngredient").asJsonArray
        for (element in jsonIngredients) {
            val ingredient = Ingredient(0, element.asString.replace("\"", ""), recipe)
            ingredientRepository.save(ingredient)
        }
        var review = Review(0, "", null, recipe, 0)
        if (element.asJsonObject.has("review")) {
            val reviews = element.asJsonObject.get("review").asJsonArray
            val reviewList = mutableListOf<Review>()
            for (element in reviews) {
                val reviewAuthor = element.asJsonObject.get("author").asJsonObject.get("name").toString().replace("\"", "")
                val reviewDescription = element.asJsonObject.get("reviewBody").toString().replace("\"", "")
                val reviewRating = element.asJsonObject.get("reviewRating")
                val ratingValue = reviewRating?.asJsonObject?.get("ratingValue")?.asInt ?: 0
                var user = userRepository.findByFirstName(reviewAuthor)
                if (user == null) {
                    val existingUser = userRepository.findByFirstName(reviewAuthor)
                    if (existingUser == null) {
                        user = User(0, reviewAuthor, null, null, null, null, null)
                        userRepository.save(user)
                    } else {
                        user = existingUser
                    }
                }
                review = Review(0, reviewDescription, user, recipe, ratingValue)
                reviewList.add(review)
            }
            reviewRepository.saveAll(reviewList)
        }
        reviewRepository.save(review)
    }

    fun isoDurationToTotalMinutes(isoDuration: String): Long {
        val duration = Duration.parse(isoDuration)
        return duration.toHours() * 60 + (duration.toMinutes() % 60)
    }
}