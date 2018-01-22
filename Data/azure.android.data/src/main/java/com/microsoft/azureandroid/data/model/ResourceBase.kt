package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Nate Rickard on 1/19/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
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