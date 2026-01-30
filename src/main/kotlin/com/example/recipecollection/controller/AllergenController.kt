package com.example.recipecollection.controller

import com.example.recipecollection.dto.AllergenDto
import com.example.recipecollection.dto.AllergenRequest
import com.example.recipecollection.service.AllergenService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/allergens")
class AllergenController(
    private val allergenService: AllergenService,
) {
    @GetMapping
    fun list(): List<AllergenDto> = allergenService.list()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): AllergenDto = allergenService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: AllergenRequest): AllergenDto = allergenService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: AllergenRequest): AllergenDto =
        allergenService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        allergenService.delete(id)
    }
}
