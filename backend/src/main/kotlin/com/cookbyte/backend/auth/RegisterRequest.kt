package com.cookbyte.backend.auth

import lombok.Data
import org.springframework.web.multipart.MultipartFile

@Data
class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
    val image: MultipartFile?
)
//TODO: Turn this into form-data