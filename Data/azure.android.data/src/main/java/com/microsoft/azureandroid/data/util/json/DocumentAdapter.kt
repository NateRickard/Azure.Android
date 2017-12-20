package com.microsoft.azureandroid.data.util.json

import com.google.gson.*
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.model.DocumentDataMap
import com.microsoft.azureandroid.data.model.Timestamp
import java.lang.reflect.Type

/**
 * Created by Nate Rickard on 12/19/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

internal class DocumentAdapter: JsonSerializer<Document>, JsonDeserializer<Document> {

    override fun serialize(src: Document?, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {

        //serilize initial/system fields
//        val jsonDoc = context.serialize(src).asJsonObject
        val jsonDoc = localGson.toJsonTree(src).asJsonObject

        src?.let {

            val docData = context.serialize(src.data).asJsonObject

            //user-defined values
            for (item in docData.entrySet()) {

                item.value?.let {

                    jsonDoc.add(item.key, item.value)
                }
            }
        }

//        val jsonObj = JsonObject()
//
//        src?.let {
//
//            //system/Document values
//            jsonObj.addProperty(Resource.idKey, src.id)
//            jsonObj.addProperty(Resource.resourceIdKey, src.resourceId)
//            jsonObj.addProperty(Resource.selfLinkKey, src.selfLink)
//            jsonObj.addProperty(Resource.etagKey, src.etag)
//            jsonObj.addProperty(Resource.timestampKey, src.timestamp?.time)
//
//            jsonObj.addProperty(Document.attachmentsLinkKey, src.attachmentsLink)
//
//            val docData = context.serialize(src.data).asJsonObject
//
//            //user-defined values
//            for (item in docData.entrySet()) {
//
//                item.value?.let {
//
////                    val jsonMember = gson.toJsonTree(item.value)
//                    jsonObj.add(item.key, item.value)
//                }
//            }
//        }

        return jsonDoc
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Document? {

        val jsonObj = json.asJsonObject

        try {

//        val doc = context.deserialize<Document>(jsonObj, typeOfT)
            val doc = localGson.fromJson<Document>(jsonObj, typeOfT)

            //remove system keys
            Document.Companion.Keys.list.forEach {
                jsonObj.remove(it)
            }

            doc.data = context.deserialize<DocumentDataMap>(jsonObj, DocumentDataMap::class.java)

            return doc
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    companion object {

        val localGson: Gson = GsonBuilder()
                .disableHtmlEscaping()
                .checkVerboseMode()
                .registerTypeAdapter(Timestamp::class.java, TimestampAdapter())
                .create()
    }
}