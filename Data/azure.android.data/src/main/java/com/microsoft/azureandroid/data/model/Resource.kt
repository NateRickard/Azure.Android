package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
* Created by Nate Rickard on 11/3/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

abstract class Resource(id: String? = null) {

    lateinit var id: String

    @SerializedName(resourceIdKey)
    var resourceId: String = ""

    @SerializedName(selfLinkKey)
    var selfLink: String? = null

    @SerializedName(etagKey)
    var etag: String? = null

    @SerializedName(timestampKey)
    var timestamp: Date? = null

    init {
        this.id = id ?: UUID.randomUUID().toString()
    }

    companion object {

        private const val idKey =   "id"
        const val resourceIdKey =   "_rid"
        const val selfLinkKey =     "_self"
        const val etagKey =         "_etag"
        const val timestampKey =    "_ts"

        val sysKeys = mutableListOf(idKey, resourceIdKey, selfLinkKey, etagKey, timestampKey)
    }
}