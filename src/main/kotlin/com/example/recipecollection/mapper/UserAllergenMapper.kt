package com.example.recipecollection.mapper

import com.example.recipecollection.domain.UserAllergen
import com.example.recipecollection.dto.UserAllergenDto
import com.example.recipecollection.dto.UserAllergenRequest
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface UserAllergenMapper {
    @Mapping(target = "allergenId", source = "allergen.id")
    @Mapping(target = "userId", source = "user.id")
    fun toDto(entity: UserAllergen): UserAllergenDto

    fun toEntity(request: UserAllergenRequest): UserAllergen

    fun updateEntity(
        request: UserAllergenRequest,
        @MappingTarget entity: UserAllergen,
    )
}
