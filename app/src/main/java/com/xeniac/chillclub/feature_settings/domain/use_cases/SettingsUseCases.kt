package com.xeniac.chillclub.feature_settings.domain.use_cases

import dagger.Lazy

data class SettingsUseCases(
    val getCurrentAppThemeUseCase: Lazy<GetCurrentAppThemeUseCase>,
    val getIsPlayInBackgroundEnabledUseCase: Lazy<GetIsPlayInBackgroundEnabledUseCase>,
    val getNotificationPermissionCountUseCase: Lazy<GetNotificationPermissionCountUseCase>,
    val storeCurrentAppThemeUseCase: Lazy<StoreCurrentAppThemeUseCase>,
    val storeIsPlayInBackgroundEnabledUseCase: Lazy<StoreIsPlayInBackgroundEnabledUseCase>,
    val storeNotificationPermissionCountUseCase: Lazy<StoreNotificationPermissionCountUseCase>
)