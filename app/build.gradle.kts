@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
    alias(libs.plugins.google.services) // Google Services plugin
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
}

val properties = gradleLocalProperties(rootDir, providers)

android {
    namespace = "com.xeniac.chillclub"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        applicationId = "com.xeniac.chillclub"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        // Keeps language resources for only the locales specified below.
        resourceConfigurations.addAll(listOf("en-rUS"))

        testInstrumentationRunner = "com.xeniac.chillclub.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            type = "String",
            name = "KTOR_HTTP_BASE_URL",
            value = properties.getProperty("KTOR_HTTP_BASE_URL")
        )
    }

    signingConfigs {
        create("release") {
            storeFile = file(path = properties.getProperty("KEY_STORE_PATH"))
            storePassword = properties.getProperty("KEY_STORE_PASSWORD")
            keyAlias = properties.getProperty("KEY_ALIAS")
            keyPassword = properties.getProperty("KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = " - debug"
            applicationIdSuffix = ".debug"

            resValue(
                type = "color",
                name = "appIconBackground",
                value = "#FFFF9100" // Orange
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")

            ndk.debugSymbolLevel = "FULL" // Include native debug symbols file in app bundle
        }
    }

    flavorDimensions += listOf("build", "market")
    productFlavors {
        create("dev") {
            dimension = "build"
            versionNameSuffix = " - Developer Preview"
            applicationIdSuffix = ".dev"
            isDefault = true

            resValue(
                type = "color",
                name = "appIconBackground",
                value = "#FFFF0011" // Red
            )
        }

        create("prod") {
            dimension = "build"

            resValue(
                type = "color",
                name = "appIconBackground",
                value = "#FF0C160D" // Dark Green // TODO: UPDATE ICON BACKGROUND COLOR
            )
        }

        create("playStore") {
            dimension = "market"
            isDefault = true

            buildConfigField(
                type = "String",
                name = "URL_APP_STORE",
                value = "\"https://play.google.com/store/apps/details?id=com.xeniac.chillclubt\""
            )

            buildConfigField(
                type = "String",
                name = "PACKAGE_NAME_APP_STORE",
                value = "\"com.android.vending\""
            )
        }

        create("gitHub") {
            dimension = "market"

            buildConfigField(
                type = "String",
                name = "URL_APP_STORE",
                value = "\"https://github.com/WilliamGates99/FUTSale\""
            )

            buildConfigField(
                type = "String",
                name = "PACKAGE_NAME_APP_STORE",
                value = "\"\""
            )
        }

        create("cafeBazaar") {
            dimension = "market"

            buildConfigField(
                type = "String",
                name = "URL_APP_STORE",
                value = "\"https://cafebazaar.ir/app/com.xeniac.chillclubt\""
            )

            buildConfigField(
                type = "String",
                name = "PACKAGE_NAME_APP_STORE",
                value = "\"com.farsitel.bazaar\""
            )
        }

        create("myket") {
            dimension = "market"

            buildConfigField(
                type = "String",
                name = "URL_APP_STORE",
                value = "\"https://myket.ir/app/com.xeniac.chillclubt\""
            )

            buildConfigField(
                type = "String",
                name = "PACKAGE_NAME_APP_STORE",
                value = "\"ir.mservices.market\""
            )
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        // Java 8+ API Desugaring Support
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }

    kotlinOptions {
        jvmTarget = "22"
    }

    room {
        schemaDirectory("$projectDir/roomSchemas")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    bundle {
        language {
            /*
            Specifies that the app bundle should not support configuration APKs for language resources.
            These resources are instead packaged with each base and dynamic feature APK.
             */
            enableSplit = false
        }
    }
}

hilt {
    enableAggregatingTask = true
}

androidComponents {
    beforeVariants { variantBuilder ->
        // Gradle ignores any variants that satisfy the conditions below.
        if (variantBuilder.buildType == "debug") {
            variantBuilder.productFlavors.let {
                variantBuilder.enable = when {
                    it.containsAll(listOf("build" to "dev", "market" to "gitHub")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "cafeBazaar")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "myket")) -> false
                    it.containsAll(listOf("build" to "prod")) -> false
                    else -> true
                }
            }
        }

        if (variantBuilder.buildType == "release") {
            variantBuilder.productFlavors.let {
                variantBuilder.enable = when {
                    it.containsAll(listOf("build" to "dev")) -> false
                    else -> true
                }
            }
        }
    }
}

dependencies {
    // Java 8+ API Desugaring Support
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.core.splashscreen)
    implementation(libs.kotlinx.serialization.json) // Kotlin JSON Serialization Library
    implementation(libs.kotlinx.datetime) // Kotlin DateTime

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3) // Material Design 3
    implementation(libs.compose.runtime.livedata) // Compose Integration with LiveData
    implementation(libs.compose.ui.tooling.preview) // Android Studio Compose Preview Support
    debugImplementation(libs.compose.ui.tooling) // Android Studio Compose Preview Support
    implementation(libs.activity.compose) // Compose Integration with Activities
    implementation(libs.constraintlayout.compose) // Compose Constraint Layout
    implementation(libs.navigation.compose) // Compose Navigation
    implementation(libs.hilt.navigation.compose) // Compose Navigation Integration with Hilt

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Architectural Components
    implementation(libs.lifecycle.viewmodel.ktx) // ViewModel
    implementation(libs.lifecycle.viewmodel.compose) // ViewModel Utilities for Compose
    implementation(libs.lifecycle.runtime.ktx) // Lifecycles Only (without ViewModel or LiveData)
    implementation(libs.lifecycle.runtime.compose) // Lifecycle Utilities for Compose

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Coroutines Support for Firebase
    implementation(libs.kotlinx.coroutines.play.services)

    // Ktor Client Library
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp) // Ktor OkHttp Engine
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

    // Room Library
    implementation(libs.room.runtime)
    implementation(libs.room.ktx) // Kotlin Extensions and Coroutines support for Room
    ksp(libs.room.compiler)

    // Preferences DataStore
    implementation(libs.datastore.preferences)

    // Firebase BoM and Analytics
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)

    // Firebase Cloud Messaging
    implementation(libs.firebase.messaging.ktx)

    // Firebase Release & Monitor
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.perf.ktx)

    // In-App Browser
    implementation(libs.browser)

    // Timber Library
    implementation(libs.timber)

    // Lottie Library
    implementation(libs.lottie.compose)

    // Coil Library
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.coil.gif)

    // Google Play In-App Reviews API
    implementation(libs.play.review.ktx)

    // Google Play In-App Reviews API
    implementation(libs.play.review.ktx)

    // Google Play In-App Updates API
    implementation(libs.play.app.update.ktx)

    // Local Unit Test Libraries
    testImplementation(libs.truth)
    testImplementation(libs.junit)
    testImplementation(libs.arch.core.testing) // Test Helpers for Architectural Components
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.ktor.client.mock)

    // Instrumentation Test Libraries
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.test.ext.junit) // JUnit Extension for Android Test
    androidTestImplementation(libs.arch.core.testing) // Test Helpers for Architectural Components
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.test.core)
    androidTestImplementation(libs.test.runner) // Android JUnit4 Test Runner
    androidTestImplementation(libs.test.rules) // Android JUnit Test Rules
    androidTestImplementation(libs.ktor.client.mock)
    androidTestImplementation(libs.room.testing)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

    // UI Test Libraries
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.intents)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
}

