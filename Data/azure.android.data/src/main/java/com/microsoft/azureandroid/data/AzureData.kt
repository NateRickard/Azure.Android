package com.microsoft.azureandroid.data

import android.content.Context
import com.microsoft.azureandroid.data.constants.TokenType
import com.microsoft.azureandroid.data.model.*
import com.microsoft.azureandroid.data.services.DocumentClient
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroid.data.util.ContextProvider

/**
* Created by Nate Rickard on 10/24/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class AzureData {

    init {
    }

    // Databases

    // create
    fun createDatabase ( databaseId: String, callback: (ResourceResponse<Database>) -> Unit) =
            documentClient.createDatabase (databaseId, callback)

    // list
    fun databases(callback: (ResourceListResponse<Database>) -> Unit) =
            documentClient.databases(callback)

    // get
    fun getDatabase(databaseId: String, callback: (ResourceResponse<Database>) -> Unit) =
            documentClient.getDatabase (databaseId, callback)

    // delete
    fun deleteDatabase (database: Database, callback: (Boolean) -> Unit) =
            documentClient.deleteDatabase (database.id, callback)

    // delete
    fun deleteDatabase (databaseId: String, callback: (Boolean) -> Unit) =
            documentClient.deleteDatabase (databaseId, callback)


    // Collections

    // create
    fun createCollection (collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) =
            documentClient.createCollection (collectionId, databaseId, callback)

    // list
    fun getCollections (databaseId: String, callback: (ResourceListResponse<DocumentCollection>) -> Unit) =
            documentClient.getCollectionsIn(databaseId, callback)

    // get
    fun getCollection (collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) =
            documentClient.getCollection (collectionId, databaseId, callback)

    // delete
    fun deleteCollection (collection: DocumentCollection, databaseId: String, callback: (Boolean) -> Unit) =
            documentClient.deleteCollection (collection.id, databaseId, callback)

    // delete
    fun deleteCollection (collectionId: String, databaseId: String, callback: (Boolean) -> Unit) =
            documentClient.deleteCollection (collectionId, databaseId, callback)


    // Documents

    // list
    fun<T: Document> getDocumentsAs ( collectionId: String, databaseId: String, callback: (ResourceListResponse<T>) -> Unit) =
            documentClient.getDocumentsAs(collectionId, databaseId, callback)

    // list
    fun<T: Document> getDocumentsAs (collection: DocumentCollection, callback: (ResourceListResponse<T>) -> Unit) =
            documentClient.getDocumentsAs(collection, callback)

    // get
    fun<T: Document> getDocument (documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<T>) -> Unit) =
            documentClient.getDocument(documentId, collectionId, databaseId, callback)


    // Users

    // create
    fun createUser (userId: String, databaseId: String, callback: (ResourceResponse<User>) -> Unit) =
            documentClient.createUser (userId, databaseId, callback)

    // list
    fun getUsers (databaseId: String, callback: (ResourceListResponse<User>) -> Unit) =
            documentClient.getUsers(databaseId, callback)

    // get
    fun getUser (userId: String, databaseId: String, callback: (ResourceResponse<User>) -> Unit) =
            documentClient.getUser(userId, databaseId, callback)

    // delete
    fun deleteUser (userId: String, databaseId: String, callback: (Boolean) -> Unit) =
            documentClient.deleteUser(userId, databaseId, callback)

    // delete
    fun deleteUser(user: User, databaseId: String, callback: (Boolean) -> Unit) =
            documentClient.deleteUser(user.id, databaseId, callback)


    // Permissions

    // create
    fun createPermission(permissionId: String, permissionMode: Permission.PermissionMode, resource: Resource, userId: String, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) =
            documentClient.createPermission(permissionId, permissionMode, resource, userId, databaseId, callback)

    fun createPermission(permissionId: String, permissionMode: Permission.PermissionMode, resource: Resource, user: User, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) =
            documentClient.createPermission(permissionId, permissionMode, resource, user.id, databaseId, callback)

    // list
    fun getPermissions(userId: String, databaseId: String, callback: (ResourceListResponse<Permission>) -> Unit) =
            documentClient.getPermissions(userId, databaseId, callback)

    // list
    fun getPermissions(user: User, callback: (ResourceListResponse<Permission>) -> Unit) =
            documentClient.getPermissions(user, callback)

    // get
    fun getPermission(permissionId: String, userId: String, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) =
            documentClient.getPermission(permissionId, userId, databaseId, callback)

    // get
    fun getPermission(permissionId: String, user: User, callback: (ResourceResponse<Permission>) -> Unit) =
            documentClient.getPermission(permissionId, user, callback)

    // delete
    fun deletePermission(permissionId: String, userId: String, databaseId: String, callback: (Boolean) -> Unit) =
            documentClient.deletePermission(permissionId, userId, databaseId, callback)

    // delete
    fun deletePermission(permission: Permission, userId: String, databaseId: String, callback: (Boolean) -> Unit) =
            documentClient.deletePermission(permission.id, userId, databaseId, callback)

    // delete
    fun deletePermission(permission: Permission, user: User, callback: (Boolean) -> Unit) =
            documentClient.deletePermission(permission, user, callback)


    // Offers

    // list
    fun offers(callback: (ResourceListResponse<Offer>) -> Unit) =
            documentClient.offers(callback)

    // get
    fun getOffer (offerId: String, callback: (ResourceResponse<Offer>) -> Unit) =
            documentClient.getOffer(offerId, callback)


    companion object {

        val instance: AzureData by lazy {
            AzureData()
        }

        lateinit var baseUri: ResourceUri
        lateinit var documentClient: DocumentClient

        fun init(context: Context, name: String, key: String, keyType: TokenType = TokenType.MASTER, verboseLogging: Boolean = false) {

            ContextProvider.init(context.applicationContext, verboseLogging)

            baseUri = ResourceUri(name)
            documentClient = DocumentClient(baseUri, key, keyType)
        }
    }
}