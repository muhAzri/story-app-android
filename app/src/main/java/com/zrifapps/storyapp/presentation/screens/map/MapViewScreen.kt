package com.zrifapps.storyapp.presentation.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.zrifapps.storyapp.R
import com.zrifapps.storyapp.common.navigation.DrawerNavigationHandler
import com.zrifapps.storyapp.presentation.components.AppScaffold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MapViewScreen(
    drawerNavigationHandler: DrawerNavigationHandler,
    onNavigateToStoryDetail: (String) -> Unit,
    viewModel: MapViewViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val mapStyleOptions = remember {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(MapViewEvent.LoadStoriesWithLocation)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MapViewViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                is MapViewViewModel.UiEvent.NavigateToStoryDetail -> {
                    onNavigateToStoryDetail(event.storyId)
                }
            }
        }
    }

    AppScaffold(
        title = "Story Map",
        drawerNavigationHandler = drawerNavigationHandler,
        currentRoute = "map",
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null -> {
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
                            text = state.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                state.stories.isEmpty() -> {
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
                            text = stringResource(R.string.no_stories_with_location_available),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.add_stories_with_location_to_see_them_on_the_map),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    val boundsBuilder = LatLngBounds.Builder()
                    state.stories.forEach { story ->
                        story.lat?.let { lat ->
                            story.lon?.let { lon ->
                                boundsBuilder.include(LatLng(lat, lon))
                            }
                        }
                    }

                    val defaultLocation = LatLng(-6.200000, 106.816666)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(defaultLocation, 7f)
                    }

                    val mapUiSettings = remember {
                        MapUiSettings(
                            zoomControlsEnabled = true,
                            compassEnabled = true,
                            mapToolbarEnabled = true,
                            scrollGesturesEnabled = true,
                            zoomGesturesEnabled = true
                        )
                    }

                    val mapProperties = remember {
                        MapProperties(
                            mapStyleOptions = mapStyleOptions
                        )
                    }

                    GoogleMap(modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = mapProperties,
                        uiSettings = mapUiSettings,
                        onMapLoaded = {
                            if (state.stories.isNotEmpty()) {
                                state.stories.firstOrNull { it.lat != null && it.lon != null }
                                    ?.let { firstStory ->
                                        val firstPosition =
                                            LatLng(firstStory.lat!!, firstStory.lon!!)
                                        val cameraUpdate =
                                            CameraUpdateFactory.newLatLngZoom(firstPosition, 7f)
                                        cameraPositionState.move(cameraUpdate)
                                    } ?: run {
                                    val bounds = boundsBuilder.build()
                                    val padding = 100
                                    val cameraUpdate =
                                        CameraUpdateFactory.newLatLngBounds(bounds, padding)
                                    cameraPositionState.move(cameraUpdate)
                                }
                            }
                        }) {
                        state.stories.forEach { story ->
                            story.lat?.let { lat ->
                                story.lon?.let { lon ->
                                    val position = LatLng(lat, lon)
                                    Marker(
                                        state = MarkerState(position = position),
                                        title = story.name,
                                        snippet = story.description,
                                        icon = BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE
                                        ),
                                        alpha = 1.0f,
                                        flat = false,
                                        visible = true,
                                        zIndex = 1.0f,
                                        tag = story.id,
                                        onClick = {
                                            viewModel.onEvent(
                                                MapViewEvent.NavigateToStoryDetail(
                                                    story.id
                                                )
                                            )
                                            true
                                        }
                                    )
                                    MarkerInfoWindow(
                                        state = MarkerState(position = position),
                                        visible = true,
                                        content = {
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        color = MaterialTheme.colorScheme.surface.copy(
                                                            alpha = 0.8f
                                                        ),
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = story.name,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
