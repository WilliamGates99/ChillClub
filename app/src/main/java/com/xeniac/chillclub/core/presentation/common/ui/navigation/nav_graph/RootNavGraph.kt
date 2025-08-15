package com.xeniac.chillclub.core.presentation.common.ui.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xeniac.chillclub.core.presentation.common.ui.navigation.MusicPlayerScreen
import com.xeniac.chillclub.core.presentation.common.ui.navigation.SettingsScreen
import com.xeniac.chillclub.feature_music_player.presensation.MusicPlayerScreen
import com.xeniac.chillclub.feature_settings.presentation.SettingsScreen

@Composable
fun SetupRootNavGraph(
    rootNavController: NavHostController,
    startDestination: Any = MusicPlayerScreen
) {
    NavHost(
        navController = rootNavController,
        startDestination = startDestination
    ) {
        composable<MusicPlayerScreen> {
            MusicPlayerScreen(
                onNavigateToSettingsScreen = {
                    rootNavController.navigate(SettingsScreen)
                }
            )
        }

        composable<SettingsScreen> {
            SettingsScreen(
                onNavigateUp = rootNavController::navigateUp
            )
        }
    }
}