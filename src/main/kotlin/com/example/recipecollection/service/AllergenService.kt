package com.example.recipecollection.service

import com.example.recipecollection.dto.AllergenDto
import com.example.recipecollection.dto.AllergenRequest
import com.example.recipecollection.mapper.AllergenMapper
import com.example.recipecollection.repository.AllergenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AllergenService(
    private val allergenRepository: AllergenRepository,
    private val allergenMapper: AllergenMapper,
) {
    fun list(): List<AllergenDto> = allergenRepository.findAll().map(allergenMapper::toDto)

    fun get(id: Long): AllergenDto = allergenMapper.toDto(findEntity(id))

    @Transactional
    fun create(request: AllergenRequest): AllergenDto {
        val allergen = allergenMapper.toEntity(request)
        return allergenMapper.toDto(allergenRepository.save(allergen))
    }

    @Transactional
    fun update(id: Long, request: AllergenRequest): AllergenDto {
        val allergen = findEntity(id)
        allergenMapper.updateEntity(request, allergen)
        return allergenMapper.toDto(allergenRepository.save(allergen))
    }

    @Transactional
    fun delete(id: Long) {
        val allergen = findEntity(id)
        allergen.deleted = true
        allergenRepository.save(allergen)
    }

    private fun findEntity(id: Long) = allergenRepository.findById(id)
        .orElseThrow { NoSuchElementException("Allergen $id not found") }
}
