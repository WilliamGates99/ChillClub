package com.xeniac.chillclub.feature_music_player.domain.use_cases

import dagger.Lazy

data class MusicPlayerUseCases(
    val getRadiosUseCase: Lazy<GetRadiosUseCase>,
    val getNotificationPermissionCountUseCase: Lazy<GetNotificationPermissionCountUseCase>,
    val setNotificationPermissionCountUseCase: Lazy<SetNotificationPermissionCountUseCase>
)