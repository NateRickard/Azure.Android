package com.microsoft.azureandroid.data.util

import com.google.gson.*
import com.microsoft.azureandroid.data.model.Resource
import java.lang.reflect.Type

/**
 * Created by Nate Rickard on 11/20/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class ResourceSerializer : JsonSerializer<Resource> {

    override fun serialize(resource: Resource, type: Type, jsc: JsonSerializationContext): JsonElement {

        val gson = Gson()
        val jObj = gson.toJsonTree(resource) as JsonObject

        if (resource.resourceId.isBlank()) {
            jObj.remove(Resource.resourceIdKey)
        }

        return jObj
    }
}