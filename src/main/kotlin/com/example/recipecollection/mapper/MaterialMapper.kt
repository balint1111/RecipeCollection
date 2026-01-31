package com.example.recipecollection.mapper

import com.example.recipecollection.domain.Material
import com.example.recipecollection.dto.MaterialDto
import com.example.recipecollection.dto.MaterialRequest
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface MaterialMapper {
    fun toDto(material: Material): MaterialDto

    fun toEntity(request: MaterialRequest): Material

    fun updateEntity(
        request: MaterialRequest,
        @MappingTarget material: Material,
    )
}
