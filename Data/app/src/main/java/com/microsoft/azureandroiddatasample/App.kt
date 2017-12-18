package com.microsoft.azureandroiddatasample

import android.app.Application
import android.content.Context
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.constants.TokenType

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AzureData.configure(applicationContext, "mobile", "gioHmSqPP7J7FE5XlqRgBjmqykWLbm0KnP2FCAOl7gu17ZWlvMTRxOvsUYWQ3YUN2Yvmd077O0hyFyBOIftjOg==", TokenType.MASTER, true)

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            // Save the fact we crashed out.
            getSharedPreferences(TAG, Context.MODE_PRIVATE).edit()
                    .putBoolean(KEY_APP_CRASHED, true).apply()
            // Chain default exception handler.
            defaultHandler?.uncaughtException(thread, exception)
        }

        val bRestartAfterCrash = getSharedPreferences(TAG, Context.MODE_PRIVATE)
                .getBoolean(KEY_APP_CRASHED, false)
        if (bRestartAfterCrash) {
            // Clear crash flag.
            isRestartedFromCrash = true

            getSharedPreferences(TAG, Context.MODE_PRIVATE).edit()
                    .putBoolean(KEY_APP_CRASHED, false).apply()
        }
    }

    companion object {

        fun activityResumed() {
            isActivityVisible = true
        }

        fun activityPaused() {
            isActivityVisible = false
        }

        var isActivityVisible: Boolean = false
            private set

        private val TAG = "AppDelegate"
        private val KEY_APP_CRASHED = "KEY_APP_CRASHED"

        var isRestartedFromCrash = false
            private set

        fun clearIsRestartedFromCrash() {
            isRestartedFromCrash = false
        }
    }
}