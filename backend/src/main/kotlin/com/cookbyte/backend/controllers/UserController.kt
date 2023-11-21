package com.cookbyte.backend.controllers

import com.cookbyte.backend.domain.User
import com.cookbyte.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {

    @GetMapping("/token")
    fun getUserFromToken(@RequestParam token: String): User? {
        return userService.getUserFromToken(token)
    }
}