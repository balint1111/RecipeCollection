package com.example.recipecollection.controller

import com.example.recipecollection.dto.IngredientGroupDto
import com.example.recipecollection.dto.IngredientGroupRequest
import com.example.recipecollection.service.IngredientGroupService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ingredient-groups")
class IngredientGroupController(
    private val ingredientGroupService: IngredientGroupService,
) {
    @GetMapping
    fun list(): List<IngredientGroupDto> = ingredientGroupService.list()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): IngredientGroupDto = ingredientGroupService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: IngredientGroupRequest): IngredientGroupDto =
        ingredientGroupService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: IngredientGroupRequest): IngredientGroupDto =
        ingredientGroupService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        ingredientGroupService.delete(id)
    }
}
