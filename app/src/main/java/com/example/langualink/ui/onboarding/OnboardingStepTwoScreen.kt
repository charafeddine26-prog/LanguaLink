package com.example.langualink.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.langualink.R

@Composable
fun OnboardingStepTwoScreen(
    viewModel: OnboardingViewModel, // Receive the ViewModel
    onNextClick: () -> Unit
) {
    val languages by viewModel.languages.collectAsState(initial = emptyList())

    // Observe the selected language from the ViewModel
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    // Button is enabled only if a language is selected
    val isButtonEnabled = selectedLanguage != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 1. Mascot Image
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "Mascot",
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )

        // 2. Welcome Text as a Speech Bubble
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // 2a. The tail of the bubble
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .rotate(45f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // 2b. The main body of the bubble
            Surface(
                modifier = Modifier
                    .offset(y = (-8).dp), // Pull the surface up to overlap the tail
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 4.dp
            ) {
                Text(
                    text = "Which language do you want to learn?",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }
        }

        // 3. Scrollable Language List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(0.9f) // Set width to 90% of screen
                .heightIn(max = 250.dp), // Reduced height to make scrolling more obvious
            verticalArrangement = Arrangement.spacedBy(8.dp), // Add more space between items
            contentPadding = PaddingValues(horizontal = 8.dp) // Padding only on sides
        ) {
            items(languages) { language ->
                LanguageSelectItem(
                    text = language.name,
                    isSelected = (language.name == selectedLanguage),
                    onSelect = {
                        viewModel.selectLanguage(language.name) // Update ViewModel on selection
                    }
                )
            }
        }

        // 4. Next Button
        Button(
            onClick = onNextClick,
            enabled = isButtonEnabled, // Button is enabled/disabled based on selection
            modifier = Modifier.fillMaxWidth(0.4f) // Set width to 80% of screen
        ) {
            Text("NEXT STEP")
        }
    }
}

/**
 * A single selectable item for the language list.
 */
@Composable
private fun LanguageSelectItem(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        onClick = onSelect,
        // Change colors based on selection
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    ) {
        // Center the text inside the card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
