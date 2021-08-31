package com.snechaev1.myroutes.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.snechaev1.myroutes.BaseApp
import com.snechaev1.myroutes.R
import com.snechaev1.myroutes.data.model.NotificationData
import java.util.*

object AppNotificationManager {

    val context = BaseApp.appContext()

    fun notify(body: String?, title: String?) {
        context?.let { context ->
            val notificationData = NotificationData().apply {
                this.title = title ?: context.getString(R.string.app_name)
                this.message = body ?: ""
//                this.action = action
            }
            val notification = handleNotification(context, notificationData)
            notify(notification)
        }
    }

//    fun getForegroundFragment(): String {
//        val navHostFragment = supportFragmentManager().findFragmentById(R.id.nav_host_fragment)
//        return navHostFragment.childFragmentManager.fragments[0].javaClass.name
//    }

    private fun handleNotification(context: Context, notificationData: NotificationData): Notification {
//        if (notificationData.action != null) {
//            localRepository.save(GlobalValues.NOTIFICATION, notificationData)
//        }
//        val intent = Intent(context, MainActivity::class.java)
//        intent.putExtra(GlobalValues.NOTIFICATION, notificationData)
//        val pendingIntent = PendingIntent.getActivity(context, generateNotificationId(), intent, 0)

        val color = ContextCompat.getColor(context, R.color.brand_prim_bg)
        val notificationBuilder = NotificationCompat.Builder(context, "channel_01")
                .setColor(color)
//                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(notificationData.title)
                .setContentText(notificationData.message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        return notificationBuilder.build()
    }

    private fun notify(notification: Notification) {
        context?.let { context ->
            val notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.app_name)
                val mChannel = NotificationChannel("channel_01", name, NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(mChannel)
            }
            notificationManager.notify(generateNotificationId(), notification)
        }
    }

    private fun generateNotificationId(): Int {
        val tmpTime = Date().time.toString()
        val notificationIdStringValue = tmpTime.substring(tmpTime.length - 5)
        return Integer.valueOf(notificationIdStringValue)
    }

}
