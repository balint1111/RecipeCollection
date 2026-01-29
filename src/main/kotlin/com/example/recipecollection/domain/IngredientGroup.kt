package com.example.recipecollection.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.Where

@Entity
@Table(name = "ingredient_groups")
@Where(clause = "deleted = false")
class IngredientGroup(
    @field:NotBlank
    @Column(nullable = false)
    var name: String,
) : AbstractEntity() {

    @OneToMany(mappedBy = "ingredientGroup", cascade = [CascadeType.ALL], orphanRemoval = true)
    var ingredients: MutableList<Ingredient> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    var recipe: Recipe? = null
}
