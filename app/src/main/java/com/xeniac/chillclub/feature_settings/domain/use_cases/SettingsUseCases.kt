package com.xeniac.chillclub.feature_settings.domain.use_cases

import dagger.Lazy
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
data class SettingsUseCases @Inject constructor(
    val getCurrentAppThemeUseCase: Lazy<GetCurrentAppThemeUseCase>,
    val getIsPlayInBackgroundEnabledUseCase: Lazy<GetIsPlayInBackgroundEnabledUseCase>,
    val storeCurrentAppThemeUseCase: Lazy<StoreCurrentAppThemeUseCase>,
    val storeIsPlayInBackgroundEnabledUseCase: Lazy<StoreIsPlayInBackgroundEnabledUseCase>
)