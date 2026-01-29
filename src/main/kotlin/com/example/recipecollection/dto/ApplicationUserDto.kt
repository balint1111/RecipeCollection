package com.example.recipecollection.dto

import jakarta.validation.constraints.NotBlank

data class ApplicationUserDto(
    val id: Long?,
    val username: String,
    val name: String,
    val settlement: String,
    val country: String,
)

data class ApplicationUserRequest(
    @field:NotBlank
    val username: String,
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val settlement: String,
    @field:NotBlank
    val country: String,
)
