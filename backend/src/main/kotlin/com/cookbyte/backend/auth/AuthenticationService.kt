package com.cookbyte.backend.auth

import com.cookbyte.backend.configurations.JwtService
import com.cookbyte.backend.domain.User
import com.cookbyte.backend.repository.UserRepository
import com.cookbyte.backend.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@RequiredArgsConstructor
class AuthenticationService(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String,
        image: MultipartFile?
    ): AuthenticationResponse {
        val user = if (image != null) {
            val userImage = userService.createUserImage(image)
            User(0, firstName, lastName, username, email, passwordEncoder.encode(password), userImage)
        } else {
            User(0, firstName, lastName, username, email, passwordEncoder.encode(password), "default-user-image.png")
        }
        userRepository.save(user)
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        val user = userRepository.findByUsername(request.username) ?: throw NoSuchElementException()
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return email.matches(emailRegex)
    }

    fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+]).{8,}\$\n")
        return password.matches(passwordRegex)
    }
    fun usernameAlreadyExists(username: String): Boolean {
        return userRepository.findByUsername(username) != null
    }

    fun emailAlreadyExists(email: String): Boolean {
        return userRepository.findByEmail(email) != null
    }
}