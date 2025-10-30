package com.example.langualink.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProgressionScreen(viewModel: ProgressionViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (user != null) {
            Text(text = "Level: ${user!!.currentLevel}")
            LinearProgressIndicator(
            progress = { user!!.completedExerciseIds.size / 25f },
                color = ProgressIndicatorDefaults.linearColor,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
            Text(text = "Points: ${user!!.completedExerciseIds.size * 10}")
            Text(text = "Badges:")
            // TODO: Display badges
        } else {
            Text(text = "Loading...")
        }
    }
}