val releaseRootDir = "${rootDir}/app"
val apkDestDir: String = properties.getProperty("APK_DESTINATION_DIR")
val bundleDestDir: String = properties.getProperty("BUNDLE_DESTINATION_DIR")
val obfuscationDestDir: String = properties.getProperty("OBFUSCATION_DESTINATION_DIR")

val versionName = "${android.defaultConfig.versionName}"
val renamedFileName = "FUTSale $versionName"

tasks.register<Copy>("copyDevPreviewBundle") {
    val bundleFile = "app-dev-playStore-release.aab"
    val bundleSourceDir = "${releaseRootDir}/devPlayStore/release/${bundleFile}"

    from(bundleSourceDir)
    into(bundleDestDir)

    rename(bundleFile, "$renamedFileName (Developer Preview).aab")
}

tasks.register<Copy>("copyDevPreviewApk") {
    val apkFile = "app-dev-playStore-release.apk"
    val apkSourceDir = "${releaseRootDir}/devPlayStore/release/${apkFile}"

    from(apkSourceDir)
    into(apkDestDir)

    rename(apkFile, "$renamedFileName (Developer Preview).aab")
}

tasks.register<Copy>("copyReleaseApk") {
    val gitHubApkFile = "app-prod-gitHub-release.apk"
    val cafeBazaarApkFile = "app-prod-cafeBazaar-release.apk"
    val myketApkFile = "app-prod-myket-release.apk"

    val gitHubApkSourceDir = "${releaseRootDir}/prodGitHub/release/${gitHubApkFile}"
    val cafeBazaarApkSourceDir = "${releaseRootDir}/prodCafeBazaar/release/${cafeBazaarApkFile}"
    val myketApkSourceDir = "${releaseRootDir}/prodMyket/release/${myketApkFile}"

    from(gitHubApkSourceDir)
    into(apkDestDir)

    from(cafeBazaarApkSourceDir)
    into(apkDestDir)

    from(myketApkSourceDir)
    into(apkDestDir)

    rename(gitHubApkFile, "$renamedFileName - GitHub.apk")
    rename(cafeBazaarApkFile, "$renamedFileName - CafeBazaar.apk")
    rename(myketApkFile, "$renamedFileName - Myket.apk")
}

tasks.register<Copy>("copyReleaseBundle") {
    val playStoreBundleFile = "app-prod-playStore-release.aab"
    val playStoreBundleSourceDir =
        "${releaseRootDir}/prodPlayStore/release/${playStoreBundleFile}"

    from(playStoreBundleSourceDir)
    into(bundleDestDir)

    rename(playStoreBundleFile, "${renamedFileName}.aab")
}

tasks.register<Copy>("copyObfuscationFolder") {
    val obfuscationSourceDir = "${rootDir}/app/obfuscation"

    from(obfuscationSourceDir)
    into(obfuscationDestDir)
}

tasks.register("copyReleaseFiles") {
    dependsOn("copyReleaseApk", "copyReleaseBundle", "copyObfuscationFolder")
}