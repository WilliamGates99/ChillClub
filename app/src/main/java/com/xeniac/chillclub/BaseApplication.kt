package com.xeniac.chillclub

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.ui.theme.PurpleNotificationLight
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), ImageLoaderFactory {

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

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        setAppTheme()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createFcmNotificationChannelGroup()
            createMiscellaneousFcmNotificationChannel()

            createMusicPlayerNotificationChannelGroup()
            createMusicPlayerServiceNotificationChannel()
        }
    }

    private fun setupTimber() = Timber.plant(Timber.DebugTree())

    private fun setAppTheme() = currentAppTheme.setAppTheme()

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

    override fun newImageLoader(): ImageLoader = ImageLoader(context = this).newBuilder().apply {
        components {
            add(factory = SvgDecoder.Factory())
            add(
                factory = if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)) {
                    ImageDecoderDecoder.Factory()
                } else {
                    GifDecoder.Factory()
                }
            )
        }
        crossfade(enable = true)
        // Ignore the network cache headers and always read from/write to the disk cache.
        respectCacheHeaders(enable = false)
        memoryCachePolicy(policy = CachePolicy.ENABLED)
        diskCachePolicy(policy = CachePolicy.ENABLED)
        memoryCache {
            MemoryCache.Builder(this@BaseApplication)
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(percent = 0.25)
                .build()
        }
        diskCache {
            DiskCache.Builder()
                // Set cache directory folder name
                .directory(cacheDir.resolve("image_cache"))
                .maxSizePercent(percent = 0.03)
                .build()
        }
        okHttpClient {
            // Don't limit concurrent network requests by host.
            val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }

            // Lazily create the OkHttpClient that is used for network operations.
            OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .build()
        }
        if (BuildConfig.DEBUG) logger(logger = DebugLogger())
    }.build()
}