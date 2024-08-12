package com.xeniac.chillclub.core.data.repositories

import com.xeniac.chillclub.core.data.dto.AppLocaleDto
import com.xeniac.chillclub.core.data.dto.AppThemeDto
import com.xeniac.chillclub.core.data.dto.RateAppOptionDto
import com.xeniac.chillclub.core.data.mapper.toAppLocale
import com.xeniac.chillclub.core.data.mapper.toAppTheme
import com.xeniac.chillclub.core.data.mapper.toRateAppOption
import com.xeniac.chillclub.core.data.utils.DateHelper
import com.xeniac.chillclub.core.domain.models.AppLocale
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.RateAppOption
import com.xeniac.chillclub.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.core.domain.repositories.PreviousRateAppRequestTimeInMs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakePreferencesRepositoryImpl @Inject constructor() : PreferencesRepository {

    var appTheme: AppTheme = AppTheme.Default
    var appLocale: AppLocale = AppLocale.Default
    var isOnBoardingCompleted = false
    var notificationPermissionCount = 0
    var selectedRateAppOption: RateAppOption = RateAppOption.NOT_SHOWN_YET
    var previousRateAppRequestTime: PreviousRateAppRequestTimeInMs? = null

    override fun getCurrentAppThemeSynchronously(): AppTheme = appTheme

    override fun getCurrentAppTheme(): Flow<AppTheme> = flow { emit(appTheme) }

    override suspend fun getCurrentAppLocale(): AppLocale = appLocale

    override suspend fun getNotificationPermissionCount(): Int = notificationPermissionCount

    override suspend fun getSelectedRateAppOption(): Flow<RateAppOption> = flow {
        emit(selectedRateAppOption)
    }

    override suspend fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?> =
        flow { previousRateAppRequestTime }

    override suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto) {
        appTheme = appThemeDto.toAppTheme()
    }

    override suspend fun setCurrentAppLocale(
        appLocaleDto: AppLocaleDto
    ): IsActivityRestartNeeded {
        appLocale = appLocaleDto.toAppLocale()
        return false
    }

    override suspend fun setNotificationPermissionCount(count: Int) {
        notificationPermissionCount = count
    }

    override suspend fun setSelectedRateAppOption(rateAppOptionDto: RateAppOptionDto) {
        selectedRateAppOption = rateAppOptionDto.toRateAppOption()
    }

    override suspend fun setPreviousRateAppRequestTimeInMs() {
        previousRateAppRequestTime = DateHelper.getCurrentTimeInMillis()
    }
}