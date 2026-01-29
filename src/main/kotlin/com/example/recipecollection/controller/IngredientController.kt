package com.example.recipecollection.controller

import com.example.recipecollection.dto.IngredientDto
import com.example.recipecollection.dto.IngredientRequest
import com.example.recipecollection.service.IngredientService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ingredients")
class IngredientController(
    private val ingredientService: IngredientService,
) {
    @GetMapping
    fun list(): List<IngredientDto> = ingredientService.list()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): IngredientDto = ingredientService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: IngredientRequest): IngredientDto = ingredientService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: IngredientRequest): IngredientDto =
        ingredientService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        ingredientService.delete(id)
    }
}
