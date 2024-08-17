package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository

class GetNotificationPermissionCountUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): Int = preferencesRepository.getNotificationPermissionCount()
}