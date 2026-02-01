package com.example.recipecollection.service

import com.example.recipecollection.dto.MaterialCategoryDto
import com.example.recipecollection.dto.MaterialCategoryRequest
import com.example.recipecollection.dto.PageResponse
import com.example.recipecollection.dto.PageableRequest
import com.example.recipecollection.mapper.MaterialCategoryMapper
import com.example.recipecollection.repository.MaterialCategoryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MaterialCategoryService(
    private val materialCategoryRepository: MaterialCategoryRepository,
    private val materialCategoryMapper: MaterialCategoryMapper,
) {
    @Transactional
    fun list(showDeleted: Boolean): List<MaterialCategoryDto> =
        materialCategoryRepository
            .findAll()
            .filter { showDeleted || !it.deleted }
            .map(materialCategoryMapper::toDto)

    @Transactional
    fun get(id: Long): MaterialCategoryDto = materialCategoryMapper.toDto(findEntity(id))

    @Transactional
    fun listPageable(
        showDeleted: Boolean,
        pageable: PageableRequest,
    ): PageResponse<MaterialCategoryDto> {
        val page =
            materialCategoryRepository
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
                ).map(materialCategoryMapper::toDto)
        return PageSupport.toPage(page)
    }

    @Transactional
    fun create(request: MaterialCategoryRequest): MaterialCategoryDto {
        val entity = materialCategoryMapper.toEntity(request)
        return materialCategoryMapper.toDto(materialCategoryRepository.save(entity))
    }

    @Transactional
    fun update(
        id: Long,
        request: MaterialCategoryRequest,
    ): MaterialCategoryDto {
        val entity = findEntity(id)
        materialCategoryMapper.updateEntity(request, entity)
        return materialCategoryMapper.toDto(materialCategoryRepository.save(entity))
    }

    @Transactional
    fun delete(id: Long) {
        val entity = findEntity(id)
        entity.deleted = true
        materialCategoryRepository.save(entity)
    }

    private fun findEntity(id: Long) =
        materialCategoryRepository
            .findById(id)
            .orElseThrow { NoSuchElementException("Material category $id not found") }
            .also { if (it.deleted) throw NoSuchElementException("Material category $id not found") }
}
