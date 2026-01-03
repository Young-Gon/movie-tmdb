package com.gondev.domain.model

data class PageContainer<T>(
    val page: Int=0,
    val results: List<T> = emptyList(),
    val totalPages: Int = 0,
    val totalResults: Int = 0
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
