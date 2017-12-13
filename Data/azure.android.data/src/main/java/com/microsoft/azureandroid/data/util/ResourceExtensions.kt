package com.microsoft.azureandroid.data.util

import com.google.gson.reflect.TypeToken
import com.microsoft.azureandroid.data.model.Resource

/**
 * Created by Nate Rickard on 11/17/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class ResourceExtensions {

    fun Resource.toJson() : String =
            gson.toJson(this)

    fun<T: Resource> Resource.fromJson(json: String) : T =
            gson.fromJson(json, object : TypeToken<T>() {}.type)
}