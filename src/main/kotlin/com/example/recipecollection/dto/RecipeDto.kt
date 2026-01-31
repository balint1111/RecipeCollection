package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank

data class RecipeDto(
    val id: Long?,
    val code: String,
    val name: String,
    val description: String,
    val preparationDuration: Long,
    val cookingDuration: Long,
    val totalDuration: Long,
    val createdByUserId: Long,
    val imgBase64: String?,
    val ingredientGroups: List<IngredientGroupDto>,
    val isFavorite: Boolean = false,
)

data class RecipeRequest(
    @field:NotBlank val code: String,
    @field:NotBlank val name: String,
    @field:NotBlank val description: String,
    val preparationDuration: Long = 0,
    val cookingDuration: Long,
    val createdByUserId: Long,
    val imgBase64: String? = null,
    val ingredientGroupIds: List<Long> = emptyList(),
)
