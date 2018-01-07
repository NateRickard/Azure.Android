package com.microsoft.azureandroid.data.util.json

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.microsoft.azureandroid.data.model.Resource
import com.microsoft.azureandroid.data.model.ResourceList

import java.lang.reflect.ParameterizedType
import java.util.ArrayList

/**
 * Created by Nate Rickard on 12/22/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

//inline fun <reified T:Any> foo() = T::class.java

class ResourceListAdapterFactory : TypeAdapterFactory {

//    @SuppressWarnings("unchecked", "rawtypes")
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {

        var typeAdapter: TypeAdapter<T>? = null

    println("*** ASKED for type: ${type.toString()}")

//        TypeToken.getParameterized(ResourceList::class.java, resourceType.type).type

//        try {
//            if (type.rawType == ResourceList::class.java) {
//
////                typeAdapter = ResourceListAdapter(
////                        (type.type as ParameterizedType)
////                                .actualTypeArguments.first()) as TypeAdapter<T>
//
//
//                typeAdapter = ResourceListAdapter(
//                        (type.type as ParameterizedType)
//                                .actualTypeArguments.first()) as TypeAdapter<T>
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        return typeAdapter

//        return create<T>(gson)
    }
}

//inline fun <reified T: Any> typeRef(): Class<T> = object: Class<T>(){}

//    inline fun <reified T> create(gson: Gson): TypeAdapter<T>? {
//        return when {
//            T::class == ResourceList::class -> {
//                val adapter: TypeAdapter<AppState> = MyAdapter()
//
//                /* this is reported as unchecked */
//                return adapter as TypeAdapter<T>
//            }
//            else -> null
//        }
//    }