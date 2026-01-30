package com.example.recipecollection.dto

import jakarta.validation.constraints.NotNull

data class UserFavoriteDto(
    val id: Long?,
    val recipeId: Long,
    val userId: Long,
)

data class UserFavoriteRequest(
    @field:NotNull
    val recipeId: Long,
    @field:NotNull
    val userId: Long,
)
