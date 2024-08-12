package com.xeniac.chillclub.core.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.chillclub.core.domain.states.MainActivityState
import com.xeniac.chillclub.core.domain.use_cases.GetCurrentAppLocaleUseCase
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentAppLocaleUseCase: Lazy<GetCurrentAppLocaleUseCase>,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val mainState = savedStateHandle.getStateFlow(
        key = "mainState",
        initialValue = MainActivityState()
    )

    init {
        getMainState()
    }

    private fun getMainState() = viewModelScope.launch {
        savedStateHandle["mainState"] = mainState.value.copy(
            currentAppLocale = getCurrentAppLocaleUseCase.get()(),
            isSplashScreenLoading = false
        )
    }
}