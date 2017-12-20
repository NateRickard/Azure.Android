package com.microsoft.azureandroid.data.util

import com.google.gson.*
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.model.DocumentDataMap
import java.lang.reflect.Type
import com.microsoft.azureandroid.data.model.Timestamp

/**
* Created by Nate Rickard on 11/10/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

val gson: Gson =

        GsonBuilder()

                .disableHtmlEscaping()

                .checkVerboseMode()

                .registerTypeAdapter(Timestamp::class.java, TimestampAdapter())

//                .registerTypeHierarchyAdapter()

                .registerTypeAdapter(Document::class.java, DocumentAdapter())

//                .registerTypeAdapterFactory(DocumentTypeAdapterFactory())

                .create()


fun GsonBuilder.checkVerboseMode() : GsonBuilder {

    if (ContextProvider.verboseLogging) {
        this.setPrettyPrinting()
    }

    return this
}

//private class DocumentAdapter : TypeAdapter<Document>() {
//
//    override fun write(out: JsonWriter, value: Document?) {
//
//        out.beginObject()
//
//        value?.let {
//
//            //system/Document values
//            out.name(Resource.idKey).value(value.id)
//            out.name(Resource.resourceIdKey).value(value.resourceId)
//            out.name(Resource.selfLinkKey).value(value.selfLink)
//            out.name(Resource.etagKey).value(value.etag)
//            out.name(Resource.timestampKey).value(value.timestamp.time)
//
//            out.name(Document.attachmentsLinkKey).value(value.attachmentsLink)
//
//            //user-defined values
//            for (item in value.data) {
//
//
//            }
//        }
//
//        out.endObject()
//    }
//
//    override fun read(`in`: JsonReader?): Document {
//
//        val doc = Document()
//
//        return doc
//    }
//}

//class DocumentTypeAdapterFactory : TypeAdapterFactory {
//
//    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>?): TypeAdapter<T> {
//
//        val delegate = gson.getDelegateAdapter(this, type)
//
//        return DocumentTypeAdapter()
//    }
//
//    private class DocumentTypeAdapter<T>() : TypeAdapter<T>() {
//
//        override fun write(out: JsonWriter?, value: Document?) {
//
//
//        }
//
//        override fun read(`in`: JsonReader?): Document {
//
//
//        }
//    }
//}

private class DocumentAdapter: JsonSerializer<Document>, JsonDeserializer<Document> {

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

private class TimestampAdapter: JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

    override fun serialize(src: Timestamp?, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {

        return if (src != null) {
            JsonPrimitive(src.time)
        }
        else {
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