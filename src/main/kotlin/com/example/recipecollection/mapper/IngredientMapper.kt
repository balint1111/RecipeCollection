package com.example.recipecollection.mapper

import com.example.recipecollection.domain.Ingredient
import com.example.recipecollection.dto.IngredientDto
import com.example.recipecollection.dto.IngredientRequest
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface IngredientMapper {
    fun toDto(ingredient: Ingredient): IngredientDto

    fun toEntity(request: IngredientRequest): Ingredient

    fun updateEntity(
        request: IngredientRequest,
        @MappingTarget ingredient: Ingredient,
    )
}
