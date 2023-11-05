package com.cookbyte.backend.auth

import lombok.Data

@Data
class AuthenticationRequest(
    val username: String,
    val password: String
)