package com.microsoft.azureandroid.data.model.contract

import com.google.gson.annotations.SerializedName

/**
 * Created by nater on 11/2/17.
 */

class DatabaseContract : BaseContract() {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("_self")
    var self: String? = null

    @SerializedName("_etag")
    var etag: String? = null

    @SerializedName("_colls")
    var colls: String? = null

    @SerializedName("_users")
    var users: String? = null

    @SerializedName("_ts")
    var ts: Int = 0
}