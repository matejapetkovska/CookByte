package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.domain.User
import com.cookbyte.backend.repository.UserRepository
import com.cookbyte.backend.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(val userRepository: UserRepository): UserService {
    override fun findByFirstName(firstName: String): User? {
        return userRepository.findByFirstName(firstName)
    }

    override fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    override fun createUser(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String,
        image: String
    ): User {
        return userRepository.save(User(0, firstName, lastName, username, email, password, image))
    }
}