package com.microsoft.azureandroid.data

import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroid.data.services.DataResponse
import com.microsoft.azureandroid.data.services.ResourceListResponse

/**
* Created by Nate Rickard on 11/11/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class AzureDataExtensions {

    // Database

    fun Database.getCollections(callback: (ResourceListResponse<DocumentCollection>) -> Unit) {
        return AzureData.instance.getCollections(this.id, callback)
    }

    fun Database.deleteCollection(collection: DocumentCollection, callback: (DataResponse) -> Unit) {
        return AzureData.instance.deleteCollection(collection, this.id, callback)
    }

    fun Database.deleteCollection(collectionId: String, callback: (DataResponse) -> Unit) {
        return AzureData.instance.deleteCollection(collectionId, this.id, callback)
    }


    // DocumentCollection

    fun<T: Document> DocumentCollection.getDocumentsAs(callback: (ResourceListResponse<T>) -> Unit) {
        return AzureData.instance.getDocumentsAs(this, callback)
    }
}