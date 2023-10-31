package com.cookbyte.backend.repository

import com.cookbyte.backend.domain.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, Long> {
    fun findFirstByName(name: String): Category?
    fun findAllById(ids: List<Long>): Set<Category>?
}