package com.example.langualink.ui.learn

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.langualink.model.ExerciseType

@Composable
fun LearnScreen(viewModel: LearnViewModel = hiltViewModel(), navController: NavController) {
    val topBarState by viewModel.topBarState.collectAsState()
    val screenState by viewModel.screenState.collectAsState()

    Scaffold(
        topBar = {
            LearnTopBar(
                state = topBarState,
                onLanguageChange = { langId -> viewModel.changeLanguage(langId) },
                onLevelChange = { levelString -> viewModel.changeLevel(levelString) }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (screenState.isLoading) {
                CircularProgressIndicator()
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    screenState.currentChapter?.let {
                        Text(text = it.title, style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "${screenState.chapterProgress}/${screenState.totalExercisesInChapter} lessons completed", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    screenState.currentLesson?.let { lesson ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    navController.navigate("lesson/${lesson.title}/${lesson.content}")
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = lesson.title, style = MaterialTheme.typography.titleLarge)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (screenState.exercises.isEmpty()) {
                        if (screenState.totalExercisesInChapter > 0 && screenState.chapterProgress == screenState.totalExercisesInChapter) {
                            Text("Vous avez fini tous les exos de ce level")
                            Text("Choissiez les autres level")
                        } else {
                            Text("No exercises available for this language and level yet.")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(screenState.exercises) { exercise ->

                                val isCompleted = exercise.id in screenState.completedExerciseIds

                                val cardColors = if (isCompleted) {
                                    CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                    )
                                } else {
                                    CardDefaults.cardColors()
                                }

                                val icon = if (isCompleted) {
                                    Icons.Default.CheckCircle
                                } else {
                                    when (exercise.type) {
                                        ExerciseType.MULTIPLE_CHOICE, ExerciseType.TRANSLATION -> Icons.Default.TextFields
                                        ExerciseType.AUDIO -> Icons.Default.Audiotrack
                                        ExerciseType.VIDEO -> Icons.Default.Videocam
                                    }
                                }

                                val iconTint = if (isCompleted) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    LocalContentColor.current
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clickable { navController.navigate("exercise/${screenState.currentChapter?.id}/${topBarState.currentLevel}/${exercise.id}") },
                                    colors = cardColors
                                ) {
                                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = "Exercise Type",
                                            tint = iconTint
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(text = exercise.question)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable function for the top bar of the Learn screen, using TopAppBar.
 */
fun getFlagForLanguage(language: String): String {
    return when (language) {
        "Français" -> "🇫🇷"
        "Español" -> "🇪🇸"
        "Deutsch" -> "🇩🇪"
        "日本語" -> "🇯🇵"
        "Português" -> "🇵🇹"
        "Italiano" -> "🇮🇹"
        "한국어" -> "🇰🇷"
        "Русский" -> "🇷🇺"
        "中文" -> "🇨🇳"
        "العربية" -> "🇸🇦"
        else -> "🏳️"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnTopBar(
    state: LearnTopBarState,
    onLanguageChange: (Int) -> Unit,
    onLevelChange: (String) -> Unit
) {
    var languageMenuExpanded by remember { mutableStateOf(false) }
    var levelMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Points",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (state.isLoading) "..." else state.points.toString(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        navigationIcon = {
            Box {
                Row(
                    modifier = Modifier.clickable { languageMenuExpanded = true }.padding(start = 8.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = getFlagForLanguage(state.currentLanguageName ?: ""), modifier = Modifier.padding(end = 4.dp))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Change Language")
                }
                DropdownMenu(
                    expanded = languageMenuExpanded,
                    onDismissRequest = { languageMenuExpanded = false }
                ) {
                    state.availableLanguages.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(language.name) },
                            onClick = {
                                onLanguageChange(language.id)
                                languageMenuExpanded = false
                            }
                        )
                    }
                }
            }
        },
        actions = {
            Box {
                Row(
                    modifier = Modifier
                        .clickable { levelMenuExpanded = true }
                        .padding(start = 4.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (state.isLoading) "..." else state.currentLevel ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Change Level")
                }

                DropdownMenu(
                    expanded = levelMenuExpanded,
                    onDismissRequest = { levelMenuExpanded = false }
                ) {
                    state.availableLevels.forEach { levelString ->
                        DropdownMenuItem(
                            text = { Text(levelString) },
                            onClick = {
                                onLevelChange(levelString)
                                levelMenuExpanded = false
                            }
                        )
                    }
                }
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}