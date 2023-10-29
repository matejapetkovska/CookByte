package com.cookbyte.backend.service

import com.cookbyte.backend.domain.User

interface UserService {
    fun findByFirstName(firstName: String): User?
    fun findByUsername(username: String): User?
    fun createUser(firstName: String, lastName: String, username: String, email: String, password: String, image: String): User
}