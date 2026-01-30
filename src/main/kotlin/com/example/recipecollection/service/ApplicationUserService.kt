package com.example.recipecollection.service

import com.example.recipecollection.dto.ApplicationUserDto
import com.example.recipecollection.dto.ApplicationUserRequest
import com.example.recipecollection.domain.UserRole
import com.example.recipecollection.mapper.ApplicationUserMapper
import com.example.recipecollection.repository.ApplicationUserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.crypto.password.PasswordEncoder

@Service
class ApplicationUserService(
    private val userRepository: ApplicationUserRepository,
    private val userMapper: ApplicationUserMapper,
    private val passwordEncoder: PasswordEncoder,
) {
    private val defaultRoles = setOf(UserRole.RECIPE_READER, UserRole.RECIPE_WRITER)

    fun list(): List<ApplicationUserDto> = userRepository.findAll().map(userMapper::toDto)

    fun get(id: Long): ApplicationUserDto = userMapper.toDto(findEntity(id))

    @Transactional
    fun create(request: ApplicationUserRequest): ApplicationUserDto {
        val user = userMapper.toEntity(request)
        val rawPassword = request.password?.trim()
        require(!rawPassword.isNullOrEmpty()) { "Password is required" }
        user.password = passwordEncoder.encode(rawPassword)!!
        if (user.roles.isEmpty()) {
            user.roles = defaultRoles.toMutableSet()
        }
        return userMapper.toDto(userRepository.save(user))
    }

    @Transactional
    fun update(id: Long, request: ApplicationUserRequest): ApplicationUserDto {
        val user = findEntity(id)
        val existingRoles = user.roles.toMutableSet()
        val existingPassword = user.password
        userMapper.updateEntity(request, user)
        if (request.roles.isEmpty()) {
            user.roles = existingRoles
        }
        val rawPassword = request.password?.trim()
        user.password = if (rawPassword.isNullOrEmpty()) {
            existingPassword
        } else {
            passwordEncoder.encode(rawPassword)!!
        }
        return userMapper.toDto(userRepository.save(user))
    }

    @Transactional
    fun delete(id: Long) {
        val user = findEntity(id)
        user.deleted = true
        userRepository.save(user)
    }

    private fun findEntity(id: Long) = userRepository.findById(id)
        .orElseThrow { NoSuchElementException("User $id not found") }
}
