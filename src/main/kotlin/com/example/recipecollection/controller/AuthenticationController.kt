package com.example.recipecollection.controller

import com.example.recipecollection.dto.AuthLoginRequest
import com.example.recipecollection.dto.AuthRegisterRequest
import com.example.recipecollection.dto.AuthResponse
import com.example.recipecollection.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/Authentication")
class AuthenticationController(
    private val authService: AuthService,
) {
    @PostMapping("/RegisterUser")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody request: AuthRegisterRequest): AuthResponse =
        authService.register(request)

    @PostMapping("/Login")
    fun login(@Valid @RequestBody request: AuthLoginRequest): AuthResponse =
        authService.login(request)
}
