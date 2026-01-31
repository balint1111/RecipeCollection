package com.example.recipecollection.dto

data class MiddlewareReturnDto(
    val statusCode: String,
    val content: Any?,
    val identity: String,
)
