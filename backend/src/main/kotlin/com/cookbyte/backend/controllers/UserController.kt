package com.cookbyte.backend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth/demo")
class UserController {

    @GetMapping
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello")
    }
}