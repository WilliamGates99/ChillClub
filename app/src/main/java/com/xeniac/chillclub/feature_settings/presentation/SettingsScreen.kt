package com.xeniac.chillclub.feature_settings.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.utils.IntentHelper
import com.xeniac.chillclub.core.presentation.utils.ObserverAsEvent
import com.xeniac.chillclub.core.presentation.utils.UiEvent
import com.xeniac.chillclub.core.presentation.utils.findActivity
import com.xeniac.chillclub.core.presentation.utils.getStatusBarHeightDp
import com.xeniac.chillclub.core.ui.components.NotificationPermissionDialog
import com.xeniac.chillclub.core.ui.components.SwipeableSnackbar
import com.xeniac.chillclub.feature_settings.presentation.components.AboutSection
import com.xeniac.chillclub.feature_settings.presentation.components.AppVersionSection
import com.xeniac.chillclub.feature_settings.presentation.components.GeneralSettings
import com.xeniac.chillclub.feature_settings.presentation.components.SettingsTopAppBar
import com.xeniac.chillclub.feature_settings.presentation.components.SupportSection
import com.xeniac.chillclub.feature_settings.presentation.utils.SettingsUiEvent
import kotlinx.coroutines.launch

@SuppressLint("InlinedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()

    var isIntentAppNotFoundErrorVisible by rememberSaveable { mutableStateOf(false) }

    var isPostNotificationsPermissionGranted by remember {
        mutableStateOf(
            when (ActivityCompat.checkSelfPermission(
                /* context = */ context,
                /* permission = */ Manifest.permission.POST_NOTIFICATIONS
            )) {
                PackageManager.PERMISSION_GRANTED -> true
                PackageManager.PERMISSION_DENIED -> false
                else -> false
            }
        )
    }

    val postNotificationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isPostNotificationsPermissionGranted = isGranted

        viewModel.onAction(
            SettingsAction.OnPermissionResult(
                permission = Manifest.permission.POST_NOTIFICATIONS,
                isGranted = isGranted
            )
        )
    }

    NotificationPermissionDialog(
        activity = activity,
        isVisible = settingsState.isPermissionDialogVisible,
        permissionQueue = settingsState.permissionDialogQueue,
        onConfirmClick = {
            postNotificationPermissionResultLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        },
        onDismiss = { permission ->
            viewModel.onAction(SettingsAction.DismissPermissionDialog(permission))
        }
    )


    ObserverAsEvent(flow = viewModel.setAppThemeEventChannel) { event ->
        when (event) {
            is SettingsUiEvent.UpdateAppTheme -> event.newAppTheme.setAppTheme()
            is UiEvent.ShowShortSnackbar -> {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()

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
                    snackbarHostState.currentSnackbarData?.dismiss()

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
            snackbarHostState.currentSnackbarData?.dismiss()

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
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        topBar = {
            SettingsTopAppBar(
                onBackClick = onNavigateUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 28.dp + getStatusBarHeightDp(),
                        bottom = 28.dp
                    )
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection)
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
                settingsState = settingsState,
                isPostNotificationsPermissionGranted = isPostNotificationsPermissionGranted,
                onThemeChange = { newAppTheme ->
                    viewModel.onAction(SettingsAction.StoreCurrentAppTheme(newAppTheme))
                },
                onPlayInBackgroundChange = { isChecked ->
                    viewModel.onAction(SettingsAction.StorePlayInBackgroundSwitch(isChecked))
                },
                onPlayInBackgroundClick = {
                    postNotificationPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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