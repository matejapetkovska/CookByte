package com.cookbyte.backend.domain

import jakarta.persistence.*

@Entity
@Table(name = "cookbyte_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val firstName: String,
    val lastName: String?,

    @Column(unique = true)
    val username: String?,

    @Column(unique = true)
    val email: String?,

    val password: String?,
    val image: String?
)