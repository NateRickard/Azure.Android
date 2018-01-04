package com.microsoft.azureandroid.data.util.json

import com.google.gson.*
import com.microsoft.azureandroid.data.model.*
import com.microsoft.azureandroid.data.util.ContextProvider
import java.util.*

/**
* Created by Nate Rickard on 11/10/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

val gson: Gson =
        GsonBuilder()
                .disableHtmlEscaping()
                .checkVerboseMode()
                .registerTypeAdapter(Date::class.java, DateTypeAdapter())
                .registerTypeAdapter(Timestamp::class.java, TimestampAdapter())
                .registerTypeAdapter(DictionaryDocument::class.java, DocumentAdapter())
                .create()


fun GsonBuilder.checkVerboseMode() : GsonBuilder {

    if (ContextProvider.verboseLogging) {
        this.setPrettyPrinting()
    }

    return this
}