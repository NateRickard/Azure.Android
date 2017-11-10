package com.microsoft.azureandroid.data.model

/**
 * Created by nater on 11/1/17.
 */

class DocumentCollection(var id: String?, rid: String, var self: String?, var etag: String?, private var partitionKey: String?) {

    var rid: String? = null

    fun getPartitionKey(): String? {
        return this.partitionKey
    }

    fun setPartitionKey(colls: String) {
        this.partitionKey = partitionKey
    }
}