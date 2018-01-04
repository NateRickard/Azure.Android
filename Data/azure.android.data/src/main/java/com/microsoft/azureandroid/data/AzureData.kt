package com.microsoft.azureandroid.data

import android.content.Context
import com.microsoft.azureandroid.data.constants.TokenType
import com.microsoft.azureandroid.data.model.*
import com.microsoft.azureandroid.data.services.DataResponse
import com.microsoft.azureandroid.data.services.DocumentClient
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroid.data.util.ContextProvider
import okhttp3.HttpUrl
import java.net.URL

/**
* Created by Nate Rickard on 10/24/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class AzureData {

    companion object {

        lateinit var baseUri: ResourceUri
        lateinit var documentClient: DocumentClient

        @JvmStatic
        fun configure(context: Context, name: String, key: String, keyType: TokenType = TokenType.MASTER, verboseLogging: Boolean = false) {

            ContextProvider.init(context.applicationContext, verboseLogging)

            baseUri = ResourceUri(name)
            documentClient = DocumentClient(baseUri, key, keyType)

            isConfigured = true
        }

        @JvmStatic
        var isConfigured: Boolean = false
            private set

        //region Databases

        // create
        @JvmStatic
        fun createDatabase(databaseId: String, callback: (ResourceResponse<Database>) -> Unit) =
                documentClient.createDatabase(databaseId, callback)

        // list
        @JvmStatic
        fun getDatabases(callback: (ResourceListResponse<Database>) -> Unit) =
                documentClient.databases(callback)

        // get
        @JvmStatic
        fun getDatabase(databaseId: String, callback: (ResourceResponse<Database>) -> Unit) =
                documentClient.getDatabase(databaseId, callback)

        // delete
        @JvmStatic
        fun deleteDatabase(database: Database, callback: (DataResponse) -> Unit) =
                documentClient.deleteDatabase(database.id, callback)

        // delete
        @JvmStatic
        fun deleteDatabase(databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteDatabase(databaseId, callback)

        //endregion

        //region Collections

        // create
        @JvmStatic
        fun createCollection(collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) =
                documentClient.createCollection(collectionId, databaseId, callback)

        // list
        @JvmStatic
        fun getCollections(databaseId: String, callback: (ResourceListResponse<DocumentCollection>) -> Unit) =
                documentClient.getCollectionsIn(databaseId, callback)

        // get
        @JvmStatic
        fun getCollection(collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) =
                documentClient.getCollection(collectionId, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteCollection(collection: DocumentCollection, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteCollection(collection.id, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteCollection(collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteCollection(collectionId, databaseId, callback)

        //endregion

        //region Documents

        // create
        @JvmStatic
        fun <T : Document> createDocument(document: T, collectionId: String, databaseId: String, callback: (ResourceResponse<T>) -> Unit) =
                documentClient.createDocument(document, collectionId, databaseId, callback)

        // create
        @JvmStatic
        fun <T : Document> createDocument(document: T, collection: DocumentCollection, callback: (ResourceResponse<T>) -> Unit) =
                documentClient.createDocument(document, collection, callback)

        // list
        @JvmStatic
        fun <T : Document> getDocuments(collectionId: String, databaseId: String, documentClass: Class<T>, callback: (ResourceListResponse<T>) -> Unit) =
                documentClient.getDocumentsAs(collectionId, databaseId, documentClass, callback)

        // list
        @JvmStatic
        fun <T : Document> getDocuments(collection: DocumentCollection, documentClass: Class<T>, callback: (ResourceListResponse<T>) -> Unit) =
                documentClient.getDocumentsAs(collection, documentClass, callback)

        // get
        @JvmStatic
        fun <T : Document> getDocument(documentId: String, collectionId: String, databaseId: String, documentClass: Class<T>, callback: (ResourceResponse<T>) -> Unit) =
                documentClient.getDocument(documentId, collectionId, databaseId, documentClass, callback)

        // delete
        @JvmStatic
        fun deleteDocument(documentId: String, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteDocument(documentId, collectionId, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteDocument(document: Document, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteDocument(document.id, collectionId, databaseId, callback)

        // query
        @JvmStatic
        fun <T : Document> queryDocuments(collectionId: String, databaseId: String, query: Query, documentClass: Class<T>, callback: (ResourceListResponse<T>) -> Unit) =
                documentClient.queryDocuments(collectionId, databaseId, query, documentClass, callback)

        // query
        @JvmStatic
        fun <T : Document> queryDocuments(collection: DocumentCollection, query: Query, documentClass: Class<T>, callback: (ResourceListResponse<T>) -> Unit) =
                documentClient.queryDocuments(collection, query, documentClass, callback)

        //endregion

        //region Attachments

        // create
        @JvmStatic
        fun createAttachment(attachmentId: String, contentType: String, mediaUrl: HttpUrl, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, mediaUrl, documentId, collectionId, databaseId, callback)

        // create
        @JvmStatic
        fun createAttachment(attachmentId: String, contentType: String, mediaUrl: String, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, HttpUrl.parse(mediaUrl)!!, documentId, collectionId, databaseId, callback)

        // create
        @JvmStatic
        fun createAttachment(attachmentId: String, contentType: String, mediaUrl: URL, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, HttpUrl.get(mediaUrl)!!, documentId, collectionId, databaseId, callback)

        // create
        @JvmStatic
        fun createAttachment(attachmentId: String, contentType: String, mediaName: String, media: ByteArray, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, mediaName, media, documentId, collectionId, databaseId, callback)

        // create
        @JvmStatic
        fun createAttachment(attachmentId: String, contentType: String, mediaUrl: HttpUrl, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, mediaUrl, document, callback)

        // create
        @JvmStatic
        fun createAttachment(attachmentId: String, contentType: String, mediaUrl: String, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, HttpUrl.parse(mediaUrl)!!, document, callback)

        // create
        @JvmStatic
        fun createAttachment(attachmentId: String, contentType: String, mediaUrl: URL, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, HttpUrl.get(mediaUrl)!!, document, callback)

        // create
        @JvmStatic
        fun createAttachment(attachmentId: String, contentType: String, mediaName: String, media: ByteArray, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, mediaName, media, document, callback)

        // list
        @JvmStatic
        fun getAttachments(documentId: String, collectionId: String, databaseId: String, callback: (ResourceListResponse<Attachment>) -> Unit) =
                documentClient.getAttachments(documentId, collectionId, databaseId, callback)

        // list
        @JvmStatic
        fun getAttachments(document: Document, callback: (ResourceListResponse<Attachment>) -> Unit) =
                documentClient.getAttachments(document, callback)

        // delete
        @JvmStatic
        fun deleteAttachment(attachment: Attachment, documentId: String, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteAttachment(attachment, documentId, collectionId, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteAttachment(attachment: Attachment, document: Document, callback: (DataResponse) -> Unit) =
                documentClient.deleteAttachment(attachment, document, callback)

        // replace
        @JvmStatic
        fun replaceAttachment(attachmentId: String, contentType: String, mediaUrl: HttpUrl, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, mediaUrl, documentId, collectionId, databaseId, callback)

        // replace
        @JvmStatic
        fun replaceAttachment(attachmentId: String, contentType: String, mediaUrl: String, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, HttpUrl.parse(mediaUrl)!!, documentId, collectionId, databaseId, callback)

        // replace
        @JvmStatic
        fun replaceAttachment(attachmentId: String, contentType: String, mediaUrl: URL, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, HttpUrl.get(mediaUrl)!!, documentId, collectionId, databaseId, callback)

        // replace
        @JvmStatic
        fun replaceAttachment(attachmentId: String, contentType: String, mediaName: String, media: ByteArray, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, mediaName, media, documentId, collectionId, databaseId, callback)

        // replace
        @JvmStatic
        fun replaceAttachment(attachmentId: String, contentType: String, mediaUrl: HttpUrl, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, mediaUrl, document, callback)

        // replace
        @JvmStatic
        fun replaceAttachment(attachmentId: String, contentType: String, mediaUrl: String, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, HttpUrl.parse(mediaUrl)!!, document, callback)

        // replace
        @JvmStatic
        fun replaceAttachment(attachmentId: String, contentType: String, mediaUrl: URL, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, HttpUrl.get(mediaUrl)!!, document, callback)

        // replace
        @JvmStatic
        fun replaceAttachment(attachmentId: String, contentType: String, mediaName: String, media: ByteArray, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) =
                documentClient.createAttachment(attachmentId, contentType, mediaName, media, document, callback)

        //endregion

        //region Stored Procedures

        // create
        @JvmStatic
        fun createStoredProcedure(storedProcedureId: String, procedure: String, collectionId: String, databaseId: String, callback: (ResourceResponse<StoredProcedure>) -> Unit) =
                documentClient.createStoredProcedure(storedProcedureId, procedure, collectionId, databaseId, callback)

        // create
        @JvmStatic
        fun createStoredProcedure(storedProcedureId: String, procedure: String, collection: DocumentCollection, callback: (ResourceResponse<StoredProcedure>) -> Unit) =
                documentClient.createStoredProcedure(storedProcedureId, procedure, collection, callback)

        // list
        @JvmStatic
        fun getStoredProcedures(collectionId: String, databaseId: String, callback: (ResourceListResponse<StoredProcedure>) -> Unit) =
                documentClient.getStoredProcedures(collectionId, databaseId, callback)

        // list
        @JvmStatic
        fun getStoredProcedures(collection: DocumentCollection, callback: (ResourceListResponse<StoredProcedure>) -> Unit) =
                documentClient.getStoredProcedures(collection, callback)

        // delete
        @JvmStatic
        fun deleteStoredProcedure(storedProcedure: StoredProcedure, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteStoredProcedure(storedProcedure, collectionId, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteStoredProcedure(storedProcedure: StoredProcedure, collection: DocumentCollection, callback: (DataResponse) -> Unit) =
                documentClient.deleteStoredProcedure(storedProcedure, collection, callback)

        // delete
        @JvmStatic
        fun deleteStoredProcedure(storedProcedureId: String, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteStoredProcedure(storedProcedureId, collectionId, databaseId, callback)

        //endregion

        //region User Defined Functions

        // create
        @JvmStatic
        fun createUserDefinedFunction(functionId: String, function: String, collectionId: String, databaseId: String, callback: (ResourceResponse<UserDefinedFunction>) -> Unit) =
                documentClient.createUserDefinedFunction(functionId, function, collectionId, databaseId, callback)

        // create
        @JvmStatic
        fun createUserDefinedFunction(functionId: String, function: String, collection: DocumentCollection, callback: (ResourceResponse<UserDefinedFunction>) -> Unit) =
                documentClient.createUserDefinedFunction(functionId, function, collection, callback)

        // list
        @JvmStatic
        fun getUserDefinedFunctions(collectionId: String, databaseId: String, callback: (ResourceListResponse<UserDefinedFunction>) -> Unit) =
                documentClient.getUserDefinedFunctions(collectionId, databaseId, callback)

        // list
        @JvmStatic
        fun getUserDefinedFunctions(collection: DocumentCollection, callback: (ResourceListResponse<UserDefinedFunction>) -> Unit) =
                documentClient.getUserDefinedFunctions(collection, callback)

        // delete
        @JvmStatic
        fun deleteUserDefinedFunction(userDefinedFunctionId: String, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteUserDefinedFunction(userDefinedFunctionId, collectionId, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteUserDefinedFunction(userDefinedFunction: UserDefinedFunction, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteUserDefinedFunction(userDefinedFunction.id, collectionId, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteUserDefinedFunction(userDefinedFunction: UserDefinedFunction, collection: DocumentCollection, callback: (DataResponse) -> Unit) =
                documentClient.deleteUserDefinedFunction(userDefinedFunction, collection, callback)

        // replace
        @JvmStatic
        fun replaceUserDefinedFunction(functionId: String, function: String, collectionId: String, databaseId: String, callback: (ResourceResponse<UserDefinedFunction>) -> Unit) =
                documentClient.replaceUserDefinedFunction(functionId, function, collectionId, databaseId, callback)

        // replace
        @JvmStatic
        fun replaceUserDefinedFunction(functionId: String, function: String, collection: DocumentCollection, callback: (ResourceResponse<UserDefinedFunction>) -> Unit) =
                documentClient.replaceUserDefinedFunction(functionId, function, collection, callback)

        //endregion

        //region Triggers

        // create
        @JvmStatic
        fun createTrigger(triggerId: String, operation: Trigger.TriggerOperation, triggerType: Trigger.TriggerType, triggerBody: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Trigger>) -> Unit) =
                documentClient.createTrigger(triggerId, operation, triggerType, triggerBody, collectionId, databaseId, callback)

        // create
        @JvmStatic
        fun createTrigger(triggerId: String, operation: Trigger.TriggerOperation, triggerType: Trigger.TriggerType, triggerBody: String, collection: DocumentCollection, callback: (ResourceResponse<Trigger>) -> Unit) =
                documentClient.createTrigger(triggerId, operation, triggerType, triggerBody, collection, callback)

        // list
        @JvmStatic
        fun getTriggers(collectionId: String, databaseId: String, callback: (ResourceListResponse<Trigger>) -> Unit) =
                documentClient.getTriggers(collectionId, databaseId, callback)

        // list
        @JvmStatic
        fun getTriggers(collection: DocumentCollection, callback: (ResourceListResponse<Trigger>) -> Unit) =
                documentClient.getTriggers(collection, callback)

        // delete
        @JvmStatic
        fun deleteTrigger(triggerId: String, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteTrigger(triggerId, collectionId, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteTrigger(trigger: Trigger, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteTrigger(trigger.id, collectionId, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteTrigger(trigger: Trigger, collection: DocumentCollection, callback: (DataResponse) -> Unit) =
                documentClient.deleteTrigger(trigger, collection, callback)

        // replace
        @JvmStatic
        fun replaceTrigger(triggerId: String, operation: Trigger.TriggerOperation, triggerType: Trigger.TriggerType, triggerBody: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Trigger>) -> Unit) =
                documentClient.replaceTrigger(triggerId, operation, triggerType, triggerBody, collectionId, databaseId, callback)

        // replace
        @JvmStatic
        fun replaceTrigger(triggerId: String, operation: Trigger.TriggerOperation, triggerType: Trigger.TriggerType, triggerBody: String, collection: DocumentCollection, callback: (ResourceResponse<Trigger>) -> Unit) =
                documentClient.replaceTrigger(triggerId, operation, triggerType, triggerBody, collection, callback)

        //endregion

        //region Users

        // create
        @JvmStatic
        fun createUser(userId: String, databaseId: String, callback: (ResourceResponse<User>) -> Unit) =
                documentClient.createUser(userId, databaseId, callback)

        // list
        @JvmStatic
        fun getUsers(databaseId: String, callback: (ResourceListResponse<User>) -> Unit) =
                documentClient.getUsers(databaseId, callback)

        // get
        @JvmStatic
        fun getUser(userId: String, databaseId: String, callback: (ResourceResponse<User>) -> Unit) =
                documentClient.getUser(userId, databaseId, callback)

        // replace
        @JvmStatic
        fun replaceUser(userId: String, newUserId: String, databaseId: String, callback: (ResourceResponse<User>) -> Unit) =
                documentClient.replaceUser(userId, newUserId, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteUser(userId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteUser(userId, databaseId, callback)

        // delete
        @JvmStatic
        fun deleteUser(user: User, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deleteUser(user.id, databaseId, callback)

        //endregion

        //region Permissions

        // create
        @JvmStatic
        fun createPermission(permissionId: String, permissionMode: Permission.PermissionMode, resource: Resource, userId: String, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) =
                documentClient.createPermission(permissionId, permissionMode, resource, userId, databaseId, callback)

        // create
        @JvmStatic
        fun createPermission(permissionId: String, permissionMode: Permission.PermissionMode, resource: Resource, user: User, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) =
                documentClient.createPermission(permissionId, permissionMode, resource, user.id, databaseId, callback)

        // list
        @JvmStatic
        fun getPermissions(userId: String, databaseId: String, callback: (ResourceListResponse<Permission>) -> Unit) =
                documentClient.getPermissions(userId, databaseId, callback)

        // list
        @JvmStatic
        fun getPermissions(user: User, callback: (ResourceListResponse<Permission>) -> Unit) =
                documentClient.getPermissions(user, callback)

        // get
        @JvmStatic
        fun getPermission(permissionId: String, userId: String, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) =
                documentClient.getPermission(permissionId, userId, databaseId, callback)

        // get
        @JvmStatic
        fun getPermission(permissionId: String, user: User, callback: (ResourceResponse<Permission>) -> Unit) =
                documentClient.getPermission(permissionId, user, callback)

        // delete
        @JvmStatic
        fun deletePermission(permissionId: String, userId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deletePermission(permissionId, userId, databaseId, callback)

        // delete
        @JvmStatic
        fun deletePermission(permission: Permission, userId: String, databaseId: String, callback: (DataResponse) -> Unit) =
                documentClient.deletePermission(permission.id, userId, databaseId, callback)

        // delete
        @JvmStatic
        fun deletePermission(permission: Permission, user: User, callback: (DataResponse) -> Unit) =
                documentClient.deletePermission(permission, user, callback)

        //endregion

        //region Offers

        // list
        @JvmStatic
        fun offers(callback: (ResourceListResponse<Offer>) -> Unit) =
                documentClient.offers(callback)

        // get
        @JvmStatic
        fun getOffer(offerId: String, callback: (ResourceResponse<Offer>) -> Unit) =
                documentClient.getOffer(offerId, callback)

        //endregion

        //region Resources

        // delete
        @JvmStatic
        fun <T : Resource> delete(resource: T, callback: (DataResponse) -> Unit) =
                documentClient.delete(resource, callback)

        // refresh
        @JvmStatic
        fun <T : Resource> refresh(resource: T, callback: (ResourceResponse<T>) -> Unit) =
                documentClient.refresh(resource, callback)

        //endregion
    }
}