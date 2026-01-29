package com.example.recipecollection.service

import com.example.recipecollection.domain.UserAllergen
import com.example.recipecollection.dto.UserAllergenDto
import com.example.recipecollection.dto.UserAllergenRequest
import com.example.recipecollection.mapper.UserAllergenMapper
import com.example.recipecollection.repository.AllergenRepository
import com.example.recipecollection.repository.ApplicationUserRepository
import com.example.recipecollection.repository.UserAllergenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAllergenService(
    private val userAllergenRepository: UserAllergenRepository,
    private val allergenRepository: AllergenRepository,
    private val userRepository: ApplicationUserRepository,
    private val userAllergenMapper: UserAllergenMapper,
) {
    fun list(): List<UserAllergenDto> = userAllergenRepository.findAll().map(userAllergenMapper::toDto)

    fun get(id: Long): UserAllergenDto = userAllergenMapper.toDto(findEntity(id))

    @Transactional
    fun create(request: UserAllergenRequest): UserAllergenDto {
        val allergen = allergenRepository.findById(request.allergenId)
            .orElseThrow { NoSuchElementException("Allergen ${request.allergenId} not found") }
        val user = userRepository.findById(request.userId)
            .orElseThrow { NoSuchElementException("User ${request.userId} not found") }
        val entity = UserAllergen(allergen = allergen, user = user)
        return userAllergenMapper.toDto(userAllergenRepository.save(entity))
    }

    @Transactional
    fun update(id: Long, request: UserAllergenRequest): UserAllergenDto {
        val entity = findEntity(id)
        val allergen = allergenRepository.findById(request.allergenId)
            .orElseThrow { NoSuchElementException("Allergen ${request.allergenId} not found") }
        val user = userRepository.findById(request.userId)
            .orElseThrow { NoSuchElementException("User ${request.userId} not found") }
        entity.allergen = allergen
        entity.user = user
        return userAllergenMapper.toDto(userAllergenRepository.save(entity))
    }

    @Transactional
    fun delete(id: Long) {
        val entity = findEntity(id)
        entity.deleted = true
        userAllergenRepository.save(entity)
    }

    private fun findEntity(id: Long) = userAllergenRepository.findById(id)
        .orElseThrow { NoSuchElementException("User allergen $id not found") }
}
