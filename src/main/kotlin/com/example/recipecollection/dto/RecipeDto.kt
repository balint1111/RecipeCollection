package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

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
    val ingredientGroupIds: List<Long>,
)

data class RecipeRequest(
    @field:NotBlank
    val code: String,
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val description: String,
    val preparationDuration: Long = 0,
    @field:NotNull
    val cookingDuration: Long,
    @field:NotNull
    val createdByUserId: Long,
    val imgBase64: String? = null,
    val ingredientGroupIds: List<Long> = emptyList(),
)
