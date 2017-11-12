package com.microsoft.azureandroid.data

import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroid.data.services.ResourceListResponse

/**
 * Created by nater on 11/11/17.
 */

class AzureDataExtensions {

    fun Database.getCollections(callback: (ResourceListResponse<DocumentCollection>) -> Unit) {
        return AzureData.instance.getCollectionsIn(this.id, callback)
    }

    fun<T: Document> DocumentCollection.getDocumentsAs(callback: (ResourceListResponse<T>) -> Unit) {
        return AzureData.instance.getDocumentsAs(this, callback)
    }
}