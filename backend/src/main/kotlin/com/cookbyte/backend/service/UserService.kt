package com.cookbyte.backend.service

import com.cookbyte.backend.domain.User
import org.springframework.web.multipart.MultipartFile

interface UserService {
    fun findByFirstName(firstName: String): User?
    fun findByUsername(username: String): User?
    fun createUserImage(image: MultipartFile): String?
}