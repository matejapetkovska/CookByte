package com.cookbyte.backend.auth

import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
class AuthenticationController(private val authenticationService: AuthenticationService) {

    data class ErrorResponse(val message: String)
    @PostMapping("/register")
    fun register(@RequestParam firstName: String,
                 @RequestParam lastName: String,
                 @RequestParam username: String,
                 @RequestParam email: String,
                 @RequestParam password: String,
                 @RequestParam(required = false) image: MultipartFile): ResponseEntity<out Any> {
        try {

            //TODO: Check setting default image

            if (!authenticationService.isValidEmail(email)) {
                val errorMessage = "Please enter a valid email address."
                return ResponseEntity.badRequest().body(ErrorResponse(errorMessage))
            }

            if (!authenticationService.isValidPassword(password)) {
                val errorMessage = "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and a minimum length of 8 characters."
                return ResponseEntity.badRequest().body(ErrorResponse(errorMessage))
            }

            if (authenticationService.usernameAlreadyExists(username)) {
                val errorMessage = "Username already exists."
                return ResponseEntity.badRequest().body(ErrorResponse(errorMessage))
            }

            if (authenticationService.emailAlreadyExists(email)) {
                val errorMessage = "Email already exists."
                return ResponseEntity.badRequest().body(ErrorResponse(errorMessage))
            }
            return ResponseEntity.ok(authenticationService.register(firstName,lastName, username, email, password, image))
        }
        catch (exception: Exception) {
            return ResponseEntity.badRequest().body(ErrorResponse("Invalid arguments"))
        }
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        //TODO: Add checks here
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }
}