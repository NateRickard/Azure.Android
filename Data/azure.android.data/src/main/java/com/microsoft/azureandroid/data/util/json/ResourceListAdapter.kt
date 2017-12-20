//package com.microsoft.azureandroid.data.util.json
//
//import com.google.gson.JsonDeserializationContext
//import com.google.gson.JsonDeserializer
//import com.google.gson.JsonElement
//import com.microsoft.azureandroid.data.model.Resource
//import com.microsoft.azureandroid.data.model.ResourceList
//import com.microsoft.azureandroid.data.model.ResourceType
//import java.lang.reflect.Type
//import com.google.gson.reflect.TypeToken.getParameterized
//import com.google.gson.reflect.TypeToken
//
//
///**
// * Created by Nate Rickard on 12/19/17.
// * Copyright © 2017 Nate Rickard. All rights reserved.
// */
//
//class ResourceListAdapter : JsonDeserializer<ResourceList<*>> {
//
//    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ResourceList<*>? {
//
////        val type = ResourceType.fromType<T>(typeOfT.javaClass)
//
//        val jsonObj = json.asJsonObject
//
//        val listJson = jsonObj.entrySet().find {
//            it.value.isJsonArray
//        }
//
//        listJson?.let {
//
//            val resourceType = ResourceType.fromListName(listJson.key)
//
//            val listType = TypeToken.getParameterized(List::class.java, resourceType.type).type
//            val resourceListType = TypeToken.getParameterized(ResourceList::class.java, resourceType.type)
//
//            val resourceList = resourceListType.rawType.newInstance() as ResourceList<*>
//
////            val list = listType.
//
////            val list = ResourceList()
//
//
////            gson.fromJson(listJson.value, resourceListType.type)
//
//            listJson.value.asJsonArray.forEach {
//                val item = context.deserialize(it, resourceType.type) as Resource
//
//                resourceList.items = listOf()
//            }
//
//            resourceList.items = listJson.value.asJsonArray.map {
//
//                context.deserialize(it, resourceType.type) as Resource
//            }
//
//            resourceList.resourceId = jsonObj.get(ResourceList.Companion.Keys.resourceIdKey).asString
//            resourceList.count = jsonObj.get(ResourceList.Companion.Keys.countKey).asInt
//
//            return resourceList
//        }
//
//        return null
//
////        for (item in jsonMap) {
////
//////            if (ResourceList.Companion.Keys.list.contains(item.key)) {
//////
//////            }
////
////            if (item.value.isJsonArray) {
////
////                val resourceType = ResourceType.fromListName(item.key)
////
////                break
////            }
////        }
//
////        val jsonObj = json.asJsonObject
//    }
//}