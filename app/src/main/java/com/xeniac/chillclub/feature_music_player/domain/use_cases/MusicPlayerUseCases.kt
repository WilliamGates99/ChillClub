package com.xeniac.chillclub.feature_music_player.domain.use_cases

import dagger.Lazy

data class MusicPlayerUseCases(
    val observeMusicVolumeChangesUseCase: Lazy<ObserveMusicVolumeChangesUseCase>,
    val adjustMusicVolumeUseCase: Lazy<AdjustMusicVolumeUseCase>,
    val getRadioStationsUseCase: Lazy<GetRadioStationsUseCase>,
    val getIsPlayInBackgroundEnabledUseCase: Lazy<GetIsPlayInBackgroundEnabledUseCase>,
    val getNotificationPermissionCountUseCase: Lazy<GetNotificationPermissionCountUseCase>,
    val storeNotificationPermissionCountUseCase: Lazy<StoreNotificationPermissionCountUseCase>
)