package com.xeniac.chillclub.feature_settings.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository

class StoreNotificationPermissionCountUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(
        count: Int
    ) = preferencesRepository.storeNotificationPermissionCount(count)
}