package com.example.recipecollection.controller

import com.example.recipecollection.dto.PageResponse
import com.example.recipecollection.dto.PageableRequest
import com.example.recipecollection.dto.RecipeDto
import com.example.recipecollection.dto.RecipeRequest
import com.example.recipecollection.dto.SortDirection
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
    fun list(@RequestParam(defaultValue = "false") showDeleted: Boolean): List<RecipeDto> =
        recipeService.list(showDeleted)

    @GetMapping("/pageable")
    fun listPageable(
        @RequestParam(defaultValue = "false") showDeleted: Boolean,
        @RequestParam(defaultValue = "false") justFavorites: Boolean,
        @RequestParam(defaultValue = "false") justOwn: Boolean,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "") filter: String,
        @RequestParam(required = false) sortField: String?,
        @RequestParam(required = false) sortDirection: SortDirection?,
    ): PageResponse<RecipeDto> = recipeService.listPageable(
        showDeleted,
        justFavorites,
        justOwn,
        PageableRequest(page = page, pageSize = pageSize, filter = filter, sortField = sortField, sortDirection = sortDirection),
    )

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): RecipeDto = recipeService.get(id)

    @GetMapping("/material/{materialId}")
    fun listByMaterialId(
        @PathVariable materialId: Long,
        @RequestParam(defaultValue = "false") showDeleted: Boolean,
    ): List<RecipeDto> = recipeService.listByMaterialId(materialId, showDeleted)

    @PostMapping("/{id}/favorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addFavorite(@PathVariable id: Long) {
        recipeService.addFavorite(id)
    }

    @DeleteMapping("/{id}/favorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFavorite(@PathVariable id: Long) {
        recipeService.deleteFavorite(id)
    }

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
