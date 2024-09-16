package com.xeniac.chillclub.feature_music_player.domain.use_cases

import dagger.Lazy

data class MusicPlayerUseCases(
    val observeMusicVolumeChangesUseCase: Lazy<ObserveMusicVolumeChangesUseCase>,
    val adjustMusicVolumeUseCase: Lazy<AdjustMusicVolumeUseCase>,
    val getRadioStationsUseCase: Lazy<GetRadioStationsUseCase>,
    val getCurrentlyPlayingRadioStationIdUseCase: Lazy<GetCurrentlyPlayingRadioStationIdUseCase>,
    val getCurrentlyPlayingRadioStationUseCase: Lazy<GetCurrentlyPlayingRadioStationUseCase>,
    val getIsPlayInBackgroundEnabledUseCase: Lazy<GetIsPlayInBackgroundEnabledUseCase>,
    val getNotificationPermissionCountUseCase: Lazy<GetNotificationPermissionCountUseCase>,
    val storeCurrentlyPlayingRadioStationIdUseCase: Lazy<StoreCurrentlyPlayingRadioStationIdUseCase>,
    val storeNotificationPermissionCountUseCase: Lazy<StoreNotificationPermissionCountUseCase>
)