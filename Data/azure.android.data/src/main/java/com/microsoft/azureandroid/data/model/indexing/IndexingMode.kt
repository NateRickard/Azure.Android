package com.microsoft.azureandroid.data.model.indexing

import com.google.gson.annotations.SerializedName

/**
 * Created by Nate Rickard on 2/1/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
 */

/**
 * Specifies the supported indexing modes in the Azure Cosmos DB service.
 *
 * - consistent:   Index is updated synchronously with a create, update or delete operation.
 * - lazy:         Index is updated asynchronously with respect to a create, update or delete operation.
 * - none:         No index is provided.
 */
enum class IndexingMode {

    @SerializedName("consistent")
    Consistent,
    @SerializedName("lazy")
    Lazy,
    @SerializedName("none")
    None
}