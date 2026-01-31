package com.example.recipecollection.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "recipes")
class Recipe(
    @field:NotBlank @Column(nullable = false) var code: String,
    @field:NotBlank @Column(nullable = false) var name: String,
    @field:NotBlank @Column(nullable = false, columnDefinition = "TEXT") var description: String,
    @Column(nullable = false) var preparationDuration: Long = 0,
    @field:NotNull @Column(nullable = false) var cookingDuration: Long,
    @field:NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    var createdBy: ApplicationUser,
    @Column(columnDefinition = "TEXT") var imgBase64: String? = null,
) : AbstractEntity() {
    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], orphanRemoval = true)
    var ingredientGroups: MutableList<IngredientGroup> = mutableListOf()

    val totalDuration: Long
        get() = preparationDuration + cookingDuration
}
