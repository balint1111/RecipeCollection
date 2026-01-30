package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class IngredientDto(
    val id: Long?,
    val materialId: Long,
    val unit: String,
    val quantity: BigDecimal,
    val ingredientGroupId: Long?,
)

data class IngredientRequest(
    @field:NotNull
    val materialId: Long,
    @field:NotBlank
    val unit: String,
    @field:NotNull
    val quantity: BigDecimal,
    val ingredientGroupId: Long?,
)
