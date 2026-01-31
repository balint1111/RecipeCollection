package com.example.recipecollection.mapper

import com.example.recipecollection.domain.UserFavorite
import com.example.recipecollection.dto.UserFavoriteDto
import com.example.recipecollection.dto.UserFavoriteRequest
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface UserFavoriteMapper {
    @Mapping(target = "recipeId", source = "recipe.id")
    @Mapping(target = "userId", source = "user.id")
    fun toDto(entity: UserFavorite): UserFavoriteDto

    fun toEntity(request: UserFavoriteRequest): UserFavorite

    fun updateEntity(
        request: UserFavoriteRequest,
        @MappingTarget entity: UserFavorite,
    )
}
