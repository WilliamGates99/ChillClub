package com.xeniac.chillclub.core.ui.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xeniac.chillclub.core.ui.navigation.Screen
import com.xeniac.chillclub.feature_music_player.presensation.MusicPlayerScreen
import com.xeniac.chillclub.feature_settings.presentation.SettingsScreen

@Composable
fun SetupRootNavGraph(
    rootNavController: NavHostController,
    startDestination: Screen = Screen.MusicPlayerScreen
) {
    NavHost(
        navController = rootNavController,
        startDestination = startDestination
    ) {
        composable<Screen.MusicPlayerScreen> {
            MusicPlayerScreen(
                onNavigateToSettingsScreen = {
                    rootNavController.navigate(Screen.SettingsScreen)
                }
            )
        }

        composable<Screen.SettingsScreen> {
            SettingsScreen(
                onNavigateUp = rootNavController::navigateUp
            )
        }
    }
}