package com.example.langualink.ui.learn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (screenState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            screenState.exercise?.let { exercise ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (backButton, timerIndicator) = createRefs()

                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.constrainAs(backButton) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }

                        LinearProgressIndicator(
                            progress = timer / 30f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(timerIndicator) {
                                    top.linkTo(backButton.bottom, margin = 16.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Help, contentDescription = "Question")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Question", style = MaterialTheme.typography.titleMedium)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = exercise.question,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(exercise.options) { option ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clickable { viewModel.selectOption(option) },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (screenState.selectedOption == option) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        if (screenState.selectedOption == option) {
                                            if (screenState.isAnswerCorrect == true) Color.Green else Color.Red
                                        } else {
                                            MaterialTheme.colorScheme.outline
                                        }
                                    )
                                ) {
                                    Text(
                                        text = option,
                                        modifier = Modifier.padding(16.dp),
                                        maxLines = 3
                                    )
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = screenState.isAnswerCorrect != null,
                        enter = fadeIn(animationSpec = tween(1000)),
                        exit = fadeOut(animationSpec = tween(1000)),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        screenState.isAnswerCorrect?.let {
                            Text(
                                text = if (it) "Correct!" else "Incorrect. Try again.",
                                color = if (it) Color.Green else Color.Red,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

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
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
        }
    }
}