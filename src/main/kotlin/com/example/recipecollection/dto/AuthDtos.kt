package com.example.recipecollection.dto

import com.example.recipecollection.domain.UserRole
import jakarta.validation.constraints.NotBlank

data class AuthRegisterRequest(
    @field:NotBlank
    val username: String,
    @field:NotBlank
    val password: String,
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val settlement: String,
    @field:NotBlank
    val country: String,
    val roles: Set<UserRole> = emptySet(),
)

data class AuthLoginRequest(
    @field:NotBlank
    val username: String,
    @field:NotBlank
    val password: String,
)

data class AuthResponse(
    val token: String,
    val user: ApplicationUserDto,
)
