package com.gondev.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.gondev.domain.model.PageContainer as DomainPageContainer

@Serializable
data class PageContainer<T>(
    val page: Int,
    val results: List<T>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
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