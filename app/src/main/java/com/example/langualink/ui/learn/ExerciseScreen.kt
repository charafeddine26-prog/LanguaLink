package com.example.langualink.ui.learn

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.langualink.ui.navigation.AppRoutes

import com.example.langualink.ui.navigation.NavScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(viewModel: ExerciseViewModel = hiltViewModel(), navController: NavController) {
    val screenState by viewModel.screenState.collectAsState()
    val timer by viewModel.timer.collectAsState(initial = 30)

    if (screenState.showCompletionModal) {
        CompletionModal(
            title = "Chapitre Terminé!",
            message = "Vous avez complété tous les exercices de ce chapitre.",
            onDismiss = {
                viewModel.dismissCompletionModal()
                navController.popBackStack(NavScreen.Learn.route, inclusive = false)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exercise") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Timer
                    Text(text = timer.toString(), modifier = Modifier.padding(end = 16.dp))
                }
            )
        }
    ) { innerPadding ->
        if (screenState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            screenState.exercise?.let { exercise ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = exercise.question, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(32.dp))
                    Column(modifier = Modifier.weight(3f)) {
                        exercise.options.forEach { option ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable { viewModel.selectOption(option) },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (screenState.selectedOption == option) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(text = option, modifier = Modifier.padding(16.dp), maxLines = 3)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    if (screenState.selectedOption != null) {
                        Button(
                            onClick = {
                                val nextExercise = viewModel.getNextExercise()
                                if (nextExercise != null) {
                                    navController.navigate("exercise/${viewModel.chapterId}/${viewModel.level}/${nextExercise.id}") {
                                        popUpTo(AppRoutes.EXERCISE) { inclusive = true }
                                    }
                                } else {
                                    viewModel.showCompletionModal()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text("Next")
                        }
                    }
                    screenState.isAnswerCorrect?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (it) "Correct!" else "Incorrect. Try again.",
                            color = if (it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}