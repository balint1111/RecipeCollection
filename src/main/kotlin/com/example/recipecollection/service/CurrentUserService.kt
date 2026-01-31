package com.example.recipecollection.service

import com.example.recipecollection.domain.ApplicationUser
import com.example.recipecollection.repository.ApplicationUserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CurrentUserService(
    private val userRepository: ApplicationUserRepository,
) {
    fun requireCurrentUser(): ApplicationUser {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalStateException("No authenticated user")
        val username = authentication.name
        return userRepository.findByUsername(username)
            ?: throw NoSuchElementException("User $username not found")
    }
}
