package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class RecipeFullRequest(
    @field:NotBlank val code: String,
    @field:NotBlank val name: String,
    @field:NotBlank val description: String,
    val preparationDuration: Long = 0,
    @field:NotNull val cookingDuration: Long,
    val imgBase64: String? = null,
    val ingredientGroups: List<IngredientGroupFullRequest> = emptyList(),
)

data class RecipeFullUpdateRequest(
    @field:NotNull val id: Long,
    @field:NotBlank val code: String,
    @field:NotBlank val name: String,
    @field:NotBlank val description: String,
    val preparationDuration: Long = 0,
    @field:NotNull val cookingDuration: Long,
    val imgBase64: String? = null,
    val ingredientGroups: List<IngredientGroupFullRequest> = emptyList(),
)

data class IngredientGroupFullRequest(
    @field:NotBlank val name: String,
    val ingredients: List<IngredientFullRequest> = emptyList(),
)

data class IngredientFullRequest(
    @field:NotNull val materialId: Long,
    @field:NotBlank val unit: String,
    @field:NotNull val quantity: BigDecimal,
)
