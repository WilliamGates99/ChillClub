package com.xeniac.chillclub.feature_settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.feature_settings.presentation.states.SettingsState
import com.xeniac.chillclub.feature_settings.presentation.utils.TestTags

@Composable
fun GeneralSettings(
    settingsState: SettingsState,
    isPostNotificationsPermissionGranted: Boolean,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.surface,
    title: String = stringResource(id = R.string.settings_general_title).uppercase(),
    titleFontSize: TextUnit = 20.sp,
    titleLineHeight: TextUnit = 20.sp,
    titleFontWeight: FontWeight = FontWeight.Normal,
    onThemeChange: (newAppTheme: AppTheme) -> Unit,
    onPlayInBackgroundChange: (isChecked: Boolean) -> Unit,
    onPlayInBackgroundClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = modifier
            .background(background)
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            )
    ) {
        Text(
            text = title,
            fontSize = titleFontSize,
            lineHeight = titleLineHeight,
            fontWeight = titleFontWeight,
            modifier = Modifier.fillMaxWidth()
        )

        SettingsSwitchRow(
            icon = painterResource(id = R.drawable.ic_settings_theme),
            title = stringResource(id = R.string.settings_general_theme_title),
            isChecked = when (settingsState.currentAppTheme) {
                AppTheme.Dark -> true
                AppTheme.Light -> false
                null -> null
            },
            testTag = TestTags.SWITCH_THEME,
            onCheckedChange = { isChecked ->
                when (isChecked) {
                    true -> onThemeChange(AppTheme.Dark)
                    false -> onThemeChange(AppTheme.Light)
                }
            }
        )

        SettingsSwitchRow(
            isEnabled = isPostNotificationsPermissionGranted,
            icon = painterResource(id = R.drawable.ic_settings_background_player),
            title = stringResource(id = R.string.settings_general_background_player_title),
            description = stringResource(id = R.string.settings_general_background_play_description),
            isChecked = isPostNotificationsPermissionGranted && settingsState.isPlayInBackgroundEnabled == true,
            testTag = TestTags.SWITCH_BACKGROUND_PLAYER,
            onCheckedChange = onPlayInBackgroundChange,
            onRowClick = if (isPostNotificationsPermissionGranted) null else onPlayInBackgroundClick
        )
    }
}