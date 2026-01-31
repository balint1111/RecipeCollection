package com.example.recipecollection.mapper

import com.example.recipecollection.domain.Recipe
import com.example.recipecollection.dto.RecipeDto
import com.example.recipecollection.dto.RecipeRequest
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface RecipeMapper {
    @Mapping(target = "createdByUserId", source = "createdBy.id")
    @Mapping(target = "isFavorite", constant = "false")
    fun toDto(recipe: Recipe): RecipeDto

    fun toEntity(request: RecipeRequest): Recipe

    fun updateEntity(
        request: RecipeRequest,
        @MappingTarget recipe: Recipe,
    )
}
