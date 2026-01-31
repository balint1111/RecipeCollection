package com.example.recipecollection.controller

import com.example.recipecollection.dto.PageResponse
import com.example.recipecollection.dto.PageableRequest
import com.example.recipecollection.dto.RecipeDto
import com.example.recipecollection.dto.RecipeRequest
import com.example.recipecollection.dto.SortDirection
import com.example.recipecollection.service.RecipeService
import jakarta.validation.Valid
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/Recipe")
class RecipeController(
    private val recipeService: RecipeService,
) {
    @GetMapping("/GetAll")
    fun list(@RequestParam(defaultValue = "false") showDeleted: Boolean): List<RecipeDto> =
        recipeService.list(showDeleted)

    @GetMapping("/GetAllPageable")
    fun listPageable(
        @RequestParam(defaultValue = "false") showDeleted: Boolean,
        @RequestParam(defaultValue = "false") justFavorites: Boolean,
        @RequestParam(defaultValue = "false") justOwn: Boolean,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "") filter: String,
        @RequestParam(required = false) sortField: String?,
        @RequestParam(required = false) sortDirection: Sort.Direction?,
    ): PageResponse<RecipeDto> = recipeService.listPageable(
        showDeleted,
        justFavorites,
        justOwn,
        PageableRequest(page = page, pageSize = pageSize, filter = filter, sortField = sortField, sortDirection = sortDirection),
    )

    @GetMapping("/GetById/{id}")
    fun get(@PathVariable id: Long): RecipeDto = recipeService.get(id)

    @GetMapping("/material/{materialId}")
    fun listByMaterialId(
        @PathVariable materialId: Long,
        @RequestParam(defaultValue = "false") showDeleted: Boolean,
    ): List<RecipeDto> = recipeService.listByMaterialId(materialId, showDeleted)

    @PostMapping("/AddFavorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addFavorite(@RequestParam recipeId: Long) {
        recipeService.addFavorite(recipeId)
    }

    @DeleteMapping("/DeleteFavorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFavorite(@RequestParam recipeId: Long) {
        recipeService.deleteFavorite(recipeId)
    }

    @PostMapping("/Create")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: RecipeRequest): RecipeDto = recipeService.create(request)

    @PutMapping("/Update/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: RecipeRequest): RecipeDto =
        recipeService.update(id, request)

    @DeleteMapping("/Delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        recipeService.delete(id)
    }
}
