package com.microsoft.azureandroid.data.model

import com.google.gson.JsonObject
import com.microsoft.azureandroid.data.util.JsonHelper

/**
* Created by Nate Rickard on 11/3/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class ResourceList<T: Resource>(resourceType: ResourceType, json: JsonObject) {

    var resourceId: String? = null
    var count: Int? = null

    lateinit var items: List<T>

    init {
        resourceId  = json[resourceIdKey]?.asString
        count       = json[countKey]?.asInt

        json[resourceType.listName]?.asJsonArray?.let {

            items = it
                    .map {
                        JsonHelper.Gson.fromJson<T>(it, resourceType.type)
                    }
        }
    }

    val isPopuated: Boolean = resourceId != null

    companion object {

        const val resourceIdKey   = "_rid"
        const val countKey        = "_count"
    }
}