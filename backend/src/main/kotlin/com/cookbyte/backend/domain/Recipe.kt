package com.cookbyte.backend.domain

import jakarta.persistence.*

@Entity
class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val title: String,

    @ManyToOne
    val user: User?,

    val datePublished: String,

    @Column(columnDefinition = "TEXT")
    val description: String,

    @Column(columnDefinition = "TEXT")
    val imageUrl: String,

    val cookTime: Long,
    val calories: String,
    val carbohydrates: String,
    val fats: String,
    val proteins: String,

    @Column(columnDefinition = "TEXT")
    val instructions: String,

    @ManyToMany
    @JoinTable(
        name = "recipe_categories",
        joinColumns = [JoinColumn(name = "recipe_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var categories: Set<Category> = emptySet()
) {
    fun setCategories(newCategories: MutableList<Category>) {
        this.categories = newCategories.toSet()
    }
}