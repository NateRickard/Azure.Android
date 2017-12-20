package com.microsoft.azureandroid.data.util.json

import com.google.gson.*
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.model.Timestamp
import com.microsoft.azureandroid.data.util.ContextProvider

/**
* Created by Nate Rickard on 11/10/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

val gson: Gson =
        GsonBuilder()
                .disableHtmlEscaping()
                .checkVerboseMode()
                .registerTypeAdapter(Timestamp::class.java, TimestampAdapter())
                .registerTypeAdapter(Document::class.java, DocumentAdapter())
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