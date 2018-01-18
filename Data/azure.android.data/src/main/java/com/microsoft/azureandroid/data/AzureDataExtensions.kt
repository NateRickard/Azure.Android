package com.microsoft.azureandroid.data

import com.microsoft.azureandroid.data.model.*
import com.microsoft.azureandroid.data.services.DataResponse
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import okhttp3.HttpUrl
import java.net.URL

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


//region DocumentCollection

fun <T : Document> DocumentCollection.getDocuments(documentClass: Class<T>, callback: (ResourceListResponse<T>) -> Unit) {
    return AzureData.getDocuments(this, documentClass, callback)
}

//endregion


//region Document -> Attachment

// create
fun Document.createAttachment (attachmentId: String, contentType: String, mediaUrl: URL, callback: (ResourceResponse<Attachment>) -> Unit) {
    return AzureData.createAttachment(attachmentId, contentType, mediaUrl, this, callback)
}

// create
fun Document.createAttachment (attachmentId: String, contentType: String, mediaUrl: HttpUrl, callback: (ResourceResponse<Attachment>) -> Unit) {
    return AzureData.createAttachment(attachmentId, contentType, mediaUrl, this, callback)
}

// create
fun Document.createAttachment (attachmentId: String, contentType: String, mediaUrl: String, callback: (ResourceResponse<Attachment>) -> Unit) {
    return AzureData.createAttachment(attachmentId, contentType, mediaUrl, this, callback)
}

// create
fun Document.createAttachment (attachmentId: String, contentType: String, data: ByteArray, callback: (ResourceResponse<Attachment>) -> Unit) {
    return AzureData.createAttachment(attachmentId, contentType, attachmentId, data, this, callback)
}

// list
fun Document.getAttachments(callback: (ResourceListResponse<Attachment>) -> Unit) {
    return AzureData.getAttachments(this, callback)
}

// delete
fun Document.deleteAttachment(attachment: Attachment, callback: (DataResponse) -> Unit) {
    return AzureData.deleteAttachment(attachment, this, callback)
}

// delete
fun Document.deleteAttachment(attachmentRid: String, callback: (DataResponse) -> Unit) {
    return AzureData.deleteAttachment(attachmentRid, this, callback)
}

// replace
fun Document.replaceAttachment(attachmentId: String, attachmentRId: String, contentType: String, mediaUrl: URL, callback: (ResourceResponse<Attachment>) -> Unit) {
    return AzureData.replaceAttachment(attachmentId, attachmentRId, contentType, mediaUrl, this, callback)
}

// replace
fun Document.replaceAttachment(attachmentId: String, attachmentRId: String, contentType: String, mediaUrl: HttpUrl, callback: (ResourceResponse<Attachment>) -> Unit) {
    return AzureData.replaceAttachment(attachmentId, attachmentRId, contentType, mediaUrl, this, callback)
}

// replace
fun Document.replaceAttachment(attachmentId: String, attachmentRId: String, contentType: String, mediaUrl: String, callback: (ResourceResponse<Attachment>) -> Unit) {
    return AzureData.replaceAttachment(attachmentId, attachmentRId, contentType, mediaUrl, this, callback)
}

//endregion


// Resource

fun <TResource : Resource> TResource.delete(callback: (DataResponse) -> Unit) =
        AzureData.delete(this, callback)

fun <TResource : Resource> TResource.refresh(callback: (ResourceResponse<TResource>) -> Unit) =
        AzureData.refresh(this, callback)