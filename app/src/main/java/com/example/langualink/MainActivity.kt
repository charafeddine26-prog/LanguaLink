package com.example.langualink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.langualink.ui.community.CommunityScreen
import com.example.langualink.ui.learn.LearnScreen
import com.example.langualink.ui.onboarding.OnboardingStepOneScreen
import com.example.langualink.ui.onboarding.OnboardingStepTwoScreen
import com.example.langualink.ui.profile.ProfileScreen
import com.example.langualink.ui.theme.LanguaLinkTheme
import dagger.hilt.android.AndroidEntryPoint

object AppRoutes {
    const val ONBOARDING_STEP_1 = "onboarding_step_1"
    const val ONBOARDING_STEP_2 = "onboarding_step_2"
    const val MAIN_CONTENT = "main_content"
}

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

@Composable
fun AppRootNavigation() {
    val navController = rememberNavController()

    val onboardingCompleted = false // TODO: Logic control

    val startDestination = if (onboardingCompleted) {
        AppRoutes.MAIN_CONTENT
    } else {
        AppRoutes.ONBOARDING_STEP_1
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(AppRoutes.ONBOARDING_STEP_1) {
            OnboardingStepOneScreen(onNextClick = {
                navController.navigate(AppRoutes.ONBOARDING_STEP_2)
            })
        }
        composable(AppRoutes.ONBOARDING_STEP_2) {
            OnboardingStepTwoScreen(onFinishClick = {
                // TODO: Sava variable exact

                navController.navigate(AppRoutes.MAIN_CONTENT) {
                    popUpTo(AppRoutes.ONBOARDING_STEP_1) { inclusive = true }
                }
            })
        }

        composable(AppRoutes.MAIN_CONTENT) {
            MainScreenWithBottomNav()
        }
    }
}

@Composable
fun MainScreenWithBottomNav() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val items = listOf(
                NavScreen.Learn,
                NavScreen.Community,
                NavScreen.Profile
            )
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = NavScreen.Learn.route, Modifier.padding(innerPadding)) {
            composable(NavScreen.Learn.route) { LearnScreen() }
            composable(NavScreen.Community.route) { CommunityScreen() }
            composable(NavScreen.Profile.route) { ProfileScreen() }
        }
    }
}

sealed class NavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Learn : NavScreen("learn", "Apprendre", Icons.Default.Book)
    object Community : NavScreen("community", "Communauté", Icons.Default.Groups)
    object Profile : NavScreen("profile", "Profil", Icons.Default.Person)
}