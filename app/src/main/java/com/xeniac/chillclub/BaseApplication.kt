package com.xeniac.chillclub

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import com.xeniac.chillclub.core.presentation.common.ui.theme.PurpleNotificationLight
import com.xeniac.chillclub.core.presentation.common.utils.NetworkObserverHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), SingletonImageLoader.Factory {

    companion object {
        const val NOTIFICATION_CHANNEL_GROUP_ID_FCM = "group_fcm"
        const val NOTIFICATION_CHANNEL_ID_FCM_MISCELLANEOUS = "channel_fcm_miscellaneous"

        const val NOTIFICATION_CHANNEL_GROUP_ID_MUSIC_PLAYER = "group_music_player"
        const val NOTIFICATION_CHANNEL_ID_MUSIC_PLAYER_SERVICE = "channel_music_player_service"
    }

    @Inject
    lateinit var currentAppTheme: AppTheme

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        setAppTheme()
        observeNetworkConnection()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createFcmNotificationChannelGroup()
            createMiscellaneousFcmNotificationChannel()

            createMusicPlayerNotificationChannelGroup()
            createMusicPlayerServiceNotificationChannel()
        }
    }

    private fun setupTimber() = Timber.plant(Timber.DebugTree())

    private fun setAppTheme() = currentAppTheme.setAppTheme()

    private fun observeNetworkConnection() {
        NetworkObserverHelper.observeNetworkConnection(connectivityObserver)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createFcmNotificationChannelGroup() {
        val notificationChannelGroup = NotificationChannelGroup(
            /* id = */ NOTIFICATION_CHANNEL_GROUP_ID_FCM,
            /* name = */ getString(R.string.notification_fcm_channel_group_name)
        )

        notificationManager.createNotificationChannelGroup(notificationChannelGroup)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMiscellaneousFcmNotificationChannel() {
        val miscellaneousNotificationChannel = NotificationChannel(
            /* id = */ NOTIFICATION_CHANNEL_ID_FCM_MISCELLANEOUS,
            /* name = */ getString(R.string.notification_fcm_channel_name_miscellaneous),
            /* importance = */ NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            group = NOTIFICATION_CHANNEL_GROUP_ID_FCM
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            lightColor = PurpleNotificationLight.toArgb()
            enableLights(true)
        }

        notificationManager.createNotificationChannel(miscellaneousNotificationChannel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMusicPlayerNotificationChannelGroup() {
        val notificationChannelGroup = NotificationChannelGroup(
            /* id = */ NOTIFICATION_CHANNEL_GROUP_ID_MUSIC_PLAYER,
            /* name = */ getString(R.string.notification_player_channel_group_name)
        )

        notificationManager.createNotificationChannelGroup(notificationChannelGroup)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMusicPlayerServiceNotificationChannel() {
        val channel = NotificationChannel(
            /* id = */ NOTIFICATION_CHANNEL_ID_MUSIC_PLAYER_SERVICE,
            /* name = */ getString(R.string.notification_player_channel_name_service),
            /* importance = */ NotificationManager.IMPORTANCE_LOW
        ).apply {
            group = NOTIFICATION_CHANNEL_GROUP_ID_MUSIC_PLAYER
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            enableLights(false)
        }

        notificationManager.createNotificationChannel(channel)
    }

    override fun newImageLoader(
        context: PlatformContext
    ): ImageLoader = ImageLoader.Builder(context).apply {
        components {
            add(factory = SvgDecoder.Factory()) // SVGs
            when { // GIFs
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> add(factory = AnimatedImageDecoder.Factory())
                else -> add(factory = GifDecoder.Factory())
            }
        }

        crossfade(enable = true)

        networkCachePolicy(policy = CachePolicy.ENABLED)
        memoryCachePolicy(policy = CachePolicy.ENABLED)
        diskCachePolicy(policy = CachePolicy.ENABLED)

        memoryCache {
            MemoryCache.Builder().apply {
                maxSizePercent( // Set the max size to 25% of the app's available memory.
                    context = context,
                    percent = 0.25
                )
            }.build()
        }

        diskCache {
            DiskCache.Builder().apply {
                directory(cacheDir.resolve(relative = "image_cache")) // Set cache directory folder name
                maxSizePercent(percent = 0.03) // Set the max size to 3% of the device's free disk space.
            }.build()
        }

        if (BuildConfig.DEBUG) logger(logger = DebugLogger())
    }.build()
}