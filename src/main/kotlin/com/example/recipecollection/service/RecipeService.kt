package com.example.recipecollection.service

import com.example.recipecollection.domain.Recipe
import com.example.recipecollection.dto.RecipeDto
import com.example.recipecollection.dto.RecipeRequest
import com.example.recipecollection.mapper.RecipeMapper
import com.example.recipecollection.repository.ApplicationUserRepository
import com.example.recipecollection.repository.IngredientGroupRepository
import com.example.recipecollection.repository.RecipeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val userRepository: ApplicationUserRepository,
    private val ingredientGroupRepository: IngredientGroupRepository,
    private val recipeMapper: RecipeMapper,
) {
    fun list(): List<RecipeDto> = recipeRepository.findAll().map(recipeMapper::toDto)

    fun get(id: Long): RecipeDto = recipeMapper.toDto(findEntity(id))

    @Transactional
    fun create(request: RecipeRequest): RecipeDto {
        val user = userRepository.findById(request.createdByUserId)
            .orElseThrow { NoSuchElementException("User ${request.createdByUserId} not found") }
        val recipe = Recipe(
            code = request.code,
            name = request.name,
            description = request.description,
            preparationDuration = request.preparationDuration,
            cookingDuration = request.cookingDuration,
            createdBy = user,
            imgBase64 = request.imgBase64,
        )
        applyIngredientGroups(recipe, request.ingredientGroupIds)
        return recipeMapper.toDto(recipeRepository.save(recipe))
    }

    @Transactional
    fun update(id: Long, request: RecipeRequest): RecipeDto {
        val recipe = findEntity(id)
        val user = userRepository.findById(request.createdByUserId)
            .orElseThrow { NoSuchElementException("User ${request.createdByUserId} not found") }
        recipe.code = request.code
        recipe.name = request.name
        recipe.description = request.description
        recipe.preparationDuration = request.preparationDuration
        recipe.cookingDuration = request.cookingDuration
        recipe.createdBy = user
        recipe.imgBase64 = request.imgBase64
        recipe.ingredientGroups.clear()
        applyIngredientGroups(recipe, request.ingredientGroupIds)
        return recipeMapper.toDto(recipeRepository.save(recipe))
    }

    @Transactional
    fun delete(id: Long) {
        val recipe = findEntity(id)
        recipe.deleted = true
        recipeRepository.save(recipe)
    }

    private fun applyIngredientGroups(recipe: Recipe, groupIds: List<Long>) {
        if (groupIds.isEmpty()) return
        val groups = ingredientGroupRepository.findAllById(groupIds)
        groups.forEach { group ->
            group.recipe = recipe
            recipe.ingredientGroups.add(group)
        }
    }

    private fun findEntity(id: Long) = recipeRepository.findById(id)
        .orElseThrow { NoSuchElementException("Recipe $id not found") }
}
