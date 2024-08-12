package com.xeniac.chillclub.core.presentation.utils

import androidx.test.espresso.IdlingRegistry

object EspressoUtils {

    /**
     * Use this method for solving the "Idling resource timed out" issue
     */
    fun solveIdlingResourceTimeout() {
        IdlingRegistry.getInstance().resources.forEach {
            if (it.name == "Compose-Espresso link") {
                IdlingRegistry.getInstance().unregister(it)
            }
        }
    }
}