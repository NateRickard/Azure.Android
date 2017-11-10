package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by nater on 10/27/17.
 */

class Database : Resource() {

    @SerializedName("_colls")
    var collectionsLink: String? = null

    @SerializedName("_users")
    var usersLink: String? = null
}