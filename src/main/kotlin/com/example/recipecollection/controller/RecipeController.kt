package com.example.recipecollection.controller

import com.example.recipecollection.dto.RecipeDto
import com.example.recipecollection.dto.RecipeRequest
import com.example.recipecollection.service.RecipeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes")
class RecipeController(
    private val recipeService: RecipeService,
) {
    @GetMapping
    fun list(): List<RecipeDto> = recipeService.list()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): RecipeDto = recipeService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: RecipeRequest): RecipeDto = recipeService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: RecipeRequest): RecipeDto =
        recipeService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        recipeService.delete(id)
    }
}
