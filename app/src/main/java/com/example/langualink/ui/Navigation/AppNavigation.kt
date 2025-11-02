package com.example.langualink.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.langualink.ui.community.CommunityScreen
import com.example.langualink.ui.learn.ChapterDetailsScreen
import com.example.langualink.ui.learn.LearnScreen
import com.example.langualink.ui.learn.LearnViewModel
import com.example.langualink.ui.onboarding.OnboardingStepOneScreen
import com.example.langualink.ui.onboarding.OnboardingStepThreeScreen
import com.example.langualink.ui.onboarding.OnboardingStepTwoScreen
import com.example.langualink.ui.onboarding.OnboardingViewModel
import com.example.langualink.ui.profile.ProgressionScreen
import com.example.langualink.ui.SplashScreen
import com.example.langualink.ui.community.ChatDetailScreen
import com.example.langualink.ui.learn.ExerciseScreen
import com.example.langualink.ui.learn.LessonScreen

/**
 * Route definitions for the app
 */
object AppRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING_STEP_1 = "onboarding_step_1"
    const val ONBOARDING_STEP_2 = "onboarding_step_2"
    const val ONBOARDING_STEP_3 = "onboarding_step_3"
    const val MAIN_CONTENT = "main_content"
    const val CHAPTER_DETAILS = "chapter_details"
    const val EXERCISE = "exercise/{chapterId}/{level}/{exerciseId}"
    const val LESSON = "lesson/{title}/{content}"

    const val CHAT_DETAIL = "chat_detail/{chatId}"
}

/**
 * Screen definitions for the bottom navigation bar
 */
sealed class NavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Learn : NavScreen("learn", "Apprendre", Icons.Default.Book)
    object Community : NavScreen("community", "Communauté", Icons.Default.Groups)
    object Profile : NavScreen("profile", "Profil", Icons.Default.Person)
}

/**
 * Root navigation graph (handles onboarding vs main content)
 */
@Composable
fun AppRootNavigation() {
    val navController = rememberNavController()

    // Get a shared ViewModel instance scoped to this navigation graph
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()

    val onboardingCompleted by onboardingViewModel.onboardingCompleted.collectAsState(initial = false)

    val isLoading by onboardingViewModel.isLoading.collectAsState()

    if (isLoading) {
        SplashScreen(onTimeout = {})
    } else {
        val startDestination = if (onboardingCompleted) {
            AppRoutes.MAIN_CONTENT
        } else {
            AppRoutes.SPLASH
        }

        NavHost(navController = navController, startDestination = startDestination) {
            composable(AppRoutes.SPLASH) {
                SplashScreen(onTimeout = {
                    navController.navigate(AppRoutes.ONBOARDING_STEP_1) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                })
            }
            composable(AppRoutes.ONBOARDING_STEP_1) {
                OnboardingStepOneScreen(
                    viewModel = onboardingViewModel,
                    onNextClick = { navController.navigate(AppRoutes.ONBOARDING_STEP_2) }
                )
            }
            composable(AppRoutes.ONBOARDING_STEP_2) {
                OnboardingStepTwoScreen(
                    viewModel = onboardingViewModel,
                    onNextClick = { navController.navigate(AppRoutes.ONBOARDING_STEP_3) }
                )
            }
            composable(AppRoutes.ONBOARDING_STEP_3) {
                OnboardingStepThreeScreen(
                    viewModel = onboardingViewModel,
                    onBackClick = { navController.popBackStack() },
                    onFinishClick = {
                        onboardingViewModel.setOnboardingCompleted(true)
                        navController.navigate(AppRoutes.MAIN_CONTENT) {
                            popUpTo(AppRoutes.ONBOARDING_STEP_1) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppRoutes.MAIN_CONTENT) {
                MainScreenWithBottomNav()
            }
        }
    }
}

/**
 * Main app screen with bottom navigation
 */
@Composable
fun MainScreenWithBottomNav() {
    val navController = rememberNavController()
    val learnViewModel: LearnViewModel = hiltViewModel()
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
        // NavHost for the main content area (Learn, Community, Profile)
        NavHost(navController, startDestination = NavScreen.Learn.route, Modifier.padding(innerPadding)) {
            composable(NavScreen.Learn.route) { LearnScreen(learnViewModel, navController) }
            composable(NavScreen.Community.route) {
                CommunityScreen(navController = navController)
            }
            composable(NavScreen.Profile.route) { ProgressionScreen() }
            composable("${AppRoutes.CHAPTER_DETAILS}/{chapterId}") { backStackEntry ->
                val chapterId = backStackEntry.arguments?.getString("chapterId")?.toIntOrNull() ?: 0
                ChapterDetailsScreen(chapterId = chapterId, learnViewModel = learnViewModel)
            }
            composable(AppRoutes.EXERCISE) { backStackEntry ->
                ExerciseScreen(navController = navController)
            }
            composable(AppRoutes.LESSON) { backStackEntry ->
                val title = backStackEntry.arguments?.getString("title")
                val content = backStackEntry.arguments?.getString("content")
                LessonScreen(navController = navController, title = title, content = content)
            }
            composable(AppRoutes.CHAT_DETAIL) { backStackEntry ->
                ChatDetailScreen(navController = navController)
            }
        }
    }
}