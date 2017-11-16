package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
* Created by Nate Rickard on 10/27/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class Database : Resource() {

    @SerializedName(collectionsLinkKey)
    var collectionsLink: String? = null

    @SerializedName(usersLinkKey)
    var usersLink: String? = null

    companion object {

        const val collectionsLinkKey    = "_colls"
        const val usersLinkKey          = "_users"
    }
}