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
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.Where

@Entity
@Table(name = "materials")
@Where(clause = "deleted = false")
class Material(
    @field:NotBlank
    @Column(nullable = false)
    var name: String,
    @field:NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_category_id", nullable = false)
    var materialCategory: MaterialCategory,
) : AbstractEntity() {

    @OneToMany(mappedBy = "material", cascade = [CascadeType.ALL], orphanRemoval = true)
    var materialAllergens: MutableList<MaterialAllergen> = mutableListOf()
}
