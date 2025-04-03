package com.zrifapps.storyapp.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.zrifapps.storyapp.R
import com.zrifapps.storyapp.common.navigation.DrawerNavigationHandler
import com.zrifapps.storyapp.domain.story.entity.Story
import com.zrifapps.storyapp.presentation.components.AppScaffold
import com.zrifapps.storyapp.presentation.components.card.StoryCard
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    drawerNavigationHandler: DrawerNavigationHandler,
    onNavigateToStoryDetail: (String) -> Unit,
    onNavigateToAddStory: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.collectAsState().value
    val storyPagingItems = viewModel.storiesPagingFlow.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeEvent.RefreshStories)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                is HomeViewModel.UiEvent.NavigateToStoryDetail -> {
                    onNavigateToStoryDetail(event.storyId)
                }

                is HomeViewModel.UiEvent.NavigateToAddStory -> {
                    onNavigateToAddStory()
                }
            }
        }
    }

    AppScaffold(
        title = "Dicoding Story",
        drawerNavigationHandler = drawerNavigationHandler,
        currentRoute = "home",
    ) {
        SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = state.isLoading || storyPagingItems.loadState.refresh is LoadState.Loading),
            onRefresh = { viewModel.onEvent(HomeEvent.RefreshStories) }) {
            Box(modifier = Modifier.fillMaxSize()) {
                HandlePagingStates(storyPagingItems = storyPagingItems, onStoryClick = { storyId ->
                    viewModel.onEvent(
                        HomeEvent.NavigateToStoryDetail(
                            storyId
                        )
                    )
                })

                FloatingActionButton(
                    onClick = { viewModel.onEvent(HomeEvent.NavigateToAddStory) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Story",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun HandlePagingStates(
    storyPagingItems: LazyPagingItems<Story>,
    onStoryClick: (String) -> Unit,
) {
    when (val refreshState = storyPagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.something_went_wrong),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = refreshState.error.localizedMessage ?: "Unknown error occurred",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        is LoadState.NotLoading -> {
            if (storyPagingItems.itemCount == 0) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.no_stories_available),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.be_the_first_to_share_a_story_by_tapping_the_button),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(count = storyPagingItems.itemCount,
                        key = { index -> storyPagingItems[index]?.id ?: index }) { index ->
                        val story = storyPagingItems[index]
                        story?.let {
                            StoryCard(story = it, onClick = { onStoryClick(it.id) })
                        }
                    }

                    item {
                        if (storyPagingItems.loadState.append is LoadState.Loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
