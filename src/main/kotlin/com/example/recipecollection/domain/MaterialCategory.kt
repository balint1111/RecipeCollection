package com.example.recipecollection.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.Where

@Entity
@Table(name = "material_categories")
@Where(clause = "deleted = false")
class MaterialCategory(
    @field:NotBlank
    @Column(nullable = false)
    var name: String,
) : AbstractEntity()
