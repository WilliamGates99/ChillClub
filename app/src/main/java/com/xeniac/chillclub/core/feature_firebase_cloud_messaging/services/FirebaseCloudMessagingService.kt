package com.xeniac.chillclub.core.feature_firebase_cloud_messaging.services

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.xeniac.chillclub.BaseApplication
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.feature_firebase_cloud_messaging.di.entrypoints.requireNotificationManager
import com.xeniac.chillclub.core.feature_firebase_cloud_messaging.services.utils.getBitmapFromUrl
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.random.Random

@AndroidEntryPoint
class FirebaseCloudMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.i("New firebase registration token generated.")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val cancelNotificationPendingIntent = PendingIntent.getActivity(
            /* context = */ this,
            /* requestCode = */ 0,
            /* intent = */ Intent(/* action = */ ""),
            /* flags = */ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(
            /* context = */ this,
            /* channelId = */ BaseApplication.NOTIFICATION_CHANNEL_ID_FCM_MISCELLANEOUS
        ).apply {
            setAutoCancel(true)
            setContentIntent(cancelNotificationPendingIntent)
            setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: CHANGE NOTIFICATION ICON
            setContentTitle(message.notification?.title)
            setContentText(message.notification?.body)

            setStyle(NotificationCompat.BigTextStyle().bigText(message.notification?.body))

            message.notification?.imageUrl?.let { imageUrl ->
                val bitmap = getBitmapFromUrl(imageUrl.toString())
                setLargeIcon(bitmap)
                setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null as Bitmap?)
                )
            }

            /*
            On Android 8.0 and above these values are ignored in favor of the values set on the notification's channel.
            On older platforms, these values are still used, so it is still required for apps supporting those platforms.
            */
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setLights(
                /* argb = */  Color.Magenta.toArgb(), // TODO: CHANGE COLOR BASED ON APP ICON
                /* onMs = */ 1000,
                /* offMs = */ 1000
            )
            setVibrate(null)
        }.build()

        requireNotificationManager(context = this).notify(
            /* id = */ Random.nextInt(),
            /* notification = */ notification
        )
    }
}