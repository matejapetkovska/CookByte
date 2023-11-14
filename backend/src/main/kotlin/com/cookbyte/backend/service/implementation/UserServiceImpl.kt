package com.cookbyte.backend.service.implementation

import com.cookbyte.backend.domain.User
import com.cookbyte.backend.repository.UserRepository
import com.cookbyte.backend.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

@Service
class UserServiceImpl(val userRepository: UserRepository): UserService {

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

    fun generateRandomName(): String {
        val name = StringBuilder()
        val allowedChars = listOf(('a'..'z'),('A'..'Z'),(0..9)).flatten().random()
        for(i in 1..8) {
            name.append(allowedChars)
        }
        return name.toString()
    }
}