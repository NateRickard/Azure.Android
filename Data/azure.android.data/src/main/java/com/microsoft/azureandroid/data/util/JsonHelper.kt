package com.microsoft.azureandroid.data.util

import com.google.gson.*
import com.google.gson.JsonDeserializer
import java.util.*
import com.google.gson.JsonPrimitive
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.microsoft.azureandroid.data.model.DocumentDataMap
import java.lang.reflect.Type
import java.text.NumberFormat
import java.text.ParsePosition

/**
* Created by Nate Rickard on 11/10/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

        val gson: Gson =

                GsonBuilder()

                        .disableHtmlEscaping()

                        .checkVerboseMode()

                        .registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, _, _ ->

                            JsonPrimitive(src.time)
                        })

                        .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, _, _ ->

                            if (json.asJsonPrimitive.isNumber) {
                                Date(json.asLong * 1000) //convert ticks since 1970 to Date
                            } else {
                                null
                            }
                        })

//                        .registerTypeAdapter(DocumentDataMap::class.java, MapDeserializer())

                        .create()


fun GsonBuilder.checkVerboseMode() : GsonBuilder {

    if (ContextProvider.verboseLogging) {
        this.setPrettyPrinting()
    }

    return this
}

// adapted from https://stackoverflow.com/questions/17090589/gson-deserialize-integers-as-integers-and-not-as-doubles
private class MapDeserializer : JsonDeserializer<DocumentDataMap> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DocumentDataMap {

//        val map = mutableMapOf<String, Any?>()
        val map = DocumentDataMap()
        val jo = json.asJsonObject

        for ((key, v) in jo.entrySet()) {

            if (v.isJsonArray) {

                map.put(key, gson.fromJson(v, List::class.java))

            } else if (v.isJsonPrimitive) {

                val prim = v.asJsonPrimitive

//                map.put(key, prim)

                if (prim.isNumber) {

                    map.put(key, prim.asNumber)

//                    var num: Number? = null
//                    val position = ParsePosition(0)
//                    val vString = v.asString
//
//                    try {
//                        num = NumberFormat.getInstance(Locale.ROOT).parse(vString, position)
//                    } catch (e: Exception) {
//                    }
//
//                    //Check if the position corresponds to the length of the string
//                    if (position.errorIndex < 0 && vString.length == position.index) {
//
//                        if (num != null) {
//                            map.put(key, num)
//                            continue
//                        }
//                    }
                } else if (prim.isString) {
                    map.put(key, prim.asString)
                } else if (prim.isBoolean) {
                    map.put(key, prim.asBoolean)
                } else {
                    map.put(key, null)
                }
            } else if (v.isJsonObject) {
                map.put(key, gson.fromJson(v, Map::class.java))
            }
        }

        return map
    }
}