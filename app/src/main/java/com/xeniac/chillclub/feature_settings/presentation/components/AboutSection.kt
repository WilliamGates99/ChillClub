package com.xeniac.chillclub.feature_settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Composable
fun AboutSection(
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.surface,
    title: String = stringResource(id = R.string.settings_about_title).uppercase(),
    titleFontSize: TextUnit = 20.sp,
    titleLineHeight: TextUnit = 20.sp,
    titleFontWeight: FontWeight = FontWeight.Normal,
    onAboutUsClick: () -> Unit,
    onContactUsClick: () -> Unit,
    onSourceClick: () -> Unit,
    onDonateClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Column(modifier = modifier.background(background)) {
        Text(
            text = title,
            fontSize = titleFontSize,
            lineHeight = titleLineHeight,
            fontWeight = titleFontWeight,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 16.dp
                )
        )

        Spacer(modifier = Modifier.height(4.dp))

        SettingsClickableItem(
            icon = painterResource(id = R.drawable.ic_settings_about_us),
            title = stringResource(id = R.string.settings_about_about_us_title),
            onClick = onAboutUsClick
        )

        SettingsHorizontalDivider()

        SettingsClickableItem(
            icon = painterResource(id = R.drawable.ic_settings_contact_us),
            title = stringResource(id = R.string.settings_about_contact_us_title),
            onClick = onContactUsClick
        )

        SettingsHorizontalDivider()

        SettingsClickableItem(
            icon = painterResource(id = R.drawable.ic_settings_source),
            title = stringResource(id = R.string.settings_about_source_title),
            onClick = onSourceClick
        )

        SettingsHorizontalDivider()

        SettingsClickableItem(
//            icon = painterResource(id = R.drawable.ic_settings_donate), // TODO: UNCOMMENT
            icon = painterResource(id = R.drawable.ic_settings_source),
            title = stringResource(id = R.string.settings_about_donate_title),
            onClick = onDonateClick
        )

        SettingsHorizontalDivider()

        SettingsClickableItem(
            icon = painterResource(id = R.drawable.ic_settings_share),
            title = stringResource(id = R.string.settings_about_share_title),
            onClick = onShareClick
        )
    }
}