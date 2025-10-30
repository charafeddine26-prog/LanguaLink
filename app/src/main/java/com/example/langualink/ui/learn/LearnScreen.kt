package com.example.langualink.ui.learn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.langualink.model.ExerciseType
import com.example.langualink.model.Language

// import com.example.langualink.R // Import if you add real flag drawables later

@Composable
fun LearnScreen(viewModel: LearnViewModel = hiltViewModel(), navController: NavController) {
    // Collect the TopBar state from the ViewModel
    val topBarState by viewModel.topBarState.collectAsState()
    val screenState by viewModel.screenState.collectAsState()

    // Use Scaffold for standard layout structure
    Scaffold(
        topBar = {
            // Pass the state and callbacks to the improved LearnTopBar
            LearnTopBar(
                state = topBarState,
                onLanguageChange = { langId -> viewModel.changeLanguage(langId) },
            )
        }
    ) { innerPadding -> // Scaffold provides padding for content below the top bar

        // --- Rest of the Screen (Placeholder) ---
        // Apply the padding provided by Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply padding here
                .padding(16.dp), // Add your own content padding if needed
            contentAlignment = Alignment.TopCenter
        ) {
            if (screenState.isLoading) {
                CircularProgressIndicator() // Show loading indicator in content area if still loading
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    screenState.currentChapter?.let {
                        Text(text = it.title, style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "${screenState.chapterProgress}/${screenState.totalExercisesInChapter} lessons completed", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    if (screenState.exercises.isEmpty()) {
                        Text("No exercises available for this language and level yet.")
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(screenState.exercises) { exercise ->
                                Card(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { navController.navigate("exercise/${screenState.currentChapter?.id}/${exercise.id}") }) {
                                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = when (exercise.type) {
                                                ExerciseType.MULTIPLE_CHOICE, ExerciseType.TRANSLATION -> Icons.Default.TextFields
                                                ExerciseType.AUDIO -> Icons.Default.Audiotrack
                                                ExerciseType.VIDEO -> Icons.Default.Videocam
                                            },
                                            contentDescription = "Exercise Type"
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

@OptIn(ExperimentalMaterial3Api::class) // Required for TopAppBar
@Composable
fun LearnTopBar(
    state: LearnTopBarState, // Receive the state object from ViewModel
    onLanguageChange: (Int) -> Unit, // Callback when language changes
) {
    // State for controlling dropdown menu expansion
    var languageMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            // We can put the points display in the title area or actions
            // Let's try putting points in the title area for now
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Points",
                    tint = MaterialTheme.colorScheme.primary // Use theme color
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (state.isLoading) "..." else state.points.toString(), // Show points or loading dots
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium // Adjust style
                )
            }
        },
        navigationIcon = {
            // --- Language Selector as Navigation Icon ---
            Box { // Box allows the DropdownMenu to anchor correctly
                Row(
                    // Increase clickable area padding slightly
                    modifier = Modifier.clickable { languageMenuExpanded = true }.padding(start = 8.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = getFlagForLanguage(state.currentLanguageName ?: ""), modifier = Modifier.padding(end = 4.dp))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Change Language")
                }
                // Language Dropdown Menu
                DropdownMenu(
                    expanded = languageMenuExpanded,
                    onDismissRequest = { languageMenuExpanded = false } // Close when clicking outside
                ) {
                    state.availableLanguages.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(language.name) }, // Display language name
                            onClick = {
                                onLanguageChange(language.id) // Call ViewModel function
                                languageMenuExpanded = false // Close menu
                            }
                        )
                    }
                }
            }
        },
        actions = {
            // --- Level Display in Actions ---
            Row(
                modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.isLoading) "..." else state.currentLevel ?: "", // Display current level or loading dots
                    style = MaterialTheme.typography.bodyMedium // Adjust style
                )
            }
        },
        // Use Material 3 recommended colors for the top app bar
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface, // Or surfaceContainerLowest for less emphasis
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}