package com.xeniac.chillclub.feature_music_player.domain.use_cases

import dagger.Lazy

data class MusicPlayerUseCases(
    val getRadioStationsUseCase: Lazy<GetRadioStationsUseCase>,
    val getCurrentlyPlayingRadioStationUseCase: Lazy<GetCurrentlyPlayingRadioStationUseCase>,
    val observeMusicVolumeChangesUseCase: Lazy<ObserveMusicVolumeChangesUseCase>,
    val adjustMusicVolumeUseCase: Lazy<AdjustMusicVolumeUseCase>,
    val getIsPlayInBackgroundEnabledUseCase: Lazy<GetIsPlayInBackgroundEnabledUseCase>,
    val getCurrentlyPlayingRadioStationIdUseCase: Lazy<GetCurrentlyPlayingRadioStationIdUseCase>,
    val storeCurrentlyPlayingRadioStationIdUseCase: Lazy<StoreCurrentlyPlayingRadioStationIdUseCase>,
    val getNotificationPermissionCountUseCase: Lazy<GetNotificationPermissionCountUseCase>,
    val storeNotificationPermissionCountUseCase: Lazy<StoreNotificationPermissionCountUseCase>
)