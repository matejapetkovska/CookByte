package com.cookbyte.backend.service

import com.cookbyte.backend.domain.Category

interface CategoryService {
    fun findFirstByName(name: String): Category?
    fun findAllById(ids: List<Long>): Set<Category>?
}