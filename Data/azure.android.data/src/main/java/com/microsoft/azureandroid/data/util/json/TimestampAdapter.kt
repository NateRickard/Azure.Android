package com.microsoft.azureandroid.data.util.json

import com.google.gson.*
import com.microsoft.azureandroid.data.model.Timestamp
import java.lang.reflect.Type

/**
 * Created by Nate Rickard on 12/19/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

internal class TimestampAdapter: JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

    override fun serialize(src: Timestamp?, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {

        return if (src != null) {
            JsonPrimitive(src.time)
        }
        else {
            @Suppress("DEPRECATION")
            JsonNull()
        }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Timestamp? {

        return if (json.asJsonPrimitive.isNumber) {
            Timestamp(json.asLong * 1000) //convert ticks since 1970 to Date
        } else {
            null
        }
    }
}