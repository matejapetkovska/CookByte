package com.cookbyte.backend.service

import com.cookbyte.backend.domain.User

interface UserService {
    fun findByFirstName(firstName: String): User?
}