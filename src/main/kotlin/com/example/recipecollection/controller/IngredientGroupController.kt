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
    @GetMapping("/GetAll")
    fun list(@RequestParam(defaultValue = "false") showDeleted: Boolean): List<IngredientGroupDto> =
        ingredientGroupService.list(showDeleted)

    @GetMapping("/GetById/{id}")
    fun get(@PathVariable id: Long): IngredientGroupDto = ingredientGroupService.get(id)

    @PostMapping("/Create")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: IngredientGroupRequest): IngredientGroupDto =
        ingredientGroupService.create(request)

    @PutMapping("/Update/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: IngredientGroupRequest,
    ): IngredientGroupDto = ingredientGroupService.update(id, request)

    @DeleteMapping("/Delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        ingredientGroupService.delete(id)
    }
}
