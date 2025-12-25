package com.gondev.movie.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.gondev.movie.navi.Route
import com.gondev.movie.ui.common.dialog.MovieDialog
import com.gondev.movie.ui.screen.DetailScreen
import com.gondev.movie.ui.screen.DetailViewModel
import com.gondev.movie.ui.screen.home.HomeScreen

@Composable
fun MovieApp(modifier: Modifier = Modifier) {
    val appBackstack = rememberNavBackStack(Route.Home)

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        NavDisplay(
            backStack = appBackstack,
            modifier = Modifier
                .fillMaxSize(),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<Route.Home> {
                    HomeScreen {
                        appBackstack.add(Route.Detail(it))
                    }
                }
                entry<Route.Detail> { key ->
                    val viewModel = hiltViewModel<DetailViewModel, DetailViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key.mediaModel)
                        }
                    )
                    DetailScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        viewModel = viewModel,
                        onBack = appBackstack::removeLastOrNull
                    )
                }
            },
        )
    }

    MovieDialog()
}