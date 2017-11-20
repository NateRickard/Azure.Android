package com.microsoft.azureandroid.data.util

import android.content.Context

/**
* Created by Nate Rickard on 11/3/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
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