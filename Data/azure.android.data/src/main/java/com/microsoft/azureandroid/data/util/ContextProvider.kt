package com.microsoft.azureandroid.data.util

import android.content.Context

/**
 * Created by nater on 11/3/17.
 */

class ContextProvider {

    companion object {

        lateinit var appContext: Context
        var verboseLogging: Boolean = false

        fun init(context: Context, verboseLogging: Boolean = false) {
            this.appContext = context
            this.verboseLogging = verboseLogging
        }
    }
}