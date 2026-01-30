package com.example.recipecollection.controller

import com.example.recipecollection.dto.MaterialCategoryDto
import com.example.recipecollection.dto.MaterialCategoryRequest
import com.example.recipecollection.service.MaterialCategoryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/material-categories")
class MaterialCategoryController(
    private val materialCategoryService: MaterialCategoryService,
) {
    @GetMapping
    fun list(): List<MaterialCategoryDto> = materialCategoryService.list()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): MaterialCategoryDto = materialCategoryService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: MaterialCategoryRequest): MaterialCategoryDto =
        materialCategoryService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: MaterialCategoryRequest): MaterialCategoryDto =
        materialCategoryService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        materialCategoryService.delete(id)
    }
}
