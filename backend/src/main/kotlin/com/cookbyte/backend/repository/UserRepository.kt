package com.cookbyte.backend.repository

import com.cookbyte.backend.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long>