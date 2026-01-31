package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank

data class AllergenDto(
    val id: Long?,
    val name: String,
    val imgBase64: String,
)

data class AllergenRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val imgBase64: String,
)
