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
}