package com.microsoft.azureandroid.data.util

import com.google.gson.*
import com.google.gson.JsonDeserializer
import com.microsoft.azureandroid.data.model.Resource
import java.util.*

/**
* Created by Nate Rickard on 11/10/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class JsonHelper {

    companion object {

        val Gson: Gson by lazy {

            val builder = GsonBuilder()

                    .disableHtmlEscaping()

//                    .registerTypeHierarchyAdapter(Resource::class.java, ResourceSerializer())

                    .registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, typeOfSrc, context ->

                        JsonPrimitive(src.time)
                    })

                    .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, typeOfT, context ->

                        if (json.asJsonPrimitive.isNumber) {
                            Date(json.asLong * 1000) //convert ticks since 1970 to Date
                        } else {
                            null
                        }
                    })

            if (ContextProvider.verboseLogging) {
                builder.setPrettyPrinting()
            }

            builder.create()
        }
    }
}