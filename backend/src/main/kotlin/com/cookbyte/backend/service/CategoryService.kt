package com.cookbyte.backend.service

import com.cookbyte.backend.domain.Category

interface CategoryService {
    fun findFirstByName(name: String): Category?
    fun findAllByIds(categoryIds: List<Long>): Set<Category>?
}