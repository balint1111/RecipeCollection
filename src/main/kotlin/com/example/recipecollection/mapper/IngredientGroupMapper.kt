package com.example.recipecollection.mapper

import com.example.recipecollection.domain.IngredientGroup
import com.example.recipecollection.dto.IngredientGroupDto
import com.example.recipecollection.dto.IngredientGroupRequest
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface IngredientGroupMapper {
    @Mapping(target = "recipeId", source = "recipe.id")
    @Mapping(target = "ingredientIds", expression = "java(mapIngredientIds(group))")
    fun toDto(group: IngredientGroup): IngredientGroupDto

    fun toEntity(request: IngredientGroupRequest): IngredientGroup

    fun updateEntity(request: IngredientGroupRequest, @MappingTarget group: IngredientGroup)

    fun mapIngredientIds(group: IngredientGroup): List<Long> {
        return group.ingredients.mapNotNull { it.id }
    }
}
