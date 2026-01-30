package com.example.recipecollection.service

import com.example.recipecollection.dto.ApplicationUserDto
import com.example.recipecollection.dto.ApplicationUserRequest
import com.example.recipecollection.mapper.ApplicationUserMapper
import com.example.recipecollection.repository.ApplicationUserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ApplicationUserService(
    private val userRepository: ApplicationUserRepository,
    private val userMapper: ApplicationUserMapper,
) {
    fun list(): List<ApplicationUserDto> = userRepository.findAll().map(userMapper::toDto)

    fun get(id: Long): ApplicationUserDto = userMapper.toDto(findEntity(id))

    @Transactional
    fun create(request: ApplicationUserRequest): ApplicationUserDto {
        val user = userMapper.toEntity(request)
        return userMapper.toDto(userRepository.save(user))
    }

    @Transactional
    fun update(id: Long, request: ApplicationUserRequest): ApplicationUserDto {
        val user = findEntity(id)
        userMapper.updateEntity(request, user)
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
