package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class IngredientDto(
    val id: Long?,
    val material: MaterialDto,
    val unit: String,
    val quantity: BigDecimal,
)

data class IngredientRequest(
    val materialId: Long,
    @NotBlank val unit: String,
    val quantity: BigDecimal,
    val ingredientGroupId: Long?,
)
