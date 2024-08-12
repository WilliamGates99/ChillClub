package com.xeniac.chillclub.core.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import com.xeniac.chillclub.core.presentation.utils.NetworkObserverHelper.observeNetworkConnection
import com.xeniac.chillclub.core.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.chillclub.core.ui.theme.ChillClubTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        splashScreen()
        observeNetworkConnection(connectivityObserver)

        setContent {
            val mainState by viewModel.mainState.collectAsStateWithLifecycle()

            // Layout Orientation is changing automatically in Android 7 (24) and newer
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                CompositionLocalProvider(
                    value = LocalLayoutDirection provides mainState.currentAppLocale.layoutDirectionCompose
                ) {
                    ChillClubRootSurface()
                }
            } else {
                ChillClubRootSurface()
            }
        }
    }

    private fun splashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.mainState.value.isSplashScreenLoading }
        }
    }

    @Composable
    fun ChillClubRootSurface() {
        ChillClubTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val rootNavController = rememberNavController()

                SetupRootNavGraph(
                    rootNavController = rootNavController
                )
            }
        }
    }
}