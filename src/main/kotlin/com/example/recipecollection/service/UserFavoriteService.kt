package com.example.recipecollection.service

import com.example.recipecollection.domain.UserFavorite
import com.example.recipecollection.dto.UserFavoriteDto
import com.example.recipecollection.dto.UserFavoriteRequest
import com.example.recipecollection.mapper.UserFavoriteMapper
import com.example.recipecollection.repository.ApplicationUserRepository
import com.example.recipecollection.repository.RecipeRepository
import com.example.recipecollection.repository.UserFavoriteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserFavoriteService(
    private val userFavoriteRepository: UserFavoriteRepository,
    private val recipeRepository: RecipeRepository,
    private val userRepository: ApplicationUserRepository,
    private val userFavoriteMapper: UserFavoriteMapper,
) {
    fun list(): List<UserFavoriteDto> = userFavoriteRepository.findAll().map(userFavoriteMapper::toDto)

    fun get(id: Long): UserFavoriteDto = userFavoriteMapper.toDto(findEntity(id))

    @Transactional
    fun create(request: UserFavoriteRequest): UserFavoriteDto {
        val recipe = recipeRepository.findById(request.recipeId)
            .orElseThrow { NoSuchElementException("Recipe ${request.recipeId} not found") }
        val user = userRepository.findById(request.userId)
            .orElseThrow { NoSuchElementException("User ${request.userId} not found") }
        val entity = UserFavorite(recipe = recipe, user = user)
        return userFavoriteMapper.toDto(userFavoriteRepository.save(entity))
    }

    @Transactional
    fun update(id: Long, request: UserFavoriteRequest): UserFavoriteDto {
        val entity = findEntity(id)
        val recipe = recipeRepository.findById(request.recipeId)
            .orElseThrow { NoSuchElementException("Recipe ${request.recipeId} not found") }
        val user = userRepository.findById(request.userId)
            .orElseThrow { NoSuchElementException("User ${request.userId} not found") }
        entity.recipe = recipe
        entity.user = user
        return userFavoriteMapper.toDto(userFavoriteRepository.save(entity))
    }

    @Transactional
    fun delete(id: Long) {
        val entity = findEntity(id)
        entity.deleted = true
        userFavoriteRepository.save(entity)
    }

    private fun findEntity(id: Long) = userFavoriteRepository.findById(id)
        .orElseThrow { NoSuchElementException("User favorite $id not found") }
}
