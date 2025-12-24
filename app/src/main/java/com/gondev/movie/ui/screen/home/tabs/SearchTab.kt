package com.gondev.movie.ui.screen.home.tabs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.gondev.domain.model.IMediaModel

@Composable
fun SearchTab(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    gotoDetail: (IMediaModel) -> Unit
) {
    Text("SearchTab")
}