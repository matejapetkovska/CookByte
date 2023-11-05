package com.cookbyte.backend.repository

import com.cookbyte.backend.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CategoryRepository: JpaRepository<Category, Long> {
    fun findFirstByName(name: String): Category?
    @Query("SELECT c FROM Category c WHERE c.id IN :categoryIds")
    fun findAllByIds(@Param("categoryIds") categoryIds: List<Long>): Set<Category>?
}