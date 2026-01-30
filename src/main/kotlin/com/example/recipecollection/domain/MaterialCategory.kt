package com.example.recipecollection.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
@Entity
@Table(name = "material_categories")
class MaterialCategory(
    @field:NotBlank
    @Column(nullable = false)
    var name: String,
) : AbstractEntity()
