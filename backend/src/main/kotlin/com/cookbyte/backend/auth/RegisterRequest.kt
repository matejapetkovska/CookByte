package com.cookbyte.backend.auth

import lombok.Data

@Data
class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
    val image: String?
) {
}