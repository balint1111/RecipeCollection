package com.example.recipecollection.dto

import com.example.recipecollection.domain.UserRole
import jakarta.validation.constraints.NotBlank

data class AuthRegisterRequest(
    @NotBlank
    val username: String,
    @NotBlank
    val password: String,
    @NotBlank
    val name: String,
    @NotBlank
    val settlement: String,
    @NotBlank
    val country: String,
    val roles: Set<UserRole> = emptySet(),
)

data class AuthLoginRequest(
    @NotBlank
    val username: String,
    @NotBlank
    val password: String,
)

data class AuthResponse(
    val token: String,
    val user: ApplicationUserDto,
)
