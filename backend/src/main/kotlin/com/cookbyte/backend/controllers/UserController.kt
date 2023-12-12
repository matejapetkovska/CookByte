package com.cookbyte.backend.controllers

import com.cookbyte.backend.domain.User
import com.cookbyte.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth/user")
class UserController(private val userService: UserService) {

    @GetMapping("/token")
    fun getUserFromToken(@RequestParam token: String): User? {
        return userService.getUserFromToken(token)
    }

    @PutMapping("/{id}")
    fun editUserProfile(@PathVariable id: Long, @RequestBody updatedUser: User): ResponseEntity<User> {
        val user = userService.updateUser(id, updatedUser)
        return ResponseEntity.ok(user)
    }
}