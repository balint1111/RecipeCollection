package com.example.recipecollection.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "material_allergens")
class MaterialAllergen(
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allergen_id", nullable = false)
    var allergen: Allergen,
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    var material: Material,
) : AbstractEntity()
