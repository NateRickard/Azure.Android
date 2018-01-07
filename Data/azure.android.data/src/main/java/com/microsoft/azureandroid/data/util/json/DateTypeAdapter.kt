package com.microsoft.azureandroid.data.util.json

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.microsoft.azureandroid.data.util.RoundtripDateConverter
import java.util.*

/**
 * Created by Nate Rickard on 12/21/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

internal class DateTypeAdapter : TypeAdapter<Date>() {

    override fun read(`in`: JsonReader?): Date? {

        val dateString = `in`?.nextString()

        return RoundtripDateConverter.toDate(dateString)
    }

    override fun write(out: JsonWriter, value: Date?) {

        out.value(RoundtripDateConverter.toString(value))
    }
}