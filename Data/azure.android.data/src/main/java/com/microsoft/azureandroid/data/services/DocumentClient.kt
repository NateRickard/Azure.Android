package com.microsoft.azureandroid.data.services

import android.content.pm.PackageManager
import com.microsoft.azureandroid.data.constants.ApiValues
import com.microsoft.azureandroid.data.constants.TokenType
import com.microsoft.azureandroid.data.BuildConfig
import com.microsoft.azureandroid.data.model.*
import com.google.gson.JsonParser
import com.microsoft.azureandroid.data.util.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import okhttp3.*
import java.io.IOException

/**
* Created by Nate Rickard on 10/31/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class DocumentClient(private val baseUri: ResourceUri, key: String, keyType: TokenType = TokenType.MASTER) {

    private val tokenProvider: TokenProvider = TokenProvider(key, keyType, "1.0")

    private val headers: Headers by lazy {

        val builder = Headers.Builder()

        //set the accept encoding header
        builder.add(ApiValues.HttpRequestHeader.AcceptEncoding.value, ApiValues.HttpRequestHeaderValue.AcceptEncoding.value)

        val currentLocale = LocaleHelper.getCurrentLocale(ContextProvider.appContext)

        //set the accepted locales/languages
//        val mappedLocales = .take(6).mapIndexed { index, locale ->
//            val rank = 1.0 - (index * 0.1)
//            "${locale};q=$rank"
//        }.joinToString()

        builder.add(ApiValues.HttpRequestHeader.AcceptLanguage.value, currentLocale.language)

        //set the user agent header
        try {
            val pkgManager = ContextProvider.appContext.packageManager
            val pkgName = ContextProvider.appContext.packageName
            val pInfo = pkgManager.getPackageInfo(pkgName, 0)

            val appName = pInfo?.applicationInfo?.loadLabel(pkgManager) ?: "Unknown"
            val appVersion = pInfo?.versionName ?: "Unknown"
            val appVersionCode = pInfo?.versionCode ?: "Unknown"
            var os = "Android"

            if (pkgManager.hasSystemFeature(PackageManager.FEATURE_WATCH)) {
                os += " Wear"
            }

            val osDetails = "$os ${android.os.Build.VERSION.RELEASE}"
            val azureDataVersion = "AzureMobile.Data/${BuildConfig.VERSION_NAME}"

            val userAgent = "$appName/$appVersion ($pkgName; build:$appVersionCode; $osDetails) $azureDataVersion"

            print(userAgent)

            builder.add(ApiValues.HttpRequestHeader.UserAgent.value, userAgent)
        } catch (e: Exception) {
            builder.add(ApiValues.HttpRequestHeader.UserAgent.value, "AzureMobile.Data")
        }

        //set the api version
        builder.add(ApiValues.HttpRequestHeader.XMSVersion.value, ApiValues.HttpRequestHeaderValue.ApiVersion.value)

        builder.build()
    }

    //region Database

    // create
    fun createDatabase(databaseId: String, callback: (ResourceResponse<Database>) -> Unit) {

        val resourceUri = baseUri.forDatabase()

        return create(databaseId, resourceUri, ResourceType.Database, callback = callback)
    }

    // list
    fun databases(callback: (ResourceListResponse<Database>) -> Unit) {

        val resourceUri = baseUri.forDatabase()

        return resources(resourceUri, ResourceType.Database, callback)
    }

    // get
    fun getDatabase(databaseId: String, callback: (ResourceResponse<Database>) -> Unit) {

        val resourceUri = baseUri.forDatabase(databaseId)

        return resource(resourceUri, ResourceType.Database, callback)
    }

    // delete
    fun deleteDatabase(databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forDatabase(databaseId)

        return delete(resourceUri, ResourceType.Database, callback)
    }

    // delete
    fun deleteDatabase(database: Database, callback: (DataResponse) -> Unit) {

        deleteDatabase(database.id, callback)
    }

    //endregion

    //region Collections

    // create
    fun createCollection(collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {

        val resourceUri = baseUri.forCollection(databaseId)

        return create(collectionId, resourceUri, ResourceType.Collection, callback = callback)
    }

    // list
    fun getCollectionsIn(databaseId: String, callback: (ResourceListResponse<DocumentCollection>) -> Unit) {

        val resourceUri = baseUri.forCollection(databaseId)

        return resources(resourceUri, ResourceType.Collection, callback)
    }

    // get
    fun getCollection(collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {

        val resourceUri = baseUri.forCollection(databaseId, collectionId)

        return resource(resourceUri, ResourceType.Collection, callback)
    }

    // delete
    fun deleteCollection(collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forCollection(databaseId, collectionId)

        return delete(resourceUri, ResourceType.Collection, callback)
    }

    //endregion

    //region Documents

    // create
    fun <T : Document> createDocument(document: T, collectionId: String, databaseId: String, callback: (ResourceResponse<T>) -> Unit) {

        val resourceUri = baseUri.forDocument(databaseId, collectionId)

        return create(document, resourceUri, ResourceType.Document, callback = callback)
    }

    // list
    fun <T : Document> getDocumentsAs(collectionId: String, databaseId: String, callback: (ResourceListResponse<T>) -> Unit) {

        val resourceUri = baseUri.forDocument(databaseId, collectionId)

        return resources(resourceUri, ResourceType.Document, callback)
    }

    // list
    fun <T : Document> getDocumentsAs(collection: DocumentCollection, callback: (ResourceListResponse<T>) -> Unit) {

        val resourceUri = baseUri.forDocument(collection.selfLink!!)

        return resources(resourceUri, ResourceType.Document, callback)
    }

    // get
    fun <T : Document> getDocument(documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<T>) -> Unit) {

        val resourceUri = baseUri.forDocument(databaseId, collectionId, documentId)

        return resource(resourceUri, ResourceType.Document, callback)
    }

    // delete
    fun deleteDocument(documentId: String, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forDocument(databaseId, collectionId, documentId)

        return delete(resourceUri, ResourceType.Document, callback)
    }

    //endregion

    //region Attachments

    // create
    fun createAttachment(attachmentId: String, contentType: String, mediaUrl: HttpUrl, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) {

        val resourceUri = baseUri.forAttachment(databaseId, collectionId, documentId)

        return create(Attachment(attachmentId, contentType, mediaUrl.toString()), resourceUri, ResourceType.Attachment, callback = callback)
    }

    // create
    fun createAttachment(attachmentId: String, contentType: String, mediaName: String, media: ByteArray, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) {

        val resourceUri = baseUri.forAttachment(databaseId, collectionId, documentId)
        val headers = Headers.Builder()
                .add(ApiValues.HttpRequestHeader.ContentType.value, contentType)
                .add(ApiValues.HttpRequestHeader.Slug.value, mediaName)
                .build()

        return createOrReplace(media, resourceUri, ResourceType.Attachment, additionalHeaders = headers, callback = callback)
    }

    // create
    fun createAttachment(attachmentId: String, contentType: String, mediaUrl: HttpUrl, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) {

        val resourceUri = baseUri.forAttachment(document.selfLink!!)

        return create(Attachment(attachmentId, contentType, mediaUrl.toString()), resourceUri, ResourceType.Attachment, callback = callback)
    }

    // create
    fun createAttachment(attachmentId: String, contentType: String, mediaName: String, media: ByteArray, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) {

        val resourceUri = baseUri.forAttachment(document.selfLink!!)
        val headers = Headers.Builder()
                .add(ApiValues.HttpRequestHeader.ContentType.value, contentType)
                .add(ApiValues.HttpRequestHeader.Slug.value, mediaName)
                .build()

        return createOrReplace(media, resourceUri, ResourceType.Attachment, additionalHeaders = headers, callback = callback)
    }

    // list
    fun getAttachments(documentId: String, collectionId: String, databaseId: String, callback: (ResourceListResponse<Attachment>) -> Unit) {

        val resourceUri = baseUri.forAttachment(databaseId, collectionId, documentId)

        return resources(resourceUri, ResourceType.Attachment, callback)
    }

    // list
    fun getAttachments(document: Document, callback: (ResourceListResponse<Attachment>) -> Unit) {

        val resourceUri = baseUri.forAttachment(document.selfLink!!)

        return resources(resourceUri, ResourceType.Attachment, callback)
    }

    // delete
    fun deleteAttachment(attachment: Attachment, documentId: String, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forAttachment(databaseId, collectionId, documentId, attachment.id)

        return delete(resourceUri, ResourceType.Attachment, callback)
    }

    // delete
    fun deleteAttachment(attachment: Attachment, document: Document, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forAttachment(document.selfLink!!, attachment.id)

        return delete(resourceUri, ResourceType.Attachment, callback)
    }

    // replace
    fun replaceAttachment(attachmentId: String, contentType: String, mediaUrl: HttpUrl, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) {

        val resourceUri = baseUri.forAttachment(databaseId, collectionId, documentId, attachmentId)

        return replace(Attachment(attachmentId, contentType, mediaUrl.toString()), resourceUri, ResourceType.Attachment, callback = callback)
    }

    // replace
    fun replaceAttachment(attachmentId: String, contentType: String, mediaName: String, media: ByteArray, documentId: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Attachment>) -> Unit) {

        val resourceUri = baseUri.forAttachment(databaseId, collectionId, documentId, attachmentId)
        val headers = Headers.Builder()
                .add(ApiValues.HttpRequestHeader.ContentType.value, contentType)
                .add(ApiValues.HttpRequestHeader.Slug.value, mediaName)
                .build()

        return createOrReplace(media, resourceUri, ResourceType.Attachment, replacing = true, additionalHeaders = headers, callback = callback)
    }

    // replace
    fun replaceAttachment(attachmentId: String, contentType: String, mediaUrl: HttpUrl, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) {

        val resourceUri = baseUri.forAttachment(document.selfLink!!, attachmentId)

        return replace(Attachment(attachmentId, contentType, mediaUrl.toString()), resourceUri, ResourceType.Attachment, callback = callback)
    }

    // replace
    fun replaceAttachment(attachmentId: String, contentType: String, mediaName: String, media: ByteArray, document: Document, callback: (ResourceResponse<Attachment>) -> Unit) {

        val resourceUri = baseUri.forAttachment(document.selfLink!!, attachmentId)
        val headers = Headers.Builder()
                .add(ApiValues.HttpRequestHeader.ContentType.value, contentType)
                .add(ApiValues.HttpRequestHeader.Slug.value, mediaName)
                .build()

        return createOrReplace(media, resourceUri, ResourceType.Attachment, replacing = true, additionalHeaders = headers, callback = callback)
    }

    //endregion

    //region Stored Procedures

    // create
    fun createStoredProcedure(storedProcedureId: String, procedure: String, collectionId: String, databaseId: String, callback: (ResourceResponse<StoredProcedure>) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(databaseId, collectionId)

        return create(storedProcedureId, resourceUri, ResourceType.StoredProcedure, mutableMapOf("body" to procedure), callback = callback)
    }

    // create
    fun createStoredProcedure(storedProcedureId: String, procedure: String, collection: DocumentCollection, callback: (ResourceResponse<StoredProcedure>) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(collection.selfLink!!)

        return create(storedProcedureId, resourceUri, ResourceType.StoredProcedure, mutableMapOf("body" to procedure), callback = callback)
    }

    // list
    fun getStoredProcedures(collectionId: String, databaseId: String, callback: (ResourceListResponse<StoredProcedure>) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(databaseId, collectionId)

        return resources(resourceUri, ResourceType.StoredProcedure, callback)
    }

    // list
    fun getStoredProcedures(collection: DocumentCollection, callback: (ResourceListResponse<StoredProcedure>) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(collection.selfLink!!)

        return resources(resourceUri, ResourceType.StoredProcedure, callback)
    }

    // delete
    fun deleteStoredProcedure(storedProcedure: StoredProcedure, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(databaseId, collectionId, storedProcedure.id)

        return delete(resourceUri, ResourceType.StoredProcedure, callback)
    }

    // delete
    fun deleteStoredProcedure(storedProcedure: StoredProcedure, collection: DocumentCollection, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(collection.selfLink!!, storedProcedure.id)

        return delete(resourceUri, ResourceType.StoredProcedure, callback)
    }

    // delete
    fun deleteStoredProcedure(storedProcedureId: String, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(databaseId, collectionId, storedProcedureId)

        return delete(resourceUri, ResourceType.StoredProcedure, callback)
    }

    // replace
    fun replaceStoredProcedure(storedProcedureId: String, procedure: String, collectionId: String, databaseId: String, callback: (ResourceResponse<StoredProcedure>) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(databaseId, collectionId, storedProcedureId)

        replace(storedProcedureId, mutableMapOf("body" to procedure), resourceUri, ResourceType.StoredProcedure, callback = callback)
    }

    // replace
    fun replaceStoredProcedure(storedProcedureId: String, procedure: String, collection: DocumentCollection, callback: (ResourceResponse<StoredProcedure>) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(collection.selfLink!!, storedProcedureId = storedProcedureId)

        replace(storedProcedureId, mutableMapOf("body" to procedure), resourceUri, ResourceType.StoredProcedure, callback = callback)
    }

    // execute
    fun executeStoredProcedure(storedProcedureId: String, parameters: List<String>?, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(databaseId, collectionId, storedProcedureId)

        return execute(parameters, resourceUri, ResourceType.StoredProcedure, callback)
    }

    // execute
    fun executeStoredProcedure(storedProcedureId: String, parameters: List<String>?, collection: DocumentCollection, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forStoredProcedure(collection.selfLink!!, storedProcedureId = storedProcedureId)

        return execute(parameters, resourceUri, ResourceType.StoredProcedure, callback)
    }

    //endregion

    //region User Defined Functions

    // create
    fun createUserDefinedFunction(functionId: String, function: String, collectionId: String, databaseId: String, callback: (ResourceResponse<UserDefinedFunction>) -> Unit) {

        val resourceUri = baseUri.forUdf(databaseId, collectionId)

        return create(functionId, resourceUri, ResourceType.Udf, mutableMapOf("body" to function), callback = callback)
    }

    // create
    fun createUserDefinedFunction(functionId: String, function: String, collection: DocumentCollection, callback: (ResourceResponse<UserDefinedFunction>) -> Unit) {

        val resourceUri = baseUri.forUdf(collection.selfLink!!, udfId = functionId)

        return create(functionId, resourceUri, ResourceType.Udf, mutableMapOf("body" to function), callback = callback)
    }

    // list
    fun getUserDefinedFunctions(collectionId: String, databaseId: String, callback: (ResourceListResponse<UserDefinedFunction>) -> Unit) {

        val resourceUri = baseUri.forUdf(databaseId, collectionId)

        return resources(resourceUri, ResourceType.Udf, callback)
    }

    // list
    fun getUserDefinedFunctions(collection: DocumentCollection, callback: (ResourceListResponse<UserDefinedFunction>) -> Unit) {

        val resourceUri = baseUri.forUdf(collection.selfLink!!)

        return resources(resourceUri, ResourceType.Udf, callback)
    }

    // delete
    fun deleteUserDefinedFunction(userDefinedFunctionId: String, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forUdf(databaseId, collectionId, udfId = userDefinedFunctionId)

        return delete(resourceUri, ResourceType.Udf, callback)
    }

    // delete
    fun deleteUserDefinedFunction(userDefinedFunction: UserDefinedFunction, collection: DocumentCollection, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forUdf(collection.selfLink!!, udfId = userDefinedFunction.id)

        return delete(resourceUri, ResourceType.Udf, callback)
    }

    // replace
    fun replaceUserDefinedFunction(functionId: String, function: String, collectionId: String, databaseId: String, callback: (ResourceResponse<UserDefinedFunction>) -> Unit) {

        val resourceUri = baseUri.forUdf(databaseId, collectionId, udfId = functionId)

        return replace(functionId, mutableMapOf("body" to function), resourceUri, ResourceType.Udf, callback = callback)
    }

    // replace
    fun replaceUserDefinedFunction(functionId: String, function: String, collection: DocumentCollection, callback: (ResourceResponse<UserDefinedFunction>) -> Unit) {

        val resourceUri = baseUri.forUdf(collection.selfLink!!, udfId = functionId)

        return replace(functionId, mutableMapOf("body" to function), resourceUri, ResourceType.Udf, callback = callback)
    }

    //endregion

    //region Triggers

    // create
    fun createTrigger(triggerId: String, operation: Trigger.TriggerOperation, triggerType: Trigger.TriggerType, triggerBody: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Trigger>) -> Unit) {

        val resourceUri = baseUri.forTrigger(databaseId, collectionId)

        return create(Trigger(triggerId, triggerBody, operation, triggerType), resourceUri, ResourceType.Trigger, callback = callback)
    }

    // create
    fun createTrigger(triggerId: String, operation: Trigger.TriggerOperation, triggerType: Trigger.TriggerType, triggerBody: String, collection: DocumentCollection, callback: (ResourceResponse<Trigger>) -> Unit) {

        val resourceUri = baseUri.forTrigger(collection.selfLink!!, triggerId = triggerId)

        return create(Trigger(triggerId, triggerBody, operation, triggerType), resourceUri, ResourceType.Trigger, callback = callback)
    }

    // list
    fun getTriggers(collectionId: String, databaseId: String, callback: (ResourceListResponse<Trigger>) -> Unit) {

        val resourceUri = baseUri.forTrigger(databaseId, collectionId)

        return resources(resourceUri, ResourceType.Trigger, callback)
    }

    // list
    fun getTriggers(collection: DocumentCollection, callback: (ResourceListResponse<Trigger>) -> Unit) {

        val resourceUri = baseUri.forTrigger(collection.selfLink!!)

        return resources(resourceUri, ResourceType.Trigger, callback)
    }

    // delete
    fun deleteTrigger(triggerId: String, collectionId: String, databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forTrigger(databaseId, collectionId, triggerId)

        return delete(resourceUri, ResourceType.Trigger, callback)
    }

    // delete
    fun deleteTrigger(trigger: Trigger, collection: DocumentCollection, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forTrigger(collection.selfLink!!, triggerId = trigger.id)

        return delete(resourceUri, ResourceType.Trigger, callback)
    }

    // replace
    fun replaceTrigger(triggerId: String, operation: Trigger.TriggerOperation, triggerType: Trigger.TriggerType, triggerBody: String, collectionId: String, databaseId: String, callback: (ResourceResponse<Trigger>) -> Unit) {

        val resourceUri = baseUri.forTrigger(databaseId, collectionId, triggerId)

        return replace(Trigger(triggerId, triggerBody, operation, triggerType), resourceUri, ResourceType.Trigger, callback = callback)
    }

    // replace
    fun replaceTrigger(triggerId: String, operation: Trigger.TriggerOperation, triggerType: Trigger.TriggerType, triggerBody: String, collection: DocumentCollection, callback: (ResourceResponse<Trigger>) -> Unit) {

        val resourceUri = baseUri.forTrigger(collection.selfLink!!, triggerId = triggerId)

        return replace(Trigger(triggerId, triggerBody, operation, triggerType), resourceUri, ResourceType.Trigger, callback = callback)
    }

    //endregion

    //region Users

    // create
    fun createUser(userId: String, databaseId: String, callback: (ResourceResponse<User>) -> Unit) {

        val resourceUri = baseUri.forUser(databaseId)

        return create(userId, resourceUri, ResourceType.User, callback = callback)
    }

    // list
    fun getUsers(databaseId: String, callback: (ResourceListResponse<User>) -> Unit) {

        val resourceUri = baseUri.forUser(databaseId)

        return resources(resourceUri, ResourceType.User, callback)
    }

    // get
    fun getUser(userId: String, databaseId: String, callback: (ResourceResponse<User>) -> Unit) {

        val resourceUri = baseUri.forUser(databaseId, userId)

        return resource(resourceUri, ResourceType.User, callback)
    }

    // replace
    fun replaceUser(userId: String, newUserId: String, databaseId: String, callback: (ResourceResponse<User>) -> Unit) {

        val resourceUri = baseUri.forUser(databaseId, userId)

        return replace(newUserId, resourceUri, ResourceType.User, callback)
    }

    // delete
    fun deleteUser(userId: String, databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forUser(databaseId, userId)

        return delete(resourceUri, ResourceType.User, callback)
    }

    //endregion

    //region Permissions

    // create
    fun createPermission(permissionId: String, permissionMode: Permission.PermissionMode, resource: Resource, userId: String, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) {

        val resourceUri = baseUri.forPermission(databaseId, userId, null)

        val permission = Permission(permissionId, permissionMode, resource.selfLink!!)

        return create(permission, resourceUri, ResourceType.Permission, callback = callback)
    }

    // create
//    fun createPermission(permissionId: String, permissionMode: Permission.PermissionMode, resource: Resource, user: User, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) {
//
////        val resourceUri = baseUri.forPermission(user.selfLink!!, null)
//
//        val resourceUri = baseUri.forPermission(databaseId, user.id, null)
//
//        val permission = Permission(permissionId, permissionMode, resource.selfLink!!)
//
//        return create(permission, resourceUri, ResourceType.Permission, callback = callback)
//    }

    // list
    fun getPermissions(userId: String, databaseId: String, callback: (ResourceListResponse<Permission>) -> Unit) {

        val resourceUri = baseUri.forPermission(databaseId, userId, null)

        return resources(resourceUri, ResourceType.Permission, callback)
    }

    // list
    fun getPermissions(user: User, callback: (ResourceListResponse<Permission>) -> Unit) {

        val resourceUri = baseUri.forPermission(user.selfLink!!, null)

        return resources(resourceUri, ResourceType.Permission, callback)
    }

    // get
    fun getPermission(permissionId: String, userId: String, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) {

        val resourceUri = baseUri.forPermission(databaseId, userId, permissionId)

        return resource(resourceUri, ResourceType.Permission, callback)
    }

    // get
    fun getPermission(permissionId: String, user: User, callback: (ResourceResponse<Permission>) -> Unit) {

        val resourceUri = baseUri.forPermission(user.selfLink!!, permissionId)

        return resource(resourceUri, ResourceType.Permission, callback)
    }

    // delete
    fun deletePermission(permissionId: String, userId: String, databaseId: String, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forPermission(databaseId, userId, permissionId)

        return delete(resourceUri, ResourceType.Permission, callback)
    }

    // delete
    fun deletePermission(permission: Permission, user: User, callback: (DataResponse) -> Unit) {

        val resourceUri = baseUri.forPermission(user.selfLink!!, permission.id)

        return delete(resourceUri, ResourceType.Permission, callback)
    }

    //endregion

    //region Offers

    // list
    fun offers(callback: (ResourceListResponse<Offer>) -> Unit) {

        val resourceUri = baseUri.forOffer()

        return resources(resourceUri, ResourceType.Offer, callback)
    }

    // get
    fun getOffer(offerId: String, callback: (ResourceResponse<Offer>) -> Unit): Any {

        val resourceUri = baseUri.forOffer(offerId)

        return resource(resourceUri, ResourceType.Offer, callback)
    }

    //endregion

    //region Resource operations

    // create
    private fun <T : Resource> create(resource: T, resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        if (!resource.id.isValidResourceId()) {
            return callback(ResourceResponse(DataError.fromType(ErrorType.InvalidId)))
        }

        createOrReplace(resource, resourceUri, resourceType, false, additionalHeaders, callback)
    }

    // create
    private fun <T : Resource> create(resourceId: String, resourceUri: UrlLink, resourceType: ResourceType, data: MutableMap<String, String>? = null, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        if (!resourceId.isValidResourceId()) {
            return callback(ResourceResponse(DataError.fromType(ErrorType.InvalidId)))
        }

        val map = data ?: mutableMapOf()
        map["id"] = resourceId

        createOrReplace(map, resourceUri, resourceType, false, additionalHeaders, callback)
    }

    // create
//    private fun<T : Resource> createAsync(resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers, jsonBody: String? = null): Deferred<ResourceResponse<T>> {
//
//        val request = createRequest(ApiValues.HttpMethod.Post, resourceUri, resourceType, additionalHeaders, jsonBody)
//
//        return sendAsync(request, resourceType)
//    }

    // list
    private fun <T : Resource> resources(resourceUri: UrlLink, resourceType: ResourceType, callback: (ResourceListResponse<T>) -> Unit) {

        val request = createRequest(ApiValues.HttpMethod.Get, resourceUri, resourceType)

        return sendResourceListRequest(request, resourceType, callback)
    }

    // get
    private fun <T : Resource> resource(resourceUri: UrlLink, resourceType: ResourceType, callback: (ResourceResponse<T>) -> Unit) {

        val request = createRequest(ApiValues.HttpMethod.Get, resourceUri, resourceType)

        return sendResourceRequest(request, resourceType, callback)
    }

    // list
    private fun <T : Resource> getResourcesAsync(resourceUri: UrlLink, resourceType: ResourceType): Deferred<ResourceListResponse<T>> {

        val request = createRequest(ApiValues.HttpMethod.Get, resourceUri, resourceType)

        return sendAsync(request, resourceType)
    }

    // refresh
    fun <T : Resource> refresh(resource: T, callback: (ResourceResponse<T>) -> Unit) {

        try {

            val resourceUri = baseUri.forResource(resource)
            val resourceType = ResourceType.fromType(resource.javaClass)

            val headers = Headers.Builder()
                    .add(ApiValues.HttpRequestHeader.IfNoneMatch.value, resource.etag!!)
                    .build()

            val request = createRequest(ApiValues.HttpMethod.Get, resourceUri, resourceType, headers)

            return sendResourceRequest(request, resourceType, resource, callback)
        } catch (e: Exception) {
            return callback(ResourceResponse(DataError(e)))
        }
    }

    // delete
    private fun delete(resourceUri: UrlLink, resourceType: ResourceType, callback: (DataResponse) -> Unit) {

        val request = createRequest(ApiValues.HttpMethod.Delete, resourceUri, resourceType)

        return sendRequest(request, callback)
    }

    fun <TResource : Resource> delete(resource: TResource, callback: (DataResponse) -> Unit) {

        return try {

            val resourceUri = baseUri.forResource(resource)
            val resourceType = ResourceType.fromType(resource.javaClass)

            val request = createRequest(ApiValues.HttpMethod.Delete, resourceUri, resourceType)

            sendRequest(request, callback)
        } catch (e: Exception) {
            callback(DataResponse(DataError(e)))
        }
    }

    // replace
    private fun <T : Resource> replace(resource: T, resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        if (!resource.id.isValidResourceId()) {
            return callback(ResourceResponse(DataError.fromType(ErrorType.InvalidId)))
        }

        createOrReplace(resource, resourceUri, resourceType, true, additionalHeaders, callback)
    }

    // replace
    private fun <T : Resource> replace(resourceId: String, resourceUri: UrlLink, resourceType: ResourceType, callback: (ResourceResponse<T>) -> Unit)
            = replace(resourceId, data = null, resourceUri = resourceUri, resourceType = resourceType, callback = callback)

    // replace
    private fun <T : Resource> replace(resourceId: String, data: MutableMap<String, String>? = null, resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        if (!resourceId.isValidResourceId()) {
            return callback(ResourceResponse(DataError.fromType(ErrorType.InvalidId)))
        }

        val map = data ?: mutableMapOf()
        map["id"] = resourceId

        createOrReplace(map, resourceUri, resourceType, true, additionalHeaders, callback)
    }

    // create or replace
    private fun <T : Resource> createOrReplace(body: T, resourceUri: UrlLink, resourceType: ResourceType, replacing: Boolean = false, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        try {
            val jsonBody = JsonHelper.Gson.toJson(body)

            val request = createRequest(if (replacing) ApiValues.HttpMethod.Put else ApiValues.HttpMethod.Post, resourceUri, resourceType, additionalHeaders, jsonBody)

            return sendResourceRequest(request, resourceType, callback)

        } catch (e: Exception) {
            callback(ResourceResponse(DataError(e)))
        }
    }

    // create or replace
    private fun <T : Resource> createOrReplace(body: Map<String, String>, resourceUri: UrlLink, resourceType: ResourceType, replacing: Boolean = false, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        try {
            val jsonBody = JsonHelper.Gson.toJson(body)

            val request = createRequest(if (replacing) ApiValues.HttpMethod.Put else ApiValues.HttpMethod.Post, resourceUri, resourceType, additionalHeaders, jsonBody)

            return sendResourceRequest(request, resourceType, callback)
        } catch (e: Exception) {
            callback(ResourceResponse(DataError(e)))
        }
    }

    // create or replace
    private fun <T : Resource> createOrReplace(body: ByteArray, resourceUri: UrlLink, resourceType: ResourceType, replacing: Boolean = false, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        try {
            val request = createRequest(if (replacing) ApiValues.HttpMethod.Put else ApiValues.HttpMethod.Post, resourceUri, resourceType, additionalHeaders, body)

            return sendResourceRequest(request, resourceType, callback)
        } catch (e: Exception) {
            callback(ResourceResponse(DataError(e)))
        }
    }

    // query
    private fun <T : Resource> query(query: Query, resourceUri: UrlLink, resourceType: ResourceType, callback: (ResourceListResponse<T>) -> Unit) {

        logIfVerbose(query.printQuery())

        try {
//            var type = object : TypeToken<T>() {}.type

//            val resourceType = ResourceType.fromType<T>(T)
            val json = JsonHelper.Gson.toJson(query)

            val request = createRequest(ApiValues.HttpMethod.Post, resourceUri, resourceType, forQuery = true, jsonBody = json)

            return sendResourceListRequest(request, resourceType, callback)

        } catch (e: Exception) {
            callback(ResourceListResponse(DataError(e)))
        }
    }

    // execute
    private fun <T> execute(body: T? = null, resourceUri: UrlLink, resourceType: ResourceType, callback: (DataResponse) -> Unit) {

        try {
//            val resourceType = ResourceType.fromType<T>() ?: throw Exception("Unable to determine resource type requested for query")
            val json = if (body != null) JsonHelper.Gson.toJson(body) else JsonHelper.Gson.toJson(arrayOf<String>())

            val request = createRequest(ApiValues.HttpMethod.Post, resourceUri, resourceType, forQuery = true, jsonBody = json)

            return sendRequest(request, callback)

        } catch (e: Exception) {
            callback(DataResponse(DataError(e)))
        }
    }

    //endregion

    //region Network plumbing

    private fun createRequest(method: ApiValues.HttpMethod, resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers? = null, forQuery: Boolean = false): Request {

        try {
            val builder = createRequestBuilder(method, resourceUri, resourceType, additionalHeaders, forQuery)

            when (method) {
                ApiValues.HttpMethod.Get -> builder.get()
                ApiValues.HttpMethod.Head -> builder.head()
                ApiValues.HttpMethod.Delete -> builder.delete()
                else -> throw Exception("Post and Put requests must use an overload that provides the content body")
            }

            return builder.build()
        } catch (e: Exception) {

            e.printStackTrace()

            throw e
        }
    }

    private fun createRequest(method: ApiValues.HttpMethod, resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers? = null, jsonBody: String, forQuery: Boolean = false): Request {

        try {
            val builder = createRequestBuilder(method, resourceUri, resourceType, additionalHeaders, forQuery)

            when (method) {
                ApiValues.HttpMethod.Post -> builder.post(RequestBody.create(jsonMediaType, jsonBody))
                ApiValues.HttpMethod.Put -> builder.put(RequestBody.create(jsonMediaType, jsonBody))
                else -> throw Exception("Get, Head, and Delete requests must use an overload that without a content body")
            }

            return builder.build()
        } catch (e: Exception) {

            e.printStackTrace()

            throw e
        }
    }

    private fun createRequest(method: ApiValues.HttpMethod, resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers? = null, body: ByteArray, forQuery: Boolean = false): Request {

        try {
            val builder = createRequestBuilder(method, resourceUri, resourceType, additionalHeaders, forQuery)

            when (method) {
                ApiValues.HttpMethod.Post -> builder.post(RequestBody.create(jsonMediaType, body))
                ApiValues.HttpMethod.Put -> builder.put(RequestBody.create(jsonMediaType, body))
                else -> throw Exception("Get, Head, and Delete requests must use an overload that without a content body")
            }

            return builder.build()
        } catch (e: Exception) {

            e.printStackTrace()

            throw e
        }
    }

    private fun createRequestBuilder(method: ApiValues.HttpMethod, resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers? = null, forQuery: Boolean = false): Request.Builder {

        val token = tokenProvider.getToken(method, resourceType, resourceUri.link)

        val builder = Request.Builder()
                .headers(headers) //base headers
                .url(resourceUri.url)

        builder.addHeader(ApiValues.HttpRequestHeader.XMSDate.value, token.date)
        builder.addHeader(ApiValues.HttpRequestHeader.Authorization.value, token.authString)

        if (forQuery) {
            builder.addHeader(ApiValues.HttpRequestHeader.XMSDocumentDBIsQuery.value, "true")
            builder.addHeader(ApiValues.HttpRequestHeader.ContentType.value, ApiValues.MediaTypes.QueryJson.value)
        } else if ((method == ApiValues.HttpMethod.Post || method == ApiValues.HttpMethod.Put) && resourceType != ResourceType.Attachment) {
            // For Post on query operations, it must be application/query+json
            // For attachments, must be set to the Mime type of the attachment.
            // For all other tasks, must be application/json.
            builder.addHeader(ApiValues.HttpRequestHeader.ContentType.value, ApiValues.MediaTypes.Json.value)
        }

        //if we have additional headers, let's add them in here
        additionalHeaders?.let {
            for (headerName in additionalHeaders.names()) {
                builder.addHeader(headerName, additionalHeaders[headerName]!!)
            }
        }

        return builder
    }

    private fun <T : Resource> sendResourceRequest(request: Request, resourceType: ResourceType, callback: (ResourceResponse<T>) -> Unit)
            = sendResourceRequest(request, resourceType, null, callback = callback)

    private fun <T : Resource> sendResourceRequest(request: Request, resourceType: ResourceType, resource: T?, callback: (ResourceResponse<T>) -> Unit) {

        if (ContextProvider.verboseLogging) {
            println("***")
            println("Sending ${request.method()} request for Data to ${request.url()}")
            println("\tBody : ${request.body()?.toString()}")
        }

        try {
            client.newCall(request)
                    .enqueue(object : Callback {

                        override fun onFailure(call: Call, e: IOException) {
                            logIfVerbose(e)
                            return callback(ResourceResponse(DataError(e)))
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) =
                                callback(processResponse(request, response, resourceType, resource))
                    })
        } catch (e: Exception) {
            logIfVerbose(e)
            callback(ResourceResponse(DataError(e)))
        }
    }

    private fun sendRequest(request: Request, callback: (DataResponse) -> Unit) {

        if (ContextProvider.verboseLogging) {
            println("***")
            println("Sending ${request.method()} request for Data to ${request.url()}")
            println("\tBody : ${request.body()?.toString()}")
        }

        try {
            client.newCall(request)
                    .enqueue(object : Callback {

                        override fun onFailure(call: Call, e: IOException) {
                            logIfVerbose(e)
                            return callback(DataResponse(DataError(e)))
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) =
                                callback(processDataResponse(request, response))
                    })
        } catch (e: Exception) {
            logIfVerbose(e)
            callback(DataResponse(DataError(e)))
        }
    }

    private fun <T : Resource> sendAsync(request: Request, resourceType: ResourceType): Deferred<ResourceListResponse<T>> {

        return async(CommonPool) {

            val response = client.newCall(request).execute()

            processListResponse<T>(request, response, resourceType)
        }
    }

    private fun <T : Resource> sendResourceListRequest(request: Request, resourceType: ResourceType, callback: (ResourceListResponse<T>) -> Unit) {

        if (ContextProvider.verboseLogging) {
            println("***")
            println("Sending ${request.method()} request for Data to ${request.url()}")
            println("\tBody : ${request.body()?.toString()}")
        }

        try {
            client.newCall(request)
                    .enqueue(object : Callback {

                        // only transpprt errors handled here
                        override fun onFailure(call: Call, e: IOException) =
                                callback(ResourceListResponse(DataError(e)))

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) =
                                callback(processListResponse(request, response, resourceType))
                    })
        } catch (e: Exception) {
            logIfVerbose(e)
            callback(ResourceListResponse(DataError(e)))
        }
    }

    private fun <T : Resource> processResponse(request: Request, response: Response, resourceType: ResourceType, resource: T?): ResourceResponse<T> {

        try {
            val body = response.body() ?: return ResourceResponse(DataError("Empty response body received"))
            val json = body.string()

            logIfVerbose(json)

            //check http return code
            if (response.isSuccessful) {
                val returnedResource = JsonHelper.Gson.fromJson<T>(json, resourceType.type) ?: return ResourceResponse(json.toError())

                return ResourceResponse(request, response, json, Result(returnedResource))
            } else if (response.code() == ApiValues.StatusCode.NotModified.code) {
                //return the original resource
                return ResourceResponse(request, response, json, Result(resource))
            } else {
                return ResourceResponse(json.toError())
            }
        } catch (e: Exception) {
            return ResourceResponse(DataError(e))
        }
    }

    private fun <T : Resource> processListResponse(request: Request, response: Response, resourceType: ResourceType): ResourceListResponse<T> {

        try {
            val body = response.body() ?: return ResourceListResponse(DataError("Empty response body received"))
            val json = body.string()

            logIfVerbose(json)

            if (response.isSuccessful) {

                val jsonParser = JsonParser()
                val jsonObject = jsonParser.parse(json).asJsonObject

                //                            var resourceList = Gson().fromJson(json, ResourceList::class.java)

                val resourceList = ResourceList<T>(resourceType, jsonObject)

                if (!resourceList.isPopuated) {
                    return ResourceListResponse(json.toError())
                }

                val result = ListResult(resourceList)

                return ResourceListResponse(request, response, json, result)
            } else {
                return ResourceListResponse(json.toError())
            }
        } catch (e: Exception) {
            return ResourceListResponse(DataError(e))
        }
    }

    private fun processDataResponse(request: Request, response: Response): DataResponse {

        try {
            val body = response.body() ?: return DataResponse(DataError("Empty response body received"))
            val json = body.string()

            logIfVerbose(json)

            //check http return code
            return if (response.isSuccessful) {
                DataResponse(request, response, json, DataResult(json))
            } else {
                DataResponse(json.toError())
            }
        } catch (e: Exception) {
            return DataResponse(DataError(e))
        }
    }

    //endregion

    private fun logIfVerbose(thing: Any) {

        if (ContextProvider.verboseLogging) {
            println(thing)
        }
    }

    companion object {

        val client = OkHttpClient()

        val jsonMediaType = MediaType.parse(ApiValues.MediaTypes.Json.value)
    }
}