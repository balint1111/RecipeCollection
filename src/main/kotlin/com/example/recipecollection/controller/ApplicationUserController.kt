package com.example.recipecollection.controller

import com.example.recipecollection.dto.ApplicationUserDto
import com.example.recipecollection.dto.ApplicationUserRequest
import com.example.recipecollection.service.ApplicationUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class ApplicationUserController(
    private val applicationUserService: ApplicationUserService,
) {
    @GetMapping
    fun list(): List<ApplicationUserDto> = applicationUserService.list()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ApplicationUserDto = applicationUserService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: ApplicationUserRequest): ApplicationUserDto =
        applicationUserService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: ApplicationUserRequest): ApplicationUserDto =
        applicationUserService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        applicationUserService.delete(id)
    }
}
