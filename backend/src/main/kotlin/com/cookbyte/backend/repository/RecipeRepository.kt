package com.cookbyte.backend.repository

import com.cookbyte.backend.domain.Recipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<Recipe, Long> {
    fun findRecipesByUserId(userId: Long): List<Recipe>?
    fun findRecipeByTitle(title: String): Recipe?

    @Query(
        "SELECT DISTINCT r FROM Recipe r " +
                "LEFT JOIN r.categories c " +
                "WHERE LOWER(r.title) LIKE %:searchTerm% " +
                "OR LOWER(c.name) LIKE %:searchTerm% "
    )
    fun searchAllByTitleAndCategories(searchTerm: String): List<Recipe>

}