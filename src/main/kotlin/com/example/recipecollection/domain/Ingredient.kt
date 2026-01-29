package com.example.recipecollection.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.Where
import java.math.BigDecimal

@Entity
@Table(name = "ingredients")
@Where(clause = "deleted = false")
class Ingredient(
    @field:NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    var material: Material,
    @field:NotBlank
    @Column(nullable = false)
    var unit: String,
    @field:NotNull
    @Column(nullable = false, precision = 18, scale = 2)
    var quantity: BigDecimal,
) : AbstractEntity() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_group_id")
    var ingredientGroup: IngredientGroup? = null
}
