package com.xeniac.chillclub.feature_music_player.presensation.utils

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.utils.UiText

fun PlayerConstants.PlayerError.asUiText(): UiText = when (this) {
    PlayerConstants.PlayerError.VIDEO_NOT_FOUND -> UiText.StringResource(R.string.music_player_error_video_not_found)
    PlayerConstants.PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER -> UiText.StringResource(R.string.music_player_error_video_not_playable)
    PlayerConstants.PlayerError.REQUEST_MISSING_HTTP_REFERER -> UiText.StringResource(R.string.music_player_error_missing_http_referer)
    PlayerConstants.PlayerError.INVALID_PARAMETER_IN_REQUEST -> UiText.StringResource(R.string.music_player_error_invalid_parameter)
    PlayerConstants.PlayerError.HTML_5_PLAYER -> UiText.StringResource(R.string.music_player_error_html_5)
    PlayerConstants.PlayerError.UNKNOWN -> UiText.StringResource(R.string.music_player_error_unknown)
}