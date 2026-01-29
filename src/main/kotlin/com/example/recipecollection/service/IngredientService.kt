package com.example.recipecollection.service

import com.example.recipecollection.domain.Ingredient
import com.example.recipecollection.dto.IngredientDto
import com.example.recipecollection.dto.IngredientRequest
import com.example.recipecollection.mapper.IngredientMapper
import com.example.recipecollection.repository.IngredientGroupRepository
import com.example.recipecollection.repository.IngredientRepository
import com.example.recipecollection.repository.MaterialRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IngredientService(
    private val ingredientRepository: IngredientRepository,
    private val materialRepository: MaterialRepository,
    private val ingredientGroupRepository: IngredientGroupRepository,
    private val ingredientMapper: IngredientMapper,
) {
    fun list(): List<IngredientDto> = ingredientRepository.findAll().map(ingredientMapper::toDto)

    fun get(id: Long): IngredientDto = ingredientMapper.toDto(findEntity(id))

    @Transactional
    fun create(request: IngredientRequest): IngredientDto {
        val material = materialRepository.findById(request.materialId)
            .orElseThrow { NoSuchElementException("Material ${request.materialId} not found") }
        val ingredient = Ingredient(material = material, unit = request.unit, quantity = request.quantity)
        applyGroup(ingredient, request.ingredientGroupId)
        return ingredientMapper.toDto(ingredientRepository.save(ingredient))
    }

    @Transactional
    fun update(id: Long, request: IngredientRequest): IngredientDto {
        val ingredient = findEntity(id)
        val material = materialRepository.findById(request.materialId)
            .orElseThrow { NoSuchElementException("Material ${request.materialId} not found") }
        ingredient.material = material
        ingredient.unit = request.unit
        ingredient.quantity = request.quantity
        applyGroup(ingredient, request.ingredientGroupId)
        return ingredientMapper.toDto(ingredientRepository.save(ingredient))
    }

    @Transactional
    fun delete(id: Long) {
        val ingredient = findEntity(id)
        ingredient.deleted = true
        ingredientRepository.save(ingredient)
    }

    private fun applyGroup(ingredient: Ingredient, ingredientGroupId: Long?) {
        ingredient.ingredientGroup = ingredientGroupId?.let { id ->
            ingredientGroupRepository.findById(id)
                .orElseThrow { NoSuchElementException("Ingredient group $id not found") }
        }
    }

    private fun findEntity(id: Long) = ingredientRepository.findById(id)
        .orElseThrow { NoSuchElementException("Ingredient $id not found") }
}
