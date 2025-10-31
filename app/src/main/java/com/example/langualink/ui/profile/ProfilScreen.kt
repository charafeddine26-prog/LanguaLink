package com.example.langualink.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState()

    Column {
        if (user != null) {
            Text(text = "Name: ${user!!.name}")
            Text(text = "Language: ${user!!.currentLanguageId}")
            Text(text = "Level: ${user!!.currentLevel}")
        } else {
            Text(text = "Loading...")
        }
    }
}