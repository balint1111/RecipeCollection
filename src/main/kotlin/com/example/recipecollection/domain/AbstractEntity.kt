package com.example.recipecollection.domain

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault

@MappedSuperclass
abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @NotNull
    @ColumnDefault(value = "false")
    var deleted: Boolean = false
}
