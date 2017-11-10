package com.microsoft.azureandroid.data.util

import com.google.gson.*
import com.google.gson.JsonDeserializer
import java.util.*

/**
 * Created by nater on 11/10/17.
 */

class JsonHelper {

    companion object {

        val Gson: Gson by lazy {

            val builder = GsonBuilder()

            .registerTypeAdapter(Date::class.java, JsonSerializer<Date> {
                src, typeOfSrc, context ->

                JsonPrimitive(src.time)
            })

            .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> {
                json, typeOfT, context ->

                if (json.asJsonPrimitive.isNumber) {
                    Date(json.asLong * 1000) //convert ticks since 1970 to Date
                }
                else {
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