//package com.microsoft.azureandroid.data.util.json
//
//import com.google.gson.*
//import com.microsoft.azureandroid.data.model.Document
//import com.microsoft.azureandroid.data.model.DocumentDataMap
//import java.lang.reflect.Type
//
///**
// * Created by Nate Rickard on 12/21/17.
// * Copyright © 2017 Nate Rickard. All rights reserved.
// */
//internal class DocumentDataMapAdapter : JsonSerializer<DocumentDataMap>, JsonDeserializer<DocumentDataMap> {
//
//    override fun serialize(src: DocumentDataMap?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
//
//        src?.let {
//
//            val docData = context.serialize(src).asJsonObject
//
//            //user-defined values
//            for (item in docData.entrySet()) {
//
//                item.value?.let {
//
//                    jsonDoc.add(item.key, item.value)
//                }
//            }
//        }
//    }
//
//    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DocumentDataMap {
//
//
//    }
//}