package com.gondev.domain.model

data class PageContainer<T>(
    val page: Int,
    val results: List<T>,
    val totalPages: Int,
    val totalResults: Int
)
