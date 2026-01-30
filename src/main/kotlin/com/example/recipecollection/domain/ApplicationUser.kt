package com.example.recipecollection.domain

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "application_users")
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
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "application_user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var roles: MutableSet<UserRole> = mutableSetOf(),
) : AbstractEntity()
