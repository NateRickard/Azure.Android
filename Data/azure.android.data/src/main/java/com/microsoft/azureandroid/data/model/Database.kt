package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by nater on 10/27/17.
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