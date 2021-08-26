package com.snechaev1.myroutes.utils

import com.snechaev1.myroutes.BuildConfig


object GlobalValues {
    const val SESSION = "user_session"
    const val DATABASE_NAME = "routes-db"
    const val OKHTTP_TIMEOUT = 60L
    const val AUTHORITY = BuildConfig.APPLICATION_ID + ".file_provider"

    //notification
    const val NOTIFICATION_ENABLED = "notification_enabled"
    const val NOTIFICATION = "notification"
    const val NOTIFICATION_STORE = "store"
    const val NOTIFICATION_TEXT = "text"

}