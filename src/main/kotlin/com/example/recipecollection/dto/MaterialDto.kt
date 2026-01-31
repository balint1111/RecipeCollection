package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank

data class MaterialDto(
    val id: Long?,
    val name: String,
    val materialCategory: MaterialCategoryDto,
    val allergens: List<AllergenDto>,
)

data class MaterialRequest(
    @NotBlank val name: String,
    val materialCategoryId: Long,
    val allergenIds: List<Long> = emptyList(),
)
