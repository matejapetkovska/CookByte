package com.cookbyte.backend.domain

import jakarta.persistence.*

@Entity
class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(columnDefinition = "TEXT")
    val description: String?,

    @ManyToOne
    val user: User?,

    @ManyToOne
    var recipe: Recipe?,

    val ratingValue: Int
)