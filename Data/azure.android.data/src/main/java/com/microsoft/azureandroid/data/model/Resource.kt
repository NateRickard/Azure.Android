package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
* Created by Nate Rickard on 11/3/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

abstract class Resource(id: String? = UUID.randomUUID().toString()) {

    var id: String = id!!

    @SerializedName(resourceIdKey)
    var resourceId: String = ""

    @SerializedName(selfLinkKey)    
    var selfLink: String? = null

    @SerializedName(etagKey)
    var etag: String? = null

    @SerializedName(timestampKey)
    var timestamp: Date? = null

    companion object {

        const val resourceIdKey =  "_rid"
        const val selfLinkKey =    "_self"
        const val etagKey =        "_etag"
        const val timestampKey =   "_ts"
    }
}