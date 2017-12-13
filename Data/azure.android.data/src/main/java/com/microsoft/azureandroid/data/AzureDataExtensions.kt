package com.microsoft.azureandroid.data

import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroid.data.model.Resource
import com.microsoft.azureandroid.data.services.DataResponse
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse

/**
* Created by Nate Rickard on 11/11/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

//region Database

//region Database -> Collections

// create
fun Database.createCollection (collectionId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {
    return AzureData.createCollection(collectionId, this.id, callback)
}

// get
fun Database.getCollection(collectionId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {
    return AzureData.getCollection(collectionId, this.id, callback)
}

// list
fun Database.getCollections(callback: (ResourceListResponse<DocumentCollection>) -> Unit) {
    return AzureData.getCollections(this.id, callback)
}

// delete
fun Database.deleteCollection(collection: DocumentCollection, callback: (DataResponse) -> Unit) {
    return AzureData.deleteCollection(collection, this.id, callback)
}

// delete
fun Database.deleteCollection(collectionId: String, callback: (DataResponse) -> Unit) {
    return AzureData.deleteCollection(collectionId, this.id, callback)
}

//endregion

//endregion

// DocumentCollection

fun <T : Document> DocumentCollection.getDocumentsAs(callback: (ResourceListResponse<T>) -> Unit) {
    return AzureData.getDocumentsAs(this, callback)
}

// Resource

fun <TResource : Resource> TResource.delete(callback: (DataResponse) -> Unit) =
        AzureData.delete(this, callback)

fun <TResource : Resource> TResource.refresh(callback: (ResourceResponse<TResource>) -> Unit) =
        AzureData.refresh(this, callback)