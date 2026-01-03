package com.gondev.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.gondev.domain.model.PageContainer as DomainPageContainer

@Serializable
data class PageContainer<T>(
    val page: Int=0,
    val results: List<T> = emptyList(),
    @SerialName("total_pages")
    val totalPages: Int = 0,
    @SerialName("total_results")
    val totalResults: Int = 0
) {
    fun <R> toDomain(
        resultConvertor: (T) -> R
    ): DomainPageContainer<R> = DomainPageContainer(
        page = page,
        results = results.map(resultConvertor),
        totalPages = totalPages,
        totalResults = totalResults,
    )
}