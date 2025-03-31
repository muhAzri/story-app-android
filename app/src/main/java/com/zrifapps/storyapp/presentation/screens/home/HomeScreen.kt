package com.zrifapps.storyapp.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zrifapps.storyapp.common.util.FormatterUtils
import com.zrifapps.storyapp.domain.story.entity.Story
import com.zrifapps.storyapp.presentation.components.AppScaffold


@Composable
fun HomeScreen(
    onNavigateToHome: () -> Unit,
    onLogout: () -> Unit,
) {
    val stories = listOf(
        Story(
            id = "1",
            name = "Aditya Pratama",
            description = "Jangan menyerah dan terus berjuang! Dengan kerja keras dan konsistensi, kita bisa meraih tujuan kita.",
            photoUrl = "https://www.udgamschool.com/wp-content/uploads/2023/05/dummy-image-grey-e1398449111870-1024x732.jpg",
            createdAt = "2025-03-28T10:24:05.000Z",
            lat = -6.175110,
            lon = 106.865036
        ),
        Story(
            id = "2",
            name = "Budi Santoso",
            description = "Jadi Programmer harus Pantang Menyerah! Coding adalah proses pembelajaran yang tak pernah berakhir.",
            photoUrl = "https://www.udgamschool.com/wp-content/uploads/2023/05/dummy-image-grey-e1398449111870-1024x732.jpg",
            createdAt = "2025-03-27T14:50:37.000Z",
            lat = -6.175110,
            lon = 106.865036
        ),
        Story(
            id = "3",
            name = "Carolina Devi Hersinta",
            description = "3 Cara Meningkatkan Peluang Kerja sejak Bangku Kuliah: Networking, Project Portfolio, dan Magang Berkualitas",
            photoUrl = "https://www.udgamschool.com/wp-content/uploads/2023/05/dummy-image-grey-e1398449111870-1024x732.jpg",
            createdAt = "2025-03-26T09:15:22.000Z",
            lat = -6.200000,
            lon = 106.816666
        )
    )

    AppScaffold(
        title = "Dicoding Story",
        onNavigateToHome = onNavigateToHome,
        onLogout = onLogout,
        currentRoute = "home"
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(stories) { story ->
                    StoryCard(story)
                }
            }

            FloatingActionButton(
                onClick = { /* Add new story */ },
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

@Composable
fun StoryCard(story: Story) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { /* Open story details */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = story.name.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = story.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = FormatterUtils.formatDate(story.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(story.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Story image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                )
            }


            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Dicoding",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = story.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
