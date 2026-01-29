package com.example.recipecollection.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.Where

@Entity
@Table(name = "application_users")
@Where(clause = "deleted = false")
class ApplicationUser(
    @field:NotBlank
    @Column(nullable = false)
    var username: String,
    @field:NotBlank
    @Column(nullable = false)
    var name: String,
    @field:NotBlank
    @Column(nullable = false)
    var settlement: String,
    @field:NotBlank
    @Column(nullable = false)
    var country: String,
) : AbstractEntity()
