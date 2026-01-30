package com.example.recipecollection.controller

import com.example.recipecollection.dto.MaterialDto
import com.example.recipecollection.dto.MaterialRequest
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
    fun list(): List<MaterialDto> = materialService.list()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): MaterialDto = materialService.get(id)

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
