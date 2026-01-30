package com.example.recipecollection.service

import com.example.recipecollection.domain.Material
import com.example.recipecollection.domain.MaterialAllergen
import com.example.recipecollection.dto.MaterialDto
import com.example.recipecollection.dto.MaterialRequest
import com.example.recipecollection.dto.PageResponse
import com.example.recipecollection.dto.PageableRequest
import com.example.recipecollection.mapper.MaterialMapper
import com.example.recipecollection.repository.AllergenRepository
import com.example.recipecollection.repository.MaterialCategoryRepository
import com.example.recipecollection.repository.MaterialRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MaterialService(
    private val materialRepository: MaterialRepository,
    private val materialCategoryRepository: MaterialCategoryRepository,
    private val allergenRepository: AllergenRepository,
    private val materialMapper: MaterialMapper,
) {
    fun list(showDeleted: Boolean): List<MaterialDto> =
        materialRepository.findAll()
            .filter { showDeleted || !it.deleted }
            .map(materialMapper::toDto)

    fun get(id: Long): MaterialDto = materialMapper.toDto(findEntity(id))

    fun listPageable(showDeleted: Boolean, pageable: PageableRequest): PageResponse<MaterialDto> {
        val filtered = materialRepository.findAll()
            .filter { showDeleted || !it.deleted }
            .filter { it.name.contains(pageable.filter, ignoreCase = true) }
        val sorted = PageSupport.applySorting(
            filtered,
            pageable,
            mapOf("id" to { it.id }, "name" to { it.name }),
        )
        return PageSupport.toPage(sorted.map(materialMapper::toDto), pageable)
    }

    fun listByCategory(materialCategoryId: Long, showDeleted: Boolean): List<MaterialDto> =
        materialRepository.findAll()
            .filter { showDeleted || !it.deleted }
            .filter { it.materialCategory.id == materialCategoryId }
            .map(materialMapper::toDto)

    fun listByAllergens(allergenIds: List<Long>, showDeleted: Boolean): List<MaterialDto> {
        if (allergenIds.isEmpty()) return emptyList()
        return materialRepository.findAll()
            .filter { showDeleted || !it.deleted }
            .filter { material -> material.materialAllergens.any { allergenIds.contains(it.allergen.id) } }
            .map(materialMapper::toDto)
    }

    @Transactional
    fun create(request: MaterialRequest): MaterialDto {
        val category = materialCategoryRepository.findById(request.materialCategoryId)
            .orElseThrow { NoSuchElementException("Material category ${request.materialCategoryId} not found") }
        val material = Material(name = request.name, materialCategory = category)
        applyAllergens(material, request.allergenIds)
        return materialMapper.toDto(materialRepository.save(material))
    }

    @Transactional
    fun update(id: Long, request: MaterialRequest): MaterialDto {
        val material = findEntity(id)
        val category = materialCategoryRepository.findById(request.materialCategoryId)
            .orElseThrow { NoSuchElementException("Material category ${request.materialCategoryId} not found") }
        material.name = request.name
        material.materialCategory = category
        material.materialAllergens.clear()
        applyAllergens(material, request.allergenIds)
        return materialMapper.toDto(materialRepository.save(material))
    }

    @Transactional
    fun delete(id: Long) {
        val material = findEntity(id)
        material.deleted = true
        materialRepository.save(material)
    }

    private fun applyAllergens(material: Material, allergenIds: List<Long>) {
        if (allergenIds.isEmpty()) return
        val allergens = allergenRepository.findAllById(allergenIds)
        allergens.forEach { allergen ->
            material.materialAllergens.add(MaterialAllergen(allergen = allergen, material = material))
        }
    }

    private fun findEntity(id: Long) = materialRepository.findById(id)
        .orElseThrow { NoSuchElementException("Material $id not found") }
        .also { if (it.deleted) throw NoSuchElementException("Material $id not found") }
}
