package com.cookbyte.backend.domain

import jakarta.persistence.*

@Entity
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val name: String
)