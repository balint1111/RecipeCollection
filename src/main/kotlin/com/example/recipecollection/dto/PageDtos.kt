package com.example.recipecollection.dto

import org.springframework.data.domain.Sort

data class PageableRequest(
    val page: Int = 1,
    val pageSize: Int = 10,
    val filter: String = "",
    val sortField: String? = null,
    val sortDirection: Sort.Direction? = null,
)

data class PageResponse<T>(
    val totalCount: Long,
    val totalPages: Int,
    val page: Int,
    val pageSize: Int,
    val content: List<T>,
)

enum class SortDirection {
    ASC,
    DESC,
}
