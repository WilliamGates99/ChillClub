package com.xeniac.chillclub.feature_settings.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.chillclub.BuildConfig
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.ui.theme.Gray40

@Composable
fun AppVersionSection(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        bottom = 8.dp
    ),
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Light,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = Gray40,
    maxLines: Int = 1
) {
    Text(
        text = stringResource(
            id = R.string.settings_app_version,
            BuildConfig.VERSION_NAME
        ),
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        color = color,
        maxLines = maxLines,
        modifier = modifier.padding(paddingValues)
    )
}