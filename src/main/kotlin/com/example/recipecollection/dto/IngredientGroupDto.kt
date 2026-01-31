package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank

data class IngredientGroupDto(
    val id: Long?,
    val name: String,
    val recipeId: Long?,
    val ingredients: List<IngredientDto>,
)

data class IngredientGroupRequest(
    @field:NotBlank val name: String,
    val recipeId: Long?,
    val ingredientIds: List<Long> = emptyList(),
)
