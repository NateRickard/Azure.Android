package com.microsoft.azureandroid.data.model.contract

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

/**
 * Created by nater on 11/2/17.
 */

class DatabasesContract : BaseContract() {
    @SerializedName("Databases")
    var databases: ArrayList<DatabaseContract>? = null

    @SerializedName("_count")
    var count: Int = 0
}