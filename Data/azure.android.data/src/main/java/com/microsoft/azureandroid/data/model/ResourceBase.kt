package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

abstract class ResourceBase {

    /**
     * Gets or sets the Resource Id associated with the resource in the Azure Cosmos DB service.
     */
    @SerializedName(resourceIdKey)
    var resourceId: String? = null

    companion object {

        const val resourceIdKey = "_rid"
    }
}