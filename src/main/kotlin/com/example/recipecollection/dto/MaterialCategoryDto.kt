package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank

data class MaterialCategoryDto(
    val id: Long?,
    val name: String,
)

data class MaterialCategoryRequest(
    @field:NotBlank val name: String,
)
