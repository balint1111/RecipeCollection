package com.example.recipecollection.service

import com.example.recipecollection.domain.IngredientGroup
import com.example.recipecollection.dto.IngredientGroupDto
import com.example.recipecollection.dto.IngredientGroupRequest
import com.example.recipecollection.mapper.IngredientGroupMapper
import com.example.recipecollection.repository.IngredientGroupRepository
import com.example.recipecollection.repository.IngredientRepository
import com.example.recipecollection.repository.RecipeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IngredientGroupService(
    private val ingredientGroupRepository: IngredientGroupRepository,
    private val ingredientRepository: IngredientRepository,
    private val recipeRepository: RecipeRepository,
    private val ingredientGroupMapper: IngredientGroupMapper,
) {
    fun list(): List<IngredientGroupDto> = ingredientGroupRepository.findAll().map(ingredientGroupMapper::toDto)

    fun get(id: Long): IngredientGroupDto = ingredientGroupMapper.toDto(findEntity(id))

    @Transactional
    fun create(request: IngredientGroupRequest): IngredientGroupDto {
        val group = IngredientGroup(name = request.name)
        applyRecipe(group, request.recipeId)
        applyIngredients(group, request.ingredientIds)
        return ingredientGroupMapper.toDto(ingredientGroupRepository.save(group))
    }

    @Transactional
    fun update(id: Long, request: IngredientGroupRequest): IngredientGroupDto {
        val group = findEntity(id)
        group.name = request.name
        applyRecipe(group, request.recipeId)
        group.ingredients.clear()
        applyIngredients(group, request.ingredientIds)
        return ingredientGroupMapper.toDto(ingredientGroupRepository.save(group))
    }

    @Transactional
    fun delete(id: Long) {
        val group = findEntity(id)
        group.deleted = true
        ingredientGroupRepository.save(group)
    }

    private fun applyRecipe(group: IngredientGroup, recipeId: Long?) {
        group.recipe = recipeId?.let { id ->
            recipeRepository.findById(id)
                .orElseThrow { NoSuchElementException("Recipe $id not found") }
        }
    }

    private fun applyIngredients(group: IngredientGroup, ingredientIds: List<Long>) {
        if (ingredientIds.isEmpty()) return
        val ingredients = ingredientRepository.findAllById(ingredientIds)
        ingredients.forEach { ingredient ->
            ingredient.ingredientGroup = group
            group.ingredients.add(ingredient)
        }
    }

    private fun findEntity(id: Long) = ingredientGroupRepository.findById(id)
        .orElseThrow { NoSuchElementException("Ingredient group $id not found") }
}
