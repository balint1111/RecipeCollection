package com.example.recipecollection.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "materials")
class Material(
    @field:NotBlank @Column(nullable = false) var name: String,
    @field:NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_category_id", nullable = false)
    var materialCategory: MaterialCategory,
) : AbstractEntity() {
    @OneToMany(orphanRemoval = false)
    @JoinTable(
        name = "material_allergens",
        joinColumns = [JoinColumn(name = "material_id")],
        inverseJoinColumns = [JoinColumn(name = "allergen_id")],
    )
    var allergens: MutableList<Allergen> = mutableListOf()
}
