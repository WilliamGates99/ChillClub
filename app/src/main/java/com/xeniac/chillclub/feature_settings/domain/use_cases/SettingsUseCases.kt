package com.xeniac.chillclub.feature_settings.domain.use_cases

import dagger.Lazy

data class SettingsUseCases(
    val getCurrentAppThemeUseCase: Lazy<GetCurrentAppThemeUseCase>,
    val getIsPlayInBackgroundEnabledUseCase: Lazy<GetIsPlayInBackgroundEnabledUseCase>,
    val setCurrentAppThemeUseCase: Lazy<SetCurrentAppThemeUseCase>,
    val setIsPlayInBackgroundEnabledUseCase: Lazy<SetIsPlayInBackgroundEnabledUseCase>
)