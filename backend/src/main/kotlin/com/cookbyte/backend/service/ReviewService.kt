package com.cookbyte.backend.service

import com.cookbyte.backend.domain.Review

interface ReviewService {
    fun findAll(): List<Review>?
}