package com.example.recipecollection.service

import com.example.recipecollection.domain.Ingredient
import com.example.recipecollection.domain.IngredientGroup
import com.example.recipecollection.domain.Recipe
import com.example.recipecollection.domain.UserFavorite
import com.example.recipecollection.dto.*
import com.example.recipecollection.mapper.IngredientGroupMapper
import com.example.recipecollection.mapper.RecipeMapper
import com.example.recipecollection.repository.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val userRepository: ApplicationUserRepository,
    private val ingredientGroupRepository: IngredientGroupRepository,
    private val materialRepository: MaterialRepository,
    private val recipeMapper: RecipeMapper,
    private val userFavoriteRepository: UserFavoriteRepository,
    private val currentUserService: CurrentUserService,
    private val ingredientGroupMapper: IngredientGroupMapper,
) {
    @Transactional
    fun list(showDeleted: Boolean): List<RecipeDto> {
        val favoriteIds = currentFavoriteRecipeIds()
        val recipes = recipeRepository.findAll().filter { showDeleted || !it.deleted }
        return recipes.map { recipeMapper.toDto(it).copy(isFavorite = favoriteIds.contains(it.id)) }
    }

    @Transactional
    fun get(id: Long): RecipeDto {
        val recipe = findEntity(id)
        val favoriteIds = currentFavoriteRecipeIds()
        return recipeMapper.toDto(recipe).copy(isFavorite = favoriteIds.contains(recipe.id))
    }

    @Transactional
    fun listPageable(
        showDeleted: Boolean,
        justFavorites: Boolean,
        justOwn: Boolean,
        pageable: PageableRequest,
    ): PageResponse<RecipeDto> {
        val favoriteIds = currentFavoriteRecipeIds()
        val page =
            recipeRepository
                .searchRecipes(
                    pageable.filter,
                    showDeleted,
                    currentUserService.requireCurrentUser().id!!,
                    justFavorites,
                    justOwn,
                    PageRequest.of(
                        0,
                        20,
                        if (pageable.sortDirection != null && pageable.sortField != null) {
                            Sort.by(pageable.sortDirection, pageable.sortField)
                        } else {
                            Sort.unsorted()
                        },
                    ),
                ).map { recipe ->
                    RecipeDto(
                        id = recipe.id,
                        code = recipe.code,
                        description = recipe.description,
                        name = recipe.name,
                        preparationDuration = recipe.preparationDuration,
                        cookingDuration = recipe.cookingDuration,
                        totalDuration = recipe.totalDuration,
                        createdByUserId = recipe.createdBy.id!!,
                        imgBase64 = recipe.imgBase64,
                        ingredientGroups =
                            recipe.ingredientGroups.map { ingredientGroupMapper.toDto(it) },
                        isFavorite = favoriteIds.contains(recipe.id),
                    )
                }
        return PageSupport.toPage(page)
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
        val existing =
            userFavoriteRepository.findByUserIdAndRecipeId(user.id!!, recipeId)
                ?: throw NoSuchElementException("Favorite for recipe $recipeId not found")
        existing.deleted = true
        userFavoriteRepository.save(existing)
    }

    @Transactional
    fun listByMaterialId(
        materialId: Long,
        showDeleted: Boolean,
    ): List<RecipeDto> {
        val favoriteIds = currentFavoriteRecipeIds()
        val recipes =
            recipeRepository
                .findAll()
                .filter { showDeleted || !it.deleted }
                .filter { recipe ->
                    recipe.ingredientGroups.any { group ->
                        group.ingredients.any { ingredient -> ingredient.material.id == materialId }
                    }
                }.sortedBy { it.cookingDuration }
        return recipes.map { recipeMapper.toDto(it).copy(isFavorite = favoriteIds.contains(it.id)) }
    }

    @Transactional
    fun create(request: RecipeRequest): RecipeDto {
        val user =
            userRepository.findById(request.createdByUserId).orElseThrow {
                NoSuchElementException("User ${request.createdByUserId} not found")
            }
        val recipe =
            Recipe(
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
    fun update(
        id: Long,
        request: RecipeRequest,
    ): RecipeDto {
        val recipe = findEntity(id)
        val user =
            userRepository.findById(request.createdByUserId).orElseThrow {
                NoSuchElementException("User ${request.createdByUserId} not found")
            }
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
    fun createFull(request: RecipeFullRequest): RecipeDto {
        val user = currentUserService.requireCurrentUser()
        val recipe =
            Recipe(
                code = request.code,
                name = request.name,
                description = request.description,
                preparationDuration = request.preparationDuration,
                cookingDuration = request.cookingDuration,
                createdBy = user,
                imgBase64 = request.imgBase64,
            )
        applyIngredientGroupsFull(recipe, request.ingredientGroups)
        return recipeMapper.toDto(recipeRepository.save(recipe))
    }

    @Transactional
    fun updateFull(request: RecipeFullUpdateRequest): RecipeDto {
        val recipe = findEntity(request.id)
        recipe.code = request.code
        recipe.name = request.name
        recipe.description = request.description
        recipe.preparationDuration = request.preparationDuration
        recipe.cookingDuration = request.cookingDuration
        recipe.imgBase64 = request.imgBase64
        recipe.ingredientGroups.clear()
        applyIngredientGroupsFull(recipe, request.ingredientGroups)
        return recipeMapper.toDto(recipeRepository.save(recipe))
    }

    @Transactional
    fun delete(id: Long) {
        val recipe = findEntity(id)
        recipe.deleted = true
        recipeRepository.save(recipe)
    }

    private fun applyIngredientGroups(
        recipe: Recipe,
        groupIds: List<Long>,
    ) {
        if (groupIds.isEmpty()) return
        val groups = ingredientGroupRepository.findAllById(groupIds)
        groups.forEach { group ->
            group.recipe = recipe
            recipe.ingredientGroups.add(group)
        }
    }

    private fun applyIngredientGroupsFull(
        recipe: Recipe,
        groups: List<IngredientGroupFullRequest>,
    ) {
        if (groups.isEmpty()) return
        groups.forEach { groupRequest ->
            val group = IngredientGroup(name = groupRequest.name).also { it.recipe = recipe }
            groupRequest.ingredients.forEach { ingredientRequest ->
                val material =
                    materialRepository.findById(ingredientRequest.materialId).orElseThrow {
                        NoSuchElementException("Material ${ingredientRequest.materialId} not found")
                    }
                val ingredient =
                    Ingredient(
                        material = material,
                        unit = ingredientRequest.unit,
                        quantity = ingredientRequest.quantity,
                    ).also { it.ingredientGroup = group }
                group.ingredients.add(ingredient)
            }
            recipe.ingredientGroups.add(group)
        }
    }

    private fun currentFavoriteRecipeIds(): Set<Long> {
        val user = currentUserService.requireCurrentUser()
        return userFavoriteRepository
            .findAllByUserId(user.id!!)
            .filter { !it.deleted }
            .mapNotNull { it.recipe.id }
            .toSet()
    }

    private fun findEntity(id: Long) =
        recipeRepository
            .findById(id)
            .orElseThrow { NoSuchElementException("Recipe $id not found") }
            .also { if (it.deleted) throw NoSuchElementException("Recipe $id not found") }
}
