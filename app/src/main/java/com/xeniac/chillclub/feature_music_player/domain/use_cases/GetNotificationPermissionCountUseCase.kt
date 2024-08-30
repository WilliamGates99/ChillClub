package com.xeniac.chillclub.feature_music_player.domain.use_cases

import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationPermissionCountUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Int> = preferencesRepository.getNotificationPermissionCount()
}