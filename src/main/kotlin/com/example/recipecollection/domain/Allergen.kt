package com.example.recipecollection.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.Where

@Entity
@Table(name = "allergens")
@Where(clause = "deleted = false")
class Allergen(
    @field:NotBlank
    @Column(nullable = false)
    var name: String,
    @field:NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    var imgBase64: String,
) : AbstractEntity()
