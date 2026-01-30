package com.example.recipecollection.controller

import com.example.recipecollection.dto.MaterialDto
import com.example.recipecollection.dto.MaterialRequest
import com.example.recipecollection.dto.PageResponse
import com.example.recipecollection.dto.PageableRequest
import com.example.recipecollection.dto.SortDirection
import com.example.recipecollection.service.MaterialService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/materials")
class MaterialController(
    private val materialService: MaterialService,
) {
    @GetMapping
    fun list(@RequestParam(defaultValue = "false") showDeleted: Boolean): List<MaterialDto> =
        materialService.list(showDeleted)

    @GetMapping("/pageable")
    fun listPageable(
        @RequestParam(defaultValue = "false") showDeleted: Boolean,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "") filter: String,
        @RequestParam(required = false) sortField: String?,
        @RequestParam(required = false) sortDirection: SortDirection?,
    ): PageResponse<MaterialDto> = materialService.listPageable(
        showDeleted,
        PageableRequest(page = page, pageSize = pageSize, filter = filter, sortField = sortField, sortDirection = sortDirection),
    )

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): MaterialDto = materialService.get(id)

    @GetMapping("/category/{categoryId}")
    fun listByCategory(
        @PathVariable categoryId: Long,
        @RequestParam(defaultValue = "false") showDeleted: Boolean,
    ): List<MaterialDto> = materialService.listByCategory(categoryId, showDeleted)

    @GetMapping("/allergens")
    fun listByAllergens(
        @RequestParam allergenIds: List<Long>,
        @RequestParam(defaultValue = "false") showDeleted: Boolean,
    ): List<MaterialDto> = materialService.listByAllergens(allergenIds, showDeleted)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: MaterialRequest): MaterialDto = materialService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: MaterialRequest): MaterialDto =
        materialService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        materialService.delete(id)
    }
}
