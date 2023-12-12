package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.domain.Category
import com.cookbyte.backend.repository.CategoryRepository
import com.cookbyte.backend.service.CategoryService
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(val categoryRepository: CategoryRepository): CategoryService {
    override fun findAll(): List<Category> {
        return categoryRepository.findAll()
    }

    override fun findFirstByName(name: String): Category? {
       return categoryRepository.findFirstByName(name)
    }

    override fun findAllByIds(categoryIds: List<Long>): Set<Category>? {
        return categoryRepository.findAllByIds(categoryIds)
    }
}