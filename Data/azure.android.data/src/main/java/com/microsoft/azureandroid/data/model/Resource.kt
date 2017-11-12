package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by nater on 11/3/17.
 */

abstract class Resource(id: String? = UUID.randomUUID().toString()) {

    @SerializedName(idKey)
    var id: String = id!!

    @SerializedName(resourceIdKey)
    var resourceId: String = ""

    @SerializedName(selfLinkKey)    
    var selfLink: String? = null

    @SerializedName(etagKey)
    var etag: String? = null

    @SerializedName(timestampKey)
    var timestamp: Date? = null

    init {
    }

    open val dictionary: Map<String, Any>
        get() {
            return mapOf(
                    idKey to id,
                    resourceIdKey to resourceId,
                    selfLinkKey to (selfLink ?: ""),
                    etagKey to (etag ?: ""),
                    timestampKey to (timestamp?.time ?: 0)
            )
        }

    companion object {

        const val idKey =          "id"
        const val resourceIdKey =  "_rid"
        const val selfLinkKey =    "_self"
        const val etagKey =        "_etag"
        const val timestampKey =   "_ts"
    }
}