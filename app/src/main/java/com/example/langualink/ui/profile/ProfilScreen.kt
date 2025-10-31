package com.example.langualink.ui.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    Log.d("ProfileScreen", "Composing ProfileScreen")
    val userWithBadges by viewModel.userWithBadges.collectAsState()

    Column {
        if (userWithBadges != null) {
            val user = userWithBadges!!.user
            val badges = userWithBadges!!.badges
            Text(text = "Name: ${user.name}")
            Text(text = "Language: ${user.currentLanguageId}")
            Text(text = "Level: ${user.currentLevel}")
            Text(text = "Points: ${user.points}")
            Text(text = "Badges count: ${badges.size}")
            Text(text = "Badges:")
            badges.forEach { badge ->
                Text(text = "- ${badge.name}: ${badge.description}")
            }
        } else {
            Text(text = "Loading...")
        }
    }
}