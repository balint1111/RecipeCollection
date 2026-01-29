package com.example.recipecollection.controller

import com.example.recipecollection.dto.UserFavoriteDto
import com.example.recipecollection.dto.UserFavoriteRequest
import com.example.recipecollection.service.UserFavoriteService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user-favorites")
class UserFavoriteController(
    private val userFavoriteService: UserFavoriteService,
) {
    @GetMapping
    fun list(): List<UserFavoriteDto> = userFavoriteService.list()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): UserFavoriteDto = userFavoriteService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: UserFavoriteRequest): UserFavoriteDto =
        userFavoriteService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: UserFavoriteRequest): UserFavoriteDto =
        userFavoriteService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        userFavoriteService.delete(id)
    }
}
