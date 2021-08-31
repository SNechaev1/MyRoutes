package com.snechaev1.myroutes.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AppFirebaseMessagingService : FirebaseMessagingService() {
//    val context = BaseApp.appContext()

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(fcmPushToken: String) {
        Timber.d("Refreshed FCM token: $fcmPushToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            Timber.d("Notification title: ${it.title}, body: ${it.body}")
            AppNotificationManager.notify(it.body, it.title)
        }

        // Check if message contains a data payload.
        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            Timber.d("Notification data: $data")
            action(data)
        }
    }

    private fun action(action: Map<String, String>) {
        action.keys.forEach {
            when(it) {
                "message" -> {
                    Timber.d("action: ${action["message"]}")
                }
            }
        }
    }

}
