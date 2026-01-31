package com.example.recipecollection.service

import com.example.recipecollection.dto.PageResponse
import com.example.recipecollection.dto.PageableRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import kotlin.math.ceil

object PageSupport {
    fun <T> toPage(
        items: List<T>,
        pageable: PageableRequest,
    ): PageResponse<T> {
        val pageSize = pageable.pageSize.coerceAtLeast(1)
        val page = pageable.page.coerceAtLeast(1)
        val totalCount = items.size.toLong()
        val totalPages = if (totalCount == 0L) 0 else ceil(totalCount.toDouble() / pageSize).toInt()
        val startIndex = ((page - 1) * pageSize).coerceAtMost(items.size)
        val endIndex = (startIndex + pageSize).coerceAtMost(items.size)
        val pageItems =
            if (startIndex >= items.size) emptyList() else items.subList(startIndex, endIndex)
        return PageResponse(
            totalCount = totalCount,
            totalPages = totalPages,
            page = page,
            pageSize = pageSize,
            content = pageItems,
        )
    }

    fun <T : Any> toPage(page: Page<T>): PageResponse<T> {
        page.totalPages
        return PageResponse(
            totalCount = page.totalElements,
            totalPages = page.totalPages,
            page = page.number,
            pageSize = page.size,
            content = page.content,
        )
    }

    fun <T> applySorting(
        items: List<T>,
        pageable: PageableRequest,
        selectors: Map<String, (T) -> Comparable<*>?>,
    ): List<T> {
        if (items.isEmpty()) return items
        val sortField = pageable.sortField?.lowercase() ?: "id"
        val selector = selectors[sortField] ?: selectors["id"] ?: return items
        return if (pageable.sortDirection == Sort.Direction.DESC) {
            items.sortedByDescending { selector(it) as Comparable<Any>? }
        } else {
            items.sortedBy { selector(it) as Comparable<Any>? }
        }
    }
}
