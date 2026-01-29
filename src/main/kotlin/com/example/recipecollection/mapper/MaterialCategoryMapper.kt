package com.example.recipecollection.mapper

import com.example.recipecollection.domain.MaterialCategory
import com.example.recipecollection.dto.MaterialCategoryDto
import com.example.recipecollection.dto.MaterialCategoryRequest
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface MaterialCategoryMapper {
    fun toDto(entity: MaterialCategory): MaterialCategoryDto
    fun toEntity(request: MaterialCategoryRequest): MaterialCategory
    fun updateEntity(request: MaterialCategoryRequest, @MappingTarget entity: MaterialCategory)
}
