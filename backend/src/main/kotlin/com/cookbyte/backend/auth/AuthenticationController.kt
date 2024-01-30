package com.cookbyte.backend.auth

import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import com.cookbyte.backend.domain.exceptions.*

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
class AuthenticationController(private val authenticationService: AuthenticationService) {

    data class ErrorResponse(val message: String)

    @PostMapping("/register")
    fun register(
        @RequestParam firstName: String,
        @RequestParam lastName: String,
        @RequestParam username: String,
        @RequestParam email: String,
        @RequestParam password: String,
        @RequestParam(required = false) image: MultipartFile?
    ): ResponseEntity<out Any> {
        try {
            if (firstName.isBlank() || lastName.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
                val errorMessage = "Please fill in all the required fields."
                println(errorMessage)
                return ResponseEntity.badRequest().body(ErrorResponse(errorMessage))
            }

            if (!authenticationService.isValidEmail(email)) {
                val errorMessage = "Please enter a valid email address."
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
            if (image==null) {
                return ResponseEntity.ok(
                    authenticationService.registerWithoutImage(
                        firstName,
                        lastName,
                        username,
                        email,
                        password
                    )
                )
            }
            else {
                return ResponseEntity.ok(
                    authenticationService.register(
                        firstName,
                        lastName,
                        username,
                        email,
                        password,
                        image
                    )
                )
            }
        } catch (exception: Exception) {
            return ResponseEntity.badRequest().body(ErrorResponse("An error occurred during registration."))
        }
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthenticationRequest): ResponseEntity<out Any> {
        if (request.username.isEmpty() || request.password.isEmpty()) {
            val errorResponse = ErrorResponse("Please fill in all required fields.")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }
        return try {
            ResponseEntity.ok(authenticationService.authenticate(request))
        } catch (exception: UsernameNotFoundException) {
            val errorResponse = ErrorResponse("Username not found.")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
        } catch (exception: InvalidPasswordException) {
            val errorResponse = ErrorResponse("Invalid password.")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
        } catch (exception: InvalidArgumentsException) {
            val errorResponse = ErrorResponse("Invalid arguments.")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }
    }
}