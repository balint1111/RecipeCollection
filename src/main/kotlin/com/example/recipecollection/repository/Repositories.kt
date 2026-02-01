package com.example.recipecollection.repository

import com.example.recipecollection.domain.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AllergenRepository : JpaRepository<Allergen, Long> {
    fun findAllByNameContainsIgnoreCaseAndDeleted(
        name: String?,
        deleted: Boolean,
        pageable: Pageable,
    ): Page<Allergen>
}

interface MaterialCategoryRepository : JpaRepository<MaterialCategory, Long> {
    fun findAllByNameContainsIgnoreCaseAndDeleted(
        name: String?,
        deleted: Boolean,
        pageable: Pageable,
    ): Page<MaterialCategory>
}

interface MaterialRepository : JpaRepository<Material, Long> {
    fun findAllByNameContainsIgnoreCaseAndDeleted(
        name: String?,
        deleted: Boolean,
        pageable: Pageable,
    ): Page<Material>

    fun findAllByDeleted(deleted: Boolean): List<Material>
}

interface MaterialAllergenRepository : JpaRepository<MaterialAllergen, Long>

interface IngredientRepository : JpaRepository<Ingredient, Long>

interface IngredientGroupRepository : JpaRepository<IngredientGroup, Long>

interface RecipeRepository : JpaRepository<Recipe, Long> {
    @Query(
        """
        select r
        from Recipe r
        where lower(r.name) like lower(concat('%', :name, '%'))
          and r.deleted = :deleted
          and (
               :justFavorites = false
               or exists (
                    select 1
                    from UserFavorite uf
                    where uf.user.id = :userId
                      and uf.deleted = false
                      and uf.recipe = r
               )
          ) and (
               :justOwn = false
               or r.createdBy.id = :userId
          )
        """
    )
    fun searchRecipes(
        @Param("name") name: String,
        @Param("deleted") deleted: Boolean,
        @Param("userId") userId: Long,
        @Param("justFavorites") justFavorites: Boolean,
        @Param("justOwn") justOwn: Boolean,
        pageable: Pageable,
    ): Page<Recipe>
}

interface ApplicationUserRepository : JpaRepository<ApplicationUser, Long> {
    fun findByUsername(username: String): ApplicationUser?
}

interface UserAllergenRepository : JpaRepository<UserAllergen, Long> {
    fun findByUserIdAndAllergenId(userId: Long, allergenId: Long): UserAllergen?

    fun findAllByUserId(userId: Long): List<UserAllergen>
}

interface UserFavoriteRepository : JpaRepository<UserFavorite, Long> {
    fun findByUserIdAndRecipeId(userId: Long, recipeId: Long): UserFavorite?

    fun findAllByUserId(userId: Long): List<UserFavorite>
}
