package com.gondev.movie.navi

import androidx.navigation3.runtime.NavKey
import com.gondev.domain.model.IMediaModel
import kotlinx.serialization.Serializable

// 전체 앱의 경로 정의
sealed interface Route : NavKey {
    @Serializable
    data object Home : Route       // 메인 홈 (탭 포함)

    @Serializable
    data class Detail(val mediaModel: IMediaModel) : Route  // 상세 페이지 (전체 화면)
}

// 홈 화면 내부의 탭 정의
sealed interface HomeTab {
    @Serializable
    data object Movie : HomeTab

    @Serializable
    data object TvShow : HomeTab

    @Serializable
    data object Search : HomeTab
}