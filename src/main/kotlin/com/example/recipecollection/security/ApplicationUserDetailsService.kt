package com.example.recipecollection.security

import com.example.recipecollection.repository.ApplicationUserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class ApplicationUserDetailsService(
    private val userRepository: ApplicationUserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User $username not found")
        val authorities = user.roles.map { role -> SimpleGrantedAuthority("ROLE_${role.name}") }
        println("user: $user")
        return User(user.username, user.password, authorities)
    }
}
