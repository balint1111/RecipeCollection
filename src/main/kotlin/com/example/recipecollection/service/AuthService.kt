package com.example.recipecollection.service

import com.example.recipecollection.domain.ApplicationUser
import com.example.recipecollection.domain.UserRole
import com.example.recipecollection.dto.AuthLoginRequest
import com.example.recipecollection.dto.AuthRegisterRequest
import com.example.recipecollection.dto.AuthResponse
import com.example.recipecollection.mapper.ApplicationUserMapper
import com.example.recipecollection.repository.ApplicationUserRepository
import com.example.recipecollection.security.JwtService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: ApplicationUserRepository,
    private val userMapper: ApplicationUserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
) {
    private val defaultRoles = setOf(UserRole.RECIPE_READER, UserRole.RECIPE_WRITER)

    @Transactional
    fun register(request: AuthRegisterRequest): AuthResponse {
        require(userRepository.findByUsername(request.username) == null) { "Username already exists" }
        val roles = if (request.roles.isEmpty()) defaultRoles else request.roles
        val user = ApplicationUser(
            username = request.username,
            name = request.name,
            settlement = request.settlement,
            country = request.country,
            password = passwordEncoder.encode(request.password)!!,
            roles = roles.toMutableSet(),
        )
        val saved = userRepository.save(user)
        val token = jwtService.generateToken(saved.username, saved.roles)
        return AuthResponse(token = token, user = userMapper.toDto(saved))
    }

    fun login(request: AuthLoginRequest): AuthResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw IllegalArgumentException("Invalid username or password")
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid username or password")
        }
        val token = jwtService.generateToken(user.username, user.roles)
        return AuthResponse(token = token, user = userMapper.toDto(user))
    }
}
