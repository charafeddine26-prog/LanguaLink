package com.example.langualink.ui.learn

import androidx.compose.material3.Button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.material3.Button

// ... existing code ...

@Composable
fun ChapterDetailsScreen(
    chapterId: Int,
    learnViewModel: LearnViewModel,
    viewModel: ChapterDetailsViewModel = hiltViewModel()
) {
    val chapter by viewModel.chapter.collectAsState()
    val topBarState by learnViewModel.topBarState.collectAsState()
    val bravoState by viewModel.bravoState.collectAsState()

    if (bravoState != null) {
        BravoModal(
            points = bravoState!!.points,
            badgeName = bravoState!!.badgeName,
            onDismiss = { viewModel.hideBravoModal() }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadChapter(chapterId, topBarState.currentLanguageId ?: 1, com.example.langualink.model.Level.valueOf(topBarState.currentLevel ?: "A1"))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (chapter == null) {
            CircularProgressIndicator()
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(chapter!!.exercises) { exercise ->
                    Card(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                        Text(text = exercise.question, modifier = Modifier.padding(16.dp))
                        Button(onClick = { viewModel.markExerciseAsComplete(exercise.id) }) {
                            Text(text = "Complete")
                        }
                    }
                }
            }
        }
    }
}
