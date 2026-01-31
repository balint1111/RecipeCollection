package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


data class MaterialDto(
    val id: Long?,
    val name: String,
    val materialCategory: MaterialCategoryDto,
    val allergens: List<AllergenDto>,
)

data class MaterialRequest(
    @field:NotBlank
    val name: String,
    @field:NotNull
    val materialCategoryId: Long,
    val allergenIds: List<Long> = emptyList(),
)
