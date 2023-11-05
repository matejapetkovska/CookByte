package com.cookbyte.backend.repository

import com.cookbyte.backend.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByFirstName(firstName: String): User?
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
}