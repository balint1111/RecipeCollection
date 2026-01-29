package com.example.recipecollection.mapper

import com.example.recipecollection.domain.ApplicationUser
import com.example.recipecollection.dto.ApplicationUserDto
import com.example.recipecollection.dto.ApplicationUserRequest
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface ApplicationUserMapper {
    fun toDto(user: ApplicationUser): ApplicationUserDto
    fun toEntity(request: ApplicationUserRequest): ApplicationUser
    fun updateEntity(request: ApplicationUserRequest, @MappingTarget user: ApplicationUser)
}
