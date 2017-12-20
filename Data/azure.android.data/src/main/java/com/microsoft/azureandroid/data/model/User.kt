package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

/**
 * Represents a user in the Azure Cosmos DB service.
 */
class User : Resource() {

    /**
     * Gets the self-link of the permissions associated with the user for the Azure Cosmos DB service.
     */
    @SerializedName(permissionsLinkKey)
    var permissionsLink: String? = null

    companion object {

        const val resourceName = "User"
        const val listName = "Users"

        const val permissionsLinkKey  = "_permissions"
    }
}