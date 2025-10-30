package com.example.langualink.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun OnboardingStepThreeScreen(
    viewModel: OnboardingViewModel, // Receive the ViewModel
    onBackClick: () -> Unit,      // Receive the Back click handler
    onFinishClick: () -> Unit
) {
    // New list of levels (A1-C2)
    val levels = listOf("A1", "A2", "B1", "B2", "C1", "C2")

    // Observe the selected level (which is now a String)
    val selectedLevel by viewModel.selectedLevel.collectAsState()

    // Observe the selected language from Step 1
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    // The "FINISH" button is enabled only after a level is selected
    val isButtonEnabled = selectedLevel != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 1. Mascot Image (Copied from Step 1)
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "Mascot",
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )

        // 2. Welcome Text as a Speech Bubble (Copied from Step 1)
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
                    text = "Please choose your level of : \n ${selectedLanguage ?: "language"}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }
        }

        // 3. Scrollable Level List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(0.9f) // 90% width
                .heightIn(max = 250.dp), // Reduced height
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(levels) { levelString -> // levelString is now "A1", "A2", etc.
                LevelSelectItem(
                    text = levelString,
                    isSelected = (levelString == selectedLevel),
                    onSelect = {
                        viewModel.selectLevel(levelString) // Update ViewModel
                    }
                )
            }
        }

        // 4. Finish Button
        Button(
            onClick = { 
                viewModel.saveUser()
                onFinishClick()
            },
            enabled = isButtonEnabled, // Enabled only when a level is selected
            modifier = Modifier.fillMaxWidth(0.8f) // 80% width
        ) {
            Text("FINISH")
        }

        // 5. Back Button
        TextButton(onClick = onBackClick) {
            Text("Back")
        }
    }
}

/**
 * A single selectable item for the level list.
 */
@Composable
private fun LevelSelectItem(
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
                text = text, // Just display the text as is (e.g., "A1")
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
