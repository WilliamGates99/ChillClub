package com.xeniac.chillclub.core.presentation.main_activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.xeniac.chillclub.core.presentation.common.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.chillclub.core.presentation.common.ui.theme.ChillClubTheme
import com.xeniac.chillclub.core.presentation.common.ui.theme.utils.enableEdgeToEdgeWindow
import com.xeniac.chillclub.core.presentation.common.utils.setScreenOrientationToPortrait
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen()
        enableEdgeToEdgeWindow()

        setContent {
            val windowInfo = LocalWindowInfo.current

            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = windowInfo.isWindowFocused) {
                setScreenOrientationToPortrait()
            }

            // Layout Orientation is changing automatically in Android 7 (24) and newer
            when (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                true -> CompositionLocalProvider(
                    value = LocalLayoutDirection provides state.currentAppLocale.layoutDirectionCompose,
                    content = { ChillClubRootSurface() }
                )
                false -> ChillClubRootSurface()
            }
        }
    }

    private fun splashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.state.value.isSplashScreenLoading }
        }
    }

    @Composable
    fun ChillClubRootSurface() {
        ChillClubTheme {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxSize()
            ) {
                val rootNavController = rememberNavController()

                SetupRootNavGraph(
                    rootNavController = rootNavController
                )
            }
        }
    }
}