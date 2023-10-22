@file:Suppress("NAME_SHADOWING")

package com.cookbyte.backend.service.scraper

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class ScraperService {
    @Scheduled(fixedRate = 10000)
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
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @Scheduled(fixedRate = 10000)
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
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createRecipeModel(element: JsonElement) {
        val title = element.asJsonObject.get("headline").toString().replace("\"", "")
        var author = ""
        if (element.asJsonObject.get("author").isJsonArray) {
            author = element.asJsonObject.get("author").asJsonArray.get(0).asJsonObject.get("name").toString().replace("\"", "")
        } else if (element.asJsonObject.get("author").isJsonObject) {
            author = element.asJsonObject.get("author").asJsonObject.get("name").toString().replace("\"", "")
        }
        val datePublished = element.asJsonObject.get("datePublished").toString().replace("\"", "")
        val description = element.asJsonObject.get("description").toString().replace("\"", "")
        var imageUrl = ""
        if (element.asJsonObject.get("image").isJsonArray) {
            imageUrl = element.asJsonObject.get("image").asJsonArray.get(0).asJsonObject.get("url").toString().replace("\"", "") //!!!
        } else if (element.asJsonObject.get("image").isJsonObject) {
            imageUrl = element.asJsonObject.get("image").asJsonObject.get("url").toString().replace("\"", "")
        }
        val cookTime = isoDurationToTotalMinutes(element.asJsonObject.get("totalTime").toString().replace("\"", ""))
        val nutrition = element.asJsonObject.get("nutrition")?.asJsonObject
        val calories = nutrition?.get("calories")?.toString()?.replace("\"", "") ?: ""
        val carbohydrates = nutrition?.get("carbohydrateContent")?.toString()?.replace("\"", "") ?: ""
        val fats = nutrition?.get("fatContent")?.toString()?.replace("\"", "") ?: ""
        val proteins = nutrition?.get("proteinContent")?.toString()?.replace("\"", "") ?: ""
        val categories = element.asJsonObject.get("recipeCategory").asJsonArray
        for (element in categories) {
            val category = element.asString.replace("\"", "")
            println("Category: $category")
        }
        val ingredients = element.asJsonObject.get("recipeIngredient").asJsonArray
        for (element in ingredients) {
            val ingredient = element.asString.replace("\"", "")
            println("Ingredient: $ingredient")
        }
        val instructions = element.asJsonObject.get("recipeInstructions").asJsonArray
        for (element in instructions) {
            var instruction = ""
            if (element.asJsonObject.has("itemListElement")) {
                val elementList = element.asJsonObject.get("itemListElement").asJsonArray
                for (item in elementList) {
                    instruction = item.asJsonObject.get("text").toString().replace("\"", "")
                    println("Instruction in IF: $instruction")
                }
            } else {
                instruction = element.asJsonObject.get("text").toString().replace("\"", "")
                println("Instruction in ELSE: $instruction")
            }
        }
        if (element.asJsonObject.has("review")) {
            val reviews = element.asJsonObject.get("review").asJsonArray
            for (element in reviews) {
                val author = element.asJsonObject.get("author").asJsonObject.get("name")
                val review = element.asJsonObject.get("reviewBody")
                println("Review Author: $author")
                println("Review Body: $review")
            }
        }

        println("Title: $title")
        println("Author: $author")
        println("Date Published: $datePublished")
        println("Description: $description")
        println("Image URL: $imageUrl")
        println("Cook Time: $cookTime")
        println("Calories: $calories")
        println("Carbohydrates: $carbohydrates")
        println("Fats: $fats")
        println("Proteins: $proteins")
    }

    fun isoDurationToTotalMinutes(isoDuration: String): Long {
        val duration = Duration.parse(isoDuration)
        return duration.toHours() * 60 + (duration.toMinutes() % 60)
    }
}