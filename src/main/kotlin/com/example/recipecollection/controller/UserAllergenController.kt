package com.example.recipecollection.controller

import com.example.recipecollection.dto.UserAllergenDto
import com.example.recipecollection.dto.UserAllergenRequest
import com.example.recipecollection.service.UserAllergenService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user-allergens")
class UserAllergenController(
    private val userAllergenService: UserAllergenService,
) {
    @GetMapping
    fun list(): List<UserAllergenDto> = userAllergenService.list()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): UserAllergenDto = userAllergenService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: UserAllergenRequest): UserAllergenDto =
        userAllergenService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: UserAllergenRequest): UserAllergenDto =
        userAllergenService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        userAllergenService.delete(id)
    }
}
