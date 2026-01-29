package com.example.recipecollection.mapper

import com.example.recipecollection.domain.Allergen
import com.example.recipecollection.dto.AllergenDto
import com.example.recipecollection.dto.AllergenRequest
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface AllergenMapper {
    fun toDto(entity: Allergen): AllergenDto
    fun toEntity(request: AllergenRequest): Allergen
    fun updateEntity(request: AllergenRequest, @MappingTarget entity: Allergen)
}
