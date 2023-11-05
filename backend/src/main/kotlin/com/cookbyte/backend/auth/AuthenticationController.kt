package com.cookbyte.backend.auth

import com.cookbyte.backend.auth.AuthenticationRequest
import com.cookbyte.backend.auth.AuthenticationResponse
import com.cookbyte.backend.auth.RegisterRequest
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
class AuthenticationController(private final val authenticationService: AuthenticationService) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.register(request))
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }
}