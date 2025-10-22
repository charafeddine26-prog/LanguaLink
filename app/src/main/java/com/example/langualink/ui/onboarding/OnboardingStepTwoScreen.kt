package com.example.langualink.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingStepTwoScreen(
    viewModel: OnboardingViewModel, // Receive the ViewModel
    onFinishClick: () -> Unit
) {
    // Read the selected language to confirm it was passed
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "before start - page two")
        Spacer(modifier = Modifier.height(8.dp))

        // Display the selected language to confirm it's retained
        Text(text = "Language selected: ${selectedLanguage ?: "None"}")

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onFinishClick) {
            Text("FINISH")
        }
    }
}