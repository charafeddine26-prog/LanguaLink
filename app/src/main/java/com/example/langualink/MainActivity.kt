package com.example.langualink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.langualink.ui.navigation.AppRootNavigation
import com.example.langualink.ui.theme.LanguaLinkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguaLinkTheme {
                AppRootNavigation()
            }
        }
    }
}