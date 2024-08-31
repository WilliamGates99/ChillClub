package com.xeniac.chillclub.feature_music_player.domain.use_cases

import dagger.Lazy

data class MusicPlayerUseCases(
    val observeMusicVolumeChangesUseCase: Lazy<ObserveMusicVolumeChangesUseCase>,
    val decreaseMusicVolumeUseCase: Lazy<DecreaseMusicVolumeUseCase>,
    val increaseMusicVolumeUseCase: Lazy<IncreaseMusicVolumeUseCase>,
    val getRadiosUseCase: Lazy<GetRadiosUseCase>,
    val getNotificationPermissionCountUseCase: Lazy<GetNotificationPermissionCountUseCase>,
    val storeNotificationPermissionCountUseCase: Lazy<StoreNotificationPermissionCountUseCase>
)