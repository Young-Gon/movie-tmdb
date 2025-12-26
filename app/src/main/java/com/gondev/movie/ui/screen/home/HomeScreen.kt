package com.gondev.movie.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.gondev.domain.model.IMediaModel
import com.gondev.movie.ui.screen.home.tabs.MovieTab
import com.gondev.movie.ui.screen.home.tabs.SearchTab
import com.gondev.movie.ui.screen.home.tabs.TVTab
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, gotoDetail: (IMediaModel) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            // 상단 헤더 추가
            CenterAlignedTopAppBar(
                title = {
                    val titleText = when (pagerState.currentPage) {
                        0 -> "Movies"
                        1 -> "TV Shows"
                        2 -> "Search"
                        else -> ""
                    }
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        bottomBar = {
            NavigationBar {
                // 1. Movie 탭
                NavigationBarItem(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    icon = { Icon(Icons.Default.Movie, contentDescription = null) },
                    label = { Text("Movies") }
                )
                // 2. TV 탭
                NavigationBarItem(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    icon = { Icon(Icons.Default.Tv, contentDescription = null) },
                    label = { Text("TVs") }
                )
                // 3. 검색 탭
                NavigationBarItem(
                    selected = pagerState.currentPage == 2,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Search") }
                )
            }
        }) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            beyondViewportPageCount = 2, // 모든 탭을 메모리에 유지
            userScrollEnabled = false    // 스와이프를 막고 싶을 때
        ) { page ->
            when (page) {
                0 -> MovieTab(gotoDetail = gotoDetail)
                1 -> TVTab(gotoDetail = gotoDetail)
                2 -> SearchTab(gotoDetail = gotoDetail)
            }
        }
    }
}
