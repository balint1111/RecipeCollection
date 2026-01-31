package com.example.recipecollection.controller

import com.example.recipecollection.dto.AllergenDto
import com.example.recipecollection.dto.AllergenRequest
import com.example.recipecollection.dto.PageResponse
import com.example.recipecollection.dto.PageableRequest
import com.example.recipecollection.dto.SortDirection
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
    fun list(@RequestParam(defaultValue = "false") showDeleted: Boolean): List<AllergenDto> =
        allergenService.list(showDeleted)

    @GetMapping("/pageable")
    fun listPageable(
        @RequestParam(defaultValue = "false") showDeleted: Boolean,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "") filter: String,
        @RequestParam(required = false) sortField: String?,
        @RequestParam(required = false) sortDirection: SortDirection?,
    ): PageResponse<AllergenDto> = allergenService.listPageable(
        showDeleted,
        PageableRequest(page = page, pageSize = pageSize, filter = filter, sortField = sortField, sortDirection = sortDirection),
    )

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): AllergenDto = allergenService.get(id)

    @PostMapping("/{id}/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addAllergen(@PathVariable id: Long) {
        allergenService.addAllergen(id)
    }

    @DeleteMapping("/{id}/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAllergen(@PathVariable id: Long) {
        allergenService.deleteAllergen(id)
    }

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
