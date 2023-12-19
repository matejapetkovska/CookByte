package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.configurations.JwtService
import com.cookbyte.backend.domain.User
import com.cookbyte.backend.repository.UserRepository
import com.cookbyte.backend.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class UserServiceImpl(val userRepository: UserRepository, val jwtService: JwtService): UserService {

    @Value("\${upload.directory}")
    private lateinit var uploadDirectory: String

    override fun findByFirstName(firstName: String): User? {
        return userRepository.findByFirstName(firstName)
    }

    override fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    override fun createUserImage(image: MultipartFile): String? {
        val imagePath = generateRandomName() + ".jpg"
        val imagePathWithDirectory = Paths.get(uploadDirectory, imagePath)
        Files.copy(image.inputStream, imagePathWithDirectory)
        return imagePath
    }

    override fun getUserFromToken(token: String): User? {
        return userRepository.findByUsername(jwtService.extractUsername(token))
    }

    override fun updateUser(userId: Long, updatedUser: User): User? {
        val existingUser = userRepository.findById(userId)
        if (existingUser.isPresent) {
            val userToUpdate = existingUser.get()
            userToUpdate.firstName = updatedUser.firstName
            userToUpdate.lastName = updatedUser.lastName
            userToUpdate.username = updatedUser.username
            return userRepository.save(userToUpdate)
        }
        return null
    }

    fun generateRandomName(): String {
        val sb = StringBuilder()
        for (i in 0..8) {
            val rand = listOf(('a'..'z'), ('A'..'Z')).flatten().random()
            sb.append(rand)
        }
        return sb.toString()
    }
}