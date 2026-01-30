package com.example.recipecollection.repository

import com.example.recipecollection.domain.*
import org.springframework.data.jpa.repository.JpaRepository

interface AllergenRepository : JpaRepository<Allergen, Long>
interface MaterialCategoryRepository : JpaRepository<MaterialCategory, Long>
interface MaterialRepository : JpaRepository<Material, Long>
interface MaterialAllergenRepository : JpaRepository<MaterialAllergen, Long>
interface IngredientRepository : JpaRepository<Ingredient, Long>
interface IngredientGroupRepository : JpaRepository<IngredientGroup, Long>
interface RecipeRepository : JpaRepository<Recipe, Long>
interface ApplicationUserRepository : JpaRepository<ApplicationUser, Long> {
    fun findByUsername(username: String): ApplicationUser?
}
interface UserAllergenRepository : JpaRepository<UserAllergen, Long>
interface UserFavoriteRepository : JpaRepository<UserFavorite, Long>
