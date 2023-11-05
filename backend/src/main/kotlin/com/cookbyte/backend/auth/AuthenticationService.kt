package com.cookbyte.backend.auth

import com.cookbyte.backend.configurations.JwtService
import com.cookbyte.backend.domain.User
import com.cookbyte.backend.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class AuthenticationService(private final val userRepository: UserRepository,
    private final val passwordEncoder: PasswordEncoder,
    private final val jwtService: JwtService,
    private final val authenticationManager: AuthenticationManager) {

    fun register(request: RegisterRequest): AuthenticationResponse{
        val user = User(
            id = 0,
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            username = request.username,
            password = passwordEncoder.encode(request.password),
            image = "default_profile_picture.jpg"
        )
        userRepository.save(user)
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse{
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        val user = userRepository.findByUsername(request.username) ?: throw NoSuchElementException()
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }
}