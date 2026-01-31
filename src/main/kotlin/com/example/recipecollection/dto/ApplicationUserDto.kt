package com.example.recipecollection.dto

import com.example.recipecollection.domain.UserRole
import jakarta.validation.constraints.NotBlank

data class ApplicationUserDto(
    val id: Long?,
    val username: String,
    val name: String,
    val settlement: String,
    val country: String,
    val roles: Set<UserRole>,
)

data class ApplicationUserRequest(
    @NotBlank val username: String,
    @NotBlank val name: String,
    @NotBlank val settlement: String,
    @NotBlank val country: String,
    val password: String? = null,
    val roles: Set<UserRole> = emptySet(),
)
