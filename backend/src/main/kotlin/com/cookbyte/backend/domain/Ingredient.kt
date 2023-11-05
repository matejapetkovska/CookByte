package com.cookbyte.backend.domain

import jakarta.persistence.*

@Entity
class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val name: String,

    @ManyToOne
    var recipe: Recipe?
)