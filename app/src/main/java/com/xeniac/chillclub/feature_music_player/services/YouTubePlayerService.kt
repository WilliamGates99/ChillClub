package com.xeniac.chillclub.feature_music_player.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.xeniac.chillclub.BaseApplication.Companion.NOTIFICATION_CHANNEL_ID_MUSIC_PLAYER_SERVICE
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.main_activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YouTubePlayerService : Service() {

    companion object {
        const val EXTRAS_RADIO_STATION_CHANNEL_NAME = "radioStationChannelName"
        const val EXTRAS_RADIO_STATION_TITLE = "radioStationTitle"
    }

    enum class Actions {
        START_SERVICE,
        STOP_SERVICE
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        when (intent?.action) {
            Actions.START_SERVICE.toString() -> {
                val channelName = intent.getStringExtra(EXTRAS_RADIO_STATION_CHANNEL_NAME)
                val radioStationTitle = intent.getStringExtra(EXTRAS_RADIO_STATION_TITLE)

                start(
                    channelName = channelName,
                    radioStationTitle = radioStationTitle
                )
            }
            Actions.STOP_SERVICE.toString() -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(
        channelName: String?,
        radioStationTitle: String?
    ) {
        val notification = NotificationCompat.Builder(
            /* context = */ this,
            /* channelId = */ NOTIFICATION_CHANNEL_ID_MUSIC_PLAYER_SERVICE
        ).apply {
            val launchMainActivityIntent = Intent(
                /* packageContext = */ this@YouTubePlayerService,
                /* cls = */ MainActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            val openAppPendingIntent = PendingIntent.getActivity(
                /* context = */ this@YouTubePlayerService,
                /* requestCode = */ 0,
                /* intent = */ launchMainActivityIntent,
                /* flags = */ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    PendingIntent.FLAG_IMMUTABLE else 0
            )

            setAutoCancel(false)
            setOngoing(true)
            setContentIntent(openAppPendingIntent)
            setSmallIcon(R.drawable.ic_notification)
            setContentTitle(channelName)
            radioStationTitle?.let {
                setContentText(
                    getString(
                        R.string.music_player_notification_message,
                        radioStationTitle
                    )
                )
            }

            /*
            On Android 8.0 and above these values are ignored in favor of the values set on the notification's channel.
            On older platforms, these values are still used, so it is still required for apps supporting those platforms.
             */
            setPriority(NotificationCompat.PRIORITY_LOW)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSound(null)
            setVibrate(null)
        }.build()

        startForeground(
            /* id = */ 1001,
            /* notification = */ notification
        )
    }

    private fun stop() {
        stopSelf()
    }
}