package com.microsoft.azureandroid.data.model.contract

/**
 * Created by nater on 11/2/17.
 */

class DocumentCollectionContract {
    var id: String? = null

    var indexingPolicy: IndexingPolicyContract? = null

    var partitionKey: PartitionKeyContract? = null

    var rid: String? = null

    var ts: Int = 0

    var self: String? = null

    var etag: String? = null

    var docs: String? = null

    var sprocs: String? = null

    var triggers: String? = null

    var udfs: String? = null

    var conflicts: String? = null
}