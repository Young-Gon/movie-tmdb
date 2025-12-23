package com.gondev.movie.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.gondev.domain.model.IMediaModel
import com.gondev.movie.navi.HomeTab
import com.gondev.movie.ui.screen.home.tabs.MovieTab
import com.gondev.movie.ui.screen.home.tabs.SearchTab
import com.gondev.movie.ui.screen.home.tabs.TVTab

@Composable
fun HomeScreen(modifier: Modifier = Modifier, gotoDetail: (IMediaModel) -> Unit) {
    val tabBackstack: MutableList<HomeTab> = remember { mutableStateListOf(HomeTab.Movie) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val currentTab = tabBackstack.last()

                // 1. Movie 탭
                NavigationBarItem(
                    selected = currentTab is HomeTab.Movie,
                    onClick = { if (currentTab !is HomeTab.Movie) tabBackstack[0] = HomeTab.Movie },
                    icon = { Icon(Icons.Default.Movie, contentDescription = null) },
                    label = { Text("Movies") }
                )
                // 2. TV 탭
                NavigationBarItem(
                    selected = currentTab is HomeTab.TvShow,
                    onClick = {
                        if (currentTab !is HomeTab.TvShow) tabBackstack[0] = HomeTab.TvShow
                    },
                    icon = { Icon(Icons.Default.Tv, contentDescription = null) },
                    label = { Text("TVs") }
                )
                // 3. 검색 탭
                NavigationBarItem(
                    selected = currentTab is HomeTab.Search,
                    onClick = {
                        if (currentTab !is HomeTab.Search) tabBackstack[0] = HomeTab.Search
                    },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Search") }
                )
            }
        }) { innerPadding ->
        NavDisplay(
            backStack = tabBackstack,
            onBack = { tabBackstack.removeLastOrNull() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<HomeTab.Movie> { MovieTab(gotoDetail = gotoDetail) }
                entry<HomeTab.TvShow> { TVTab() }
                entry<HomeTab.Search> { SearchTab() }
            },
        )
    }
}