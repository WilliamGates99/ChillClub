package com.xeniac.chillclub.core.feature_firebase_cloud_messaging.di.entrypoints

import android.app.NotificationManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NotificationManagerEntryPoint {
    val notificationManager: NotificationManager
}