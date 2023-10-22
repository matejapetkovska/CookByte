package com.cookbyte.backend.service.scraper

import com.google.gson.JsonParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

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

                if (jsonElement.isJsonArray) {
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
                                val title = element.asJsonObject.get("headline")
                                val author = element.asJsonObject.get("author").asJsonArray.get(0).asJsonObject.get("name") //[]
                                val datePublished = element.asJsonObject.get("datePublished")
                                val description = element.asJsonObject.get("description")
                                val imageUrl = element.asJsonObject.get("image").asJsonObject.get("url").toString().replace("\"", "")
                                val cookTime = element.asJsonObject.get("cookTime")
                                val calories = element.asJsonObject.get("nutrition").asJsonObject.get("calories")
                                val carbohydrates = element.asJsonObject.get("nutrition").asJsonObject.get("carbohydrateContent")
                                val fats = element.asJsonObject.get("nutrition").asJsonObject.get("fatContent")
                                val proteins = element.asJsonObject.get("nutrition").asJsonObject.get("proteinContent")
                                val category = element.asJsonObject.get("recipeCategory").asJsonArray.get(0)
                                val ingredients = element.asJsonObject.get("recipeIngredient").asJsonArray
                                for (element in ingredients) {
                                    val ingredient = element.asString
                                }
                                val instructions = element.asJsonObject.get("recipeInstructions").asJsonArray
                                for (element in instructions) {
                                    val instruction = element.asJsonObject.get("text")
                                }
                                val reviews = element.asJsonObject.get("review").asJsonArray
                                for (element in reviews) {
                                    val author = element.asJsonObject.get("author").asJsonObject.get("name")
                                    val review = element.asJsonObject.get("reviewBody")
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

}