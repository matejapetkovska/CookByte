package com.cookbyte.backend.auth

import lombok.Data

@Data
class AuthenticationResponse(
    val token: String,
)