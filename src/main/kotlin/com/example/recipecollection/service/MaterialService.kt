package com.example.recipecollection.service

import com.example.recipecollection.domain.Material
import com.example.recipecollection.domain.MaterialAllergen
import com.example.recipecollection.dto.MaterialDto
import com.example.recipecollection.dto.MaterialRequest
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
    fun list(): List<MaterialDto> = materialRepository.findAll().map(materialMapper::toDto)

    fun get(id: Long): MaterialDto = materialMapper.toDto(findEntity(id))

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
}
