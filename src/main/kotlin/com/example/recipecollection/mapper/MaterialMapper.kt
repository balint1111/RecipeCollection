package com.example.recipecollection.mapper

import com.example.recipecollection.domain.Material
import com.example.recipecollection.dto.MaterialDto
import com.example.recipecollection.dto.MaterialRequest
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface MaterialMapper {
    @Mapping(target = "materialCategoryId", source = "materialCategory.id")
    @Mapping(target = "allergenIds", expression = "java(mapAllergenIds(material))")
    fun toDto(material: Material): MaterialDto

    fun toEntity(request: MaterialRequest): Material

    fun updateEntity(request: MaterialRequest, @MappingTarget material: Material)

    fun mapAllergenIds(material: Material): List<Long> {
        return material.materialAllergens.mapNotNull { it.allergen.id }
    }
}
