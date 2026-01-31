package com.example.recipecollection.controller

import com.example.recipecollection.dto.MaterialCategoryDto
import com.example.recipecollection.dto.MaterialCategoryRequest
import com.example.recipecollection.dto.PageResponse
import com.example.recipecollection.dto.PageableRequest
import com.example.recipecollection.service.MaterialCategoryService
import jakarta.validation.Valid
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/MaterialCategory")
class MaterialCategoryController(
    private val materialCategoryService: MaterialCategoryService,
) {
    @GetMapping("/GetAll")
    fun list(@RequestParam(defaultValue = "false") showDeleted: Boolean): List<MaterialCategoryDto> =
        materialCategoryService.list(showDeleted)

    @GetMapping("/GetAllPageable")
    fun listPageable(
        @RequestParam(defaultValue = "false") showDeleted: Boolean,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "") filter: String,
        @RequestParam(required = false) sortField: String?,
        @RequestParam(required = false) sortDirection: Sort.Direction?,
    ): PageResponse<MaterialCategoryDto> =
        materialCategoryService.listPageable(
            showDeleted,
            PageableRequest(
                page = page,
                pageSize = pageSize,
                filter = filter,
                sortField = sortField,
                sortDirection = sortDirection,
            ),
        )

    @GetMapping("/GetById/{id}")
    fun get(@PathVariable id: Long): MaterialCategoryDto = materialCategoryService.get(id)

    @PostMapping("/Create")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: MaterialCategoryRequest): MaterialCategoryDto =
        materialCategoryService.create(request)

    @PutMapping("/Update/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: MaterialCategoryRequest,
    ): MaterialCategoryDto = materialCategoryService.update(id, request)

    @DeleteMapping("/Delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        materialCategoryService.delete(id)
    }
}
