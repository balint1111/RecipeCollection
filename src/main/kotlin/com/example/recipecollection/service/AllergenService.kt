package com.example.recipecollection.service

import com.example.recipecollection.domain.UserAllergen
import com.example.recipecollection.dto.AllergenDto
import com.example.recipecollection.dto.AllergenRequest
import com.example.recipecollection.dto.PageResponse
import com.example.recipecollection.dto.PageableRequest
import com.example.recipecollection.mapper.AllergenMapper
import com.example.recipecollection.repository.AllergenRepository
import com.example.recipecollection.repository.UserAllergenRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AllergenService(
    private val allergenRepository: AllergenRepository,
    private val allergenMapper: AllergenMapper,
    private val userAllergenRepository: UserAllergenRepository,
    private val currentUserService: CurrentUserService,
) {
    fun list(showDeleted: Boolean): List<AllergenDto> =
        allergenRepository.findAll().filter { showDeleted || !it.deleted }.map(allergenMapper::toDto)

    fun get(id: Long): AllergenDto = allergenMapper.toDto(findEntity(id))

    fun listPageable(
        showDeleted: Boolean,
        pageable: PageableRequest,
    ): PageResponse<AllergenDto> {
        val page =
            allergenRepository
                .findAllByNameContainsIgnoreCaseAndDeleted(
                    pageable.filter,
                    showDeleted,
                    PageRequest.of(
                        (pageable.page - 1).coerceAtLeast(0),
                        pageable.pageSize.coerceAtLeast(1),
                        if (pageable.sortDirection != null && pageable.sortField != null) {
                            Sort.by(pageable.sortDirection, pageable.sortField)
                        } else {
                            Sort.unsorted()
                        },
                    ),
                ).map(allergenMapper::toDto)
        return PageSupport.toPage(page)
    }

    @Transactional
    fun addAllergen(allergenId: Long) {
        val user = currentUserService.requireCurrentUser()
        val allergen = findEntity(allergenId)
        val existing = userAllergenRepository.findByUserIdAndAllergenId(user.id!!, allergen.id!!)
        if (existing != null) {
            if (existing.deleted) {
                existing.deleted = false
                userAllergenRepository.save(existing)
            }
            return
        }
        userAllergenRepository.save(UserAllergen(allergen = allergen, user = user))
    }

    @Transactional
    fun deleteAllergen(allergenId: Long) {
        val user = currentUserService.requireCurrentUser()
        val existing =
            userAllergenRepository.findByUserIdAndAllergenId(user.id!!, allergenId)
                ?: throw NoSuchElementException("Allergen $allergenId not found for user")
        existing.deleted = true
        userAllergenRepository.save(existing)
    }

    @Transactional
    fun create(request: AllergenRequest): AllergenDto {
        val allergen = allergenMapper.toEntity(request)
        return allergenMapper.toDto(allergenRepository.save(allergen))
    }

    @Transactional
    fun update(
        id: Long,
        request: AllergenRequest,
    ): AllergenDto {
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

    private fun findEntity(id: Long) =
        allergenRepository
            .findById(id)
            .orElseThrow { NoSuchElementException("Allergen $id not found") }
            .also { if (it.deleted) throw NoSuchElementException("Allergen $id not found") }
}
