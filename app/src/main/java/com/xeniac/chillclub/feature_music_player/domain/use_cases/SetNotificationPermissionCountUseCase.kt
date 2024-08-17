package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository

class SetNotificationPermissionCountUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(
        count: Int
    ) = preferencesRepository.setNotificationPermissionCount(count)
}