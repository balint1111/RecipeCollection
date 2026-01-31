package com.example.recipecollection.dto

data class PageableRequest(
    val page: Int = 1,
    val pageSize: Int = 10,
    val filter: String = "",
    val sortField: String? = null,
    val sortDirection: SortDirection? = null,
)

data class PageResponse<T>(
    val totalCount: Long,
    val totalPages: Int,
    val page: Int,
    val pageSize: Int,
    val items: List<T>,
)

enum class SortDirection {
    ASC,
    DESC,
}
