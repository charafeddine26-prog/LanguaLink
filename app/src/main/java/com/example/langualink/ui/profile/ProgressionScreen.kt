package com.example.langualink.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.langualink.R
import com.example.langualink.model.Badge
import com.example.langualink.model.Level

@Composable
fun ProgressionScreen(viewModel: ProgressionViewModel = hiltViewModel()) {
    // 1. Subscribe to the new UiState
    val uiState by viewModel.uiState.collectAsState()
    var showCertificateModal by remember { mutableStateOf(false) }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // 2. Use LazyColumn to make the entire screen scrollable
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Header
            item {
                ProfileHeader(
                    name = uiState.user?.name ?: "User",
                    points = uiState.totalPoints
                )
            }

            // Horizontally scrolling level progress
            item {
                LevelProgressRow(
                    progressList = uiState.levelProgressList,
                    currentLevel = uiState.user?.currentLevel ?: Level.A1
                )
            }

            // Badges section
            item {
                BadgeGrid(badges = uiState.earnedBadges)
            }

            item {
                Button(onClick = { showCertificateModal = true }) {
                    Text(text = "View Certificate")
                }
            }
        }

        if (showCertificateModal) {
            CertificateModal(
                userName = uiState.user?.name ?: "User",
                language = uiState.user?.currentLanguageId.toString(),
                level = uiState.user?.currentLevel?.name ?: Level.A1.name,
                onDismiss = { showCertificateModal = false }
            )
        }
    }
}

@Composable
private fun ProfileHeader(name: String, points: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Logo (using icon.png from your project)
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "Profile Logo",
            modifier = Modifier
                .size(120.dp) // Added size modifier that was missing
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // 2. Username
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // 3. Total points
        Text(
            text = "$points Points",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun LevelProgressRow(progressList: List<LevelProgress>, currentLevel: Level) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Mon Progress",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // 4. Use LazyRow to implement horizontal scrolling
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(progressList) { progress ->
                // 5. Create a card for each level
                LevelProgressCard(
                    progress = progress,
                    // Highlight the user's current level
                    isCurrentLevel = (progress.level == currentLevel)
                )
            }
        }
    }
}

@Composable
private fun LevelProgressCard(progress: LevelProgress, isCurrentLevel: Boolean) {
    Card(
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentLevel) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrentLevel) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = progress.level.name, // "A1", "A2", etc.
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 6. Progress display
            val progressFraction = if (progress.totalCount > 0) {
                progress.completedCount.toFloat() / progress.totalCount.toFloat()
            } else {
                0f
            }

            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier.size(60.dp),
                    strokeWidth = 6.dp,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                Text(
                    text = "${progress.completedCount}/${progress.totalCount}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BadgeGrid(badges: List<Badge>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Mon Badges",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (badges.isEmpty()) {
            Text(
                text = "Pas encore de badge",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // 7. Use FlowRow to display badges with automatic line breaks
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                badges.forEach { badge ->
                    BadgeItem(badge = badge)
                }
            }
        }
    }
}

@Composable
private fun BadgeItem(badge: Badge) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp) // Give each badge a fixed width
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon), // Reminder: Using icon as a placeholder, you need to create real icons for badges
            contentDescription = badge.name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = badge.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}