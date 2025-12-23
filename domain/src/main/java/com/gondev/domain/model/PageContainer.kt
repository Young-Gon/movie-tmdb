package com.gondev.domain.model

data class PageContainer<T>(
    val page: Int,
    val results: List<T>,
    val totalPages: Int,
    val totalResults: Int
){
    companion object{
        fun <T> createTestInstance(results: List<T>) = PageContainer(
            page = 1,
            results = results,
            totalPages = 1,
            totalResults = results.size
        )
    }
}
