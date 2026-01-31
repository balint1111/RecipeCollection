package com.example.recipecollection.dto

import jakarta.validation.constraints.NotNull

data class UserAllergenDto(
    val id: Long?,
    val allergenId: Long,
    val userId: Long,
)

data class UserAllergenRequest(
    @field:NotNull val allergenId: Long,
    @field:NotNull val userId: Long,
)
