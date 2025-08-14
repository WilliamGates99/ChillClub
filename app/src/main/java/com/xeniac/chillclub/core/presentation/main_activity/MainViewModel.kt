package com.xeniac.chillclub.core.presentation.main_activity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.chillclub.core.domain.use_cases.GetCurrentAppLocaleUseCase
import com.xeniac.chillclub.core.presentation.main_activity.states.MainState
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentAppLocaleUseCase: Lazy<GetCurrentAppLocaleUseCase>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = savedStateHandle.getMutableStateFlow(
        key = "mainState",
        initialValue = MainState()
    )
    val state = _state.onStart {
        getMainState()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 2.minutes),
        initialValue = _state.value
    )

    private fun getMainState() = viewModelScope.launch {
        _state.update {
            it.copy(
                currentAppLocale = getCurrentAppLocaleUseCase.get()(),
                isSplashScreenLoading = false
            )
        }
    }
}