package com.example.recipecollection.service

import com.example.recipecollection.domain.Recipe
import com.example.recipecollection.domain.UserFavorite
import com.example.recipecollection.dto.PageResponse
import com.example.recipecollection.dto.PageableRequest
import com.example.recipecollection.dto.RecipeDto
import com.example.recipecollection.dto.RecipeRequest
import com.example.recipecollection.mapper.RecipeMapper
import com.example.recipecollection.repository.ApplicationUserRepository
import com.example.recipecollection.repository.IngredientGroupRepository
import com.example.recipecollection.repository.RecipeRepository
import com.example.recipecollection.repository.UserFavoriteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val userRepository: ApplicationUserRepository,
    private val ingredientGroupRepository: IngredientGroupRepository,
    private val recipeMapper: RecipeMapper,
    private val userFavoriteRepository: UserFavoriteRepository,
    private val currentUserService: CurrentUserService,
) {
    fun list(showDeleted: Boolean): List<RecipeDto> {
        val favoriteIds = currentFavoriteRecipeIds()
        val recipes = recipeRepository.findAll().filter { showDeleted || !it.deleted }
        return recipes.map { recipeMapper.toDto(it).copy(isFavorite = favoriteIds.contains(it.id)) }
    }

    fun get(id: Long): RecipeDto {
        val recipe = findEntity(id)
        val favoriteIds = currentFavoriteRecipeIds()
        return recipeMapper.toDto(recipe).copy(isFavorite = favoriteIds.contains(recipe.id))
    }

    fun listPageable(
        showDeleted: Boolean,
        justFavorites: Boolean,
        justOwn: Boolean,
        pageable: PageableRequest,
    ): PageResponse<RecipeDto> {
        val user = currentUserService.requireCurrentUser()
        val favoriteIds = currentFavoriteRecipeIds()
        val filtered = recipeRepository.findAll()
            .filter { showDeleted || !it.deleted }
            .filter { it.name.contains(pageable.filter, ignoreCase = true) }
            .filter { !justOwn || it.createdBy.id == user.id }
            .filter { !justFavorites || favoriteIds.contains(it.id) }
        val sorted = PageSupport.applySorting(
            filtered,
            pageable,
            mapOf(
                "id" to { it.id },
                "name" to { it.name },
                "cookingduration" to { it.cookingDuration },
                "preparationduration" to { it.preparationDuration },
                "totalduration" to { it.totalDuration },
            ),
        )
        val mapped = sorted.map { recipeMapper.toDto(it).copy(isFavorite = favoriteIds.contains(it.id)) }
        return PageSupport.toPage(mapped, pageable)
    }

    @Transactional
    fun addFavorite(recipeId: Long) {
        val user = currentUserService.requireCurrentUser()
        val recipe = findEntity(recipeId)
        val existing = userFavoriteRepository.findByUserIdAndRecipeId(user.id!!, recipeId)
        if (existing != null) {
            if (existing.deleted) {
                existing.deleted = false
                userFavoriteRepository.save(existing)
            }
            return
        }
        userFavoriteRepository.save(UserFavorite(recipe = recipe, user = user))
    }

    @Transactional
    fun deleteFavorite(recipeId: Long) {
        val user = currentUserService.requireCurrentUser()
        val existing = userFavoriteRepository.findByUserIdAndRecipeId(user.id!!, recipeId)
            ?: throw NoSuchElementException("Favorite for recipe $recipeId not found")
        existing.deleted = true
        userFavoriteRepository.save(existing)
    }

    fun listByMaterialId(materialId: Long, showDeleted: Boolean): List<RecipeDto> {
        val favoriteIds = currentFavoriteRecipeIds()
        val recipes = recipeRepository.findAll()
            .filter { showDeleted || !it.deleted }
            .filter { recipe ->
                recipe.ingredientGroups.any { group ->
                    group.ingredients.any { ingredient -> ingredient.material.id == materialId }
                }
            }
            .sortedBy { it.cookingDuration }
        return recipes.map { recipeMapper.toDto(it).copy(isFavorite = favoriteIds.contains(it.id)) }
    }

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

    private fun currentFavoriteRecipeIds(): Set<Long> {
        val user = currentUserService.requireCurrentUser()
        return userFavoriteRepository.findAllByUserId(user.id!!)
            .filter { !it.deleted }
            .mapNotNull { it.recipe.id }
            .toSet()
    }

    private fun findEntity(id: Long) = recipeRepository.findById(id)
        .orElseThrow { NoSuchElementException("Recipe $id not found") }
        .also { if (it.deleted) throw NoSuchElementException("Recipe $id not found") }
}
