package com.xeniac.chillclub.feature_settings.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.utils.IntentHelper
import com.xeniac.chillclub.core.presentation.utils.ObserverAsEvent
import com.xeniac.chillclub.core.presentation.utils.UiEvent
import com.xeniac.chillclub.core.presentation.utils.getNavigationBarHeightDp
import com.xeniac.chillclub.core.ui.components.SwipeableSnackbar
import com.xeniac.chillclub.feature_settings.presentation.components.AboutSection
import com.xeniac.chillclub.feature_settings.presentation.components.AppVersionSection
import com.xeniac.chillclub.feature_settings.presentation.components.GeneralSettings
import com.xeniac.chillclub.feature_settings.presentation.components.SupportSection
import com.xeniac.chillclub.feature_settings.presentation.utils.SettingsUiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val appTheme by viewModel.appTheme.collectAsStateWithLifecycle(initialValue = null)
    val isPlayInBackgroundEnabled by viewModel.isPlayInBackgroundEnabled.collectAsStateWithLifecycle(
        initialValue = null
    )

    var isIntentAppNotFoundErrorVisible by rememberSaveable { mutableStateOf(false) }

    ObserverAsEvent(flow = viewModel.setAppThemeEventChannel) { event ->
        when (event) {
            is SettingsUiEvent.UpdateAppTheme -> event.newAppTheme.setAppTheme()
            is UiEvent.ShowShortSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
            else -> Unit
        }
    }

    ObserverAsEvent(flow = viewModel.setPlayInBackgroundEventChannel) { event ->
        when (event) {
            is UiEvent.ShowShortSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
            else -> Unit
        }
    }

    LaunchedEffect(key1 = isIntentAppNotFoundErrorVisible) {
        if (isIntentAppNotFoundErrorVisible) {
            val result = snackbarHostState.showSnackbar(
                message = context.getString(R.string.error_intent_app_not_found),
                duration = SnackbarDuration.Short
            )

            when (result) {
                SnackbarResult.ActionPerformed -> Unit
                SnackbarResult.Dismissed -> {
                    isIntentAppNotFoundErrorVisible = false
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SwipeableSnackbar(hostState = snackbarHostState)
        },
        topBar = {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 28.dp + getNavigationBarHeightDp(),
                        bottom = 28.dp
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_core_back),
                    contentDescription = stringResource(id = R.string.core_content_description_back),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable(
                            onClick = onNavigateUp,
                            role = Role.Button,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
                        )
                )

                Text(
                    text = stringResource(id = R.string.settings_app_bar_title),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = innerPadding.calculateTopPadding() + 4.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            GeneralSettings(
                appTheme = appTheme,
                isPlayInBackgroundEnabled = isPlayInBackgroundEnabled,
                onThemeChange = { newAppTheme ->
                    viewModel.onEvent(SettingsEvent.SetCurrentAppTheme(newAppTheme))
                },
                onPlayInBackgroundChange = { isChecked ->
                    viewModel.onEvent(SettingsEvent.SetPlayInBackgroundSwitch(isChecked))
                },
                modifier = Modifier.fillMaxWidth()
            )

            AboutSection(
                openUrlInInAppBrowser = { url ->
                    IntentHelper.openLinkInInAppBrowser(
                        context = context,
                        urlString = url
                    )
                },
                onContactUsClick = {
                    isIntentAppNotFoundErrorVisible = IntentHelper.sendEmail(context)
                },
                onShareClick = {
                    isIntentAppNotFoundErrorVisible = IntentHelper.sendShareMessage(context)
                },
                modifier = Modifier.fillMaxWidth()
            )

            SupportSection(
                openUrlInInAppBrowser = { url ->
                    IntentHelper.openLinkInInAppBrowser(
                        context = context,
                        urlString = url
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            AppVersionSection(modifier = Modifier.fillMaxWidth())
        }
    }
}