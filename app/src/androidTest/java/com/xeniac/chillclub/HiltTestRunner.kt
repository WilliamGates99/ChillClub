package com.xeniac.chillclub

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(
            /* cl = */ cl,
            /* className = */ HiltTestApplication::class.java.name,
            /* context = */ context
        )
    }
}