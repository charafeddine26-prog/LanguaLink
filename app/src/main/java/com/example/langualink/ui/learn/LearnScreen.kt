package com.example.langualink.ui.learn

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.langualink.model.Language // Import Language model
import com.example.langualink.ui.learn.LearnViewModel // Import ViewModel
import com.example.langualink.ui.learn.LearnTopBarState // Import State
// import com.example.langualink.R // Import if you add real flag drawables later
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.Color

@Composable
fun LearnScreen(viewModel: LearnViewModel = hiltViewModel()) {
    // Collect the TopBar state from the ViewModel
    val topBarState by viewModel.topBarState.collectAsState()

    // Use Scaffold for standard layout structure
    Scaffold(
        topBar = {
            // Pass the state and callbacks to the improved LearnTopBar
            LearnTopBar(
                state = topBarState,
                onLanguageChange = { langId -> viewModel.changeLanguage(langId) },
                onLevelChange = { levelString -> viewModel.changeLevel(levelString) }
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
            contentAlignment = Alignment.Center
        ) {
            if (topBarState.isLoading) {
                CircularProgressIndicator() // Show loading indicator in content area if still loading
            } else {
                Text("(Lesson List Area - Placeholder)") // Placeholder for future content
            }
        }
    }
}

/**
 * Composable function for the top bar of the Learn screen, using TopAppBar.
 */
@OptIn(ExperimentalMaterial3Api::class) // Required for TopAppBar
@Composable
fun LearnTopBar(
    state: LearnTopBarState, // Receive the state object from ViewModel
    onLanguageChange: (Int) -> Unit, // Callback when language changes
    onLevelChange: (String) -> Unit  // Callback when level changes
) {
    // State for controlling dropdown menu expansion
    var languageMenuExpanded by remember { mutableStateOf(false) }
    var levelMenuExpanded by remember { mutableStateOf(false) }
    // List of levels for the dropdown
    val levels = state.availableLevels // Get levels from state

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
                    // TODO: Replace Emoji with actual Image based on state.currentLanguageName/ID
                    Text(text = "🇫🇷", modifier = Modifier.padding(end = 4.dp)) // Placeholder Emoji flag
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
            // --- Level Selector in Actions ---
            Box { // Box allows the DropdownMenu to anchor correctly
                Row(
                    // Increase clickable area padding slightly
                    modifier = Modifier.clickable { levelMenuExpanded = true }.padding(start = 4.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (state.isLoading) "..." else state.currentLevel, // Display current level or loading dots
                        style = MaterialTheme.typography.bodyMedium // Adjust style
                    )
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Change Level")
                }
                // Level Dropdown Menu
                DropdownMenu(
                    expanded = levelMenuExpanded,
                    onDismissRequest = { levelMenuExpanded = false } // Close when clicking outside
                ) {
                    levels.forEach { level ->
                        DropdownMenuItem(
                            text = { Text(level) }, // Display level string ("A1", etc.)
                            onClick = {
                                onLevelChange(level) // Call ViewModel function
                                levelMenuExpanded = false // Close menu
                            }
                        )
                    }
                }
            }
            // You could add other icons/actions here if needed
            // IconButton(onClick = { /*TODO*/ }) {
            //     Icon(Icons.Filled.MoreVert, contentDescription = "More options")
            // }
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