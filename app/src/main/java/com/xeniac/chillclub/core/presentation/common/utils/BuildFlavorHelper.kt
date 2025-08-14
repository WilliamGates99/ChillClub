package com.xeniac.chillclub.core.presentation.common.utils

import com.xeniac.chillclub.BuildConfig

fun isAppInstalledFromPlayStore() = when (BuildConfig.FLAVOR_market) {
    "playStore" -> true
    else -> false
}

fun isAppInstalledFromGitHub() = when (BuildConfig.FLAVOR_market) {
    "gitHub" -> true
    else -> false
}