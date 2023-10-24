package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.domain.Category
import com.cookbyte.backend.repository.CategoryRepository
import com.cookbyte.backend.service.CategoryService
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(val categoryRepository: CategoryRepository): CategoryService {
    override fun findFirstByName(name: String): Category? {
       return categoryRepository.findFirstByName(name)
    }
}