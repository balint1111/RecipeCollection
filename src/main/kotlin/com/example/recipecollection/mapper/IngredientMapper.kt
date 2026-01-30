package com.example.recipecollection.mapper

import com.example.recipecollection.domain.Ingredient
import com.example.recipecollection.dto.IngredientDto
import com.example.recipecollection.dto.IngredientRequest
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface IngredientMapper {
    @Mapping(target = "materialId", source = "material.id")
    @Mapping(target = "ingredientGroupId", source = "ingredientGroup.id")
    fun toDto(ingredient: Ingredient): IngredientDto

    fun toEntity(request: IngredientRequest): Ingredient

    fun updateEntity(request: IngredientRequest, @MappingTarget ingredient: Ingredient)
}
