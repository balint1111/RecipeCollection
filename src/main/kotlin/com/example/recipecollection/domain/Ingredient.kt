package com.example.recipecollection.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

@Entity
@Table(name = "ingredients")
class Ingredient(
    @field:NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    var material: Material,
    @field:NotBlank @Column(nullable = false) var unit: String,
    @field:NotNull @Column(nullable = false, precision = 18, scale = 2) var quantity: BigDecimal,
) : AbstractEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_group_id")
    var ingredientGroup: IngredientGroup? = null
}
