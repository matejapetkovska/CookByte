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

    fun register(request: RegisterRequest): AuthenticationResponse {
        var user: User
        if (request.image != null) {
            val userImage = userService.createUserImage(request.image)
            user = User(
                id = 0,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                username = request.username,
                password = passwordEncoder.encode(request.password),
                image = userImage
            )
        } else {
            user = User(
                id = 0,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                username = request.username,
                password = passwordEncoder.encode(request.password),
                image = "default-user-image.png"
            )
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
}