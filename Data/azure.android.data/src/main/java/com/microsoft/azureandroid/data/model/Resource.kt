package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
* Created by Nate Rickard on 11/3/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

/**
 * Represents a resource type in the Azure Cosmos DB service.
 * All Azure Cosmos DB resources, such as `Database`, `DocumentCollection`, and `Document` are derived from this class.
 */
abstract class Resource(id: String? = null) {

    /**
     * Gets or sets the Id of the resource in the Azure Cosmos DB service.
     */
    var id: String = id ?: UUID.randomUUID().toString()

    /**
     * Gets or sets the Resource Id associated with the resource in the Azure Cosmos DB service.
     */
    @SerializedName(resourceIdKey)
    var resourceId: String = ""

    /**
     * Gets the self-link associated with the resource from the Azure Cosmos DB service.
     */
    @SerializedName(selfLinkKey)
    var selfLink: String? = null

    /**
     * Gets the entity tag associated with the resource from the Azure Cosmos DB service.
     */
    @SerializedName(etagKey)
    var etag: String? = null

    /**
     * Gets the last modified timestamp associated with the resource from the Azure Cosmos DB service.
     */
    @SerializedName(timestampKey)
    var timestamp: Date? = null

    companion object {

        private const val idKey =   "id"
        const val resourceIdKey =   "_rid"
        const val selfLinkKey =     "_self"
        const val etagKey =         "_etag"
        const val timestampKey =    "_ts"

        val sysKeys = mutableListOf(idKey, resourceIdKey, selfLinkKey, etagKey, timestampKey)
    }
}