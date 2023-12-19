package com.cookbyte.backend.domain

import com.fasterxml.jackson.annotation.JsonSetter
import jakarta.persistence.*

@Entity
class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    var title: String,

    @ManyToOne
    val user: User?,

    val datePublished: String,

    @Column(columnDefinition = "TEXT")
    var description: String,

    @Column(columnDefinition = "TEXT")
    var imageUrl: String,

    var cookTime: Long,
    var calories: String,
    var carbohydrates: String,
    var fats: String,
    var proteins: String,

    @Column(columnDefinition = "TEXT")
    var instructions: String,

    @ManyToMany
    @JoinTable(
        name = "recipe_categories",
        joinColumns = [JoinColumn(name = "recipe_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var categories: Set<Category> = emptySet()
) {
    @JsonSetter("categories")
    fun setCategories(newCategories: List<Category>) {
        this.categories = newCategories.toSet()
    }
}