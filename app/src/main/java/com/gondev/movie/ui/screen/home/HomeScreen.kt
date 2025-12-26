package com.gondev.movie.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.gondev.domain.model.IMediaModel
import com.gondev.movie.navi.HomeTab
import com.gondev.movie.ui.screen.home.tabs.MovieTab
import com.gondev.movie.ui.screen.home.tabs.SearchTab
import com.gondev.movie.ui.screen.home.tabs.TVTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, gotoDetail: (IMediaModel) -> Unit) {
    var tabBackstack by rememberSaveable() {
        mutableStateOf(
            listOf<HomeTab>(
                HomeTab.Search,
                HomeTab.TvShow,
                HomeTab.Movie
            )
        )
    }
    val currentTab = tabBackstack.last()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            // 상단 헤더 추가
            CenterAlignedTopAppBar(
                title = {
                    val titleText = when (currentTab) {
                        is HomeTab.Movie -> "Movies"
                        is HomeTab.TvShow -> "TV Shows"
                        is HomeTab.Search -> "Search"
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
                    selected = currentTab is HomeTab.Movie,
                    onClick = {
                        if (currentTab !is HomeTab.Movie) {
                            tabBackstack =
                                listOf(
                                    HomeTab.Search,
                                    HomeTab.TvShow,
                                    HomeTab.Movie
                                )

                        }
                    },
                    icon = { Icon(Icons.Default.Movie, contentDescription = null) },
                    label = { Text("Movies") }
                )
                // 2. TV 탭
                NavigationBarItem(
                    selected = currentTab is HomeTab.TvShow,
                    onClick = {
                        if (currentTab !is HomeTab.TvShow) {
                            tabBackstack =
                                listOf(
                                    HomeTab.Search,
                                    HomeTab.Movie,
                                    HomeTab.TvShow
                                )

                        }
                    },
                    icon = { Icon(Icons.Default.Tv, contentDescription = null) },
                    label = { Text("TVs") }
                )
                // 3. 검색 탭
                NavigationBarItem(
                    selected = currentTab is HomeTab.Search,
                    onClick = {
                        if (currentTab !is HomeTab.Search) {
                            tabBackstack =
                                listOf(
                                    HomeTab.TvShow,
                                    HomeTab.Movie,
                                    HomeTab.Search
                                )

                        }
                    },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Search") }
                )
            }
        }) { innerPadding ->
        NavDisplay(
            backStack = tabBackstack,
            onBack = { tabBackstack = emptyList() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<HomeTab.Movie> { MovieTab(gotoDetail = gotoDetail) }
                entry<HomeTab.TvShow> { TVTab(gotoDetail = gotoDetail) }
                entry<HomeTab.Search> { SearchTab(gotoDetail = gotoDetail) }
            },
        )
    }
}
