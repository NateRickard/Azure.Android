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
        builder.add(ApiValues.HttpRequestHeader.ACCEPTENCODING.value, ApiValues.HttpRequestHeaderValue.ACCEPT_ENCODING.value)

        val currentLocale = LocaleHelper.getCurrentLocale(ContextProvider.appContext)

        //set the accepted locales/languages
//        val mappedLocales = .take(6).mapIndexed { index, locale ->
//            val rank = 1.0 - (index * 0.1)
//            "${locale};q=$rank"
//        }.joinToString()

        builder.add(ApiValues.HttpRequestHeader.ACCEPTLANGUAGE.value, currentLocale.language)

        //set the user agent header
        try {
            val pkgManager = ContextProvider.appContext.packageManager
            val pkgName = ContextProvider.appContext.packageName
            val pInfo = pkgManager.getPackageInfo(pkgName, 0);

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

            builder.add(ApiValues.HttpRequestHeader.USERAGENT.value, userAgent)
        } catch (e: Exception) {
            builder.add(ApiValues.HttpRequestHeader.USERAGENT.value, "AzureMobile.Data")
        }

        //set the api version
        builder.add(ApiValues.HttpRequestHeader.XMSVERSION.value, ApiValues.HttpRequestHeaderValue.API_VERSION.value)

        builder.build()
    }


    // Database

    // create
    fun createDatabase(databaseId: String, callback: (ResourceResponse<Database>) -> Unit) {

        val resourceUri = baseUri.forDatabase()

        return create(databaseId, resourceUri, ResourceType.DATABASE, callback = callback)
    }

    // list
    fun databases(callback: (ResourceListResponse<Database>) -> Unit) {

        val resourceUri = baseUri.forDatabase()

        return resources(resourceUri, ResourceType.DATABASE, callback)
    }

    // get
    fun getDatabase(databaseId: String, callback: (ResourceResponse<Database>) -> Unit) {

        val resourceUri = baseUri.forDatabase(databaseId)

        return resource(resourceUri, ResourceType.DATABASE, callback)
    }

    // delete
    fun deleteDatabase(databaseId: String, callback: (Boolean) -> Unit) {

        val resourceUri = baseUri.forDatabase(databaseId)

        return delete(resourceUri, ResourceType.DATABASE, callback)
    }

    // delete
    fun deleteDatabase(database: Database, callback: (Boolean) -> Unit) {

        deleteDatabase(database.id, callback)
    }


    // Collections

    // create
    fun createCollection(collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {

        val resourceUri = baseUri.forCollection(databaseId)

        return create(collectionId, resourceUri, ResourceType.COLLECTION, callback = callback)
    }

    // list
    fun getCollectionsIn(databaseId: String, callback: (ResourceListResponse<DocumentCollection>) -> Unit) {

        val resourceUri = baseUri.forCollection(databaseId)

        return resources(resourceUri, ResourceType.COLLECTION, callback)
    }

    // get
    fun getCollection(collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {

        val resourceUri = baseUri.forCollection(databaseId, collectionId)

        return resource(resourceUri, ResourceType.COLLECTION, callback)
    }

    // delete
    fun deleteCollection(collectionId: String, databaseId: String, callback: (Boolean) -> Unit) {

        val resourceUri = baseUri.forCollection(databaseId, collectionId)

        return delete(resourceUri, ResourceType.COLLECTION, callback)
    }


    // Documents

    // list
    fun <T : Document> getDocumentsAs(collectionId: String, databaseId: String, callback: (ResourceListResponse<T>) -> Unit) {

        val resourceUri = baseUri.forDocument(databaseId, collectionId)

        return resources(resourceUri, ResourceType.DOCUMENT, callback)
    }

    fun <T : Document> getDocumentsAs(collection: DocumentCollection, callback: (ResourceListResponse<T>) -> Unit) {

        val resourceUri = baseUri.forDocument(collection.selfLink!!)

        return resources(resourceUri, ResourceType.DOCUMENT, callback)
    }


    // Users

    // create
    fun createUser(userId: String, databaseId: String, callback: (ResourceResponse<User>) -> Unit) {

        val resourceUri = baseUri.forUser(databaseId)

        return create(userId, resourceUri, ResourceType.USER, callback = callback)
    }

    // list
    fun getUsers(databaseId: String, callback: (ResourceListResponse<User>) -> Unit) {

        val resourceUri = baseUri.forUser(databaseId)

        return resources(resourceUri, ResourceType.USER, callback)
    }

    // get
    fun getUser(userId: String, databaseId: String, callback: (ResourceResponse<User>) -> Unit) {

        val resourceUri = baseUri.forUser(databaseId, userId)

        return resource(resourceUri, ResourceType.USER, callback)
    }

    // delete
    fun deleteUser(userId: String, databaseId: String, callback: (Boolean) -> Unit) {

        val resourceUri = baseUri.forUser(databaseId, userId)

        return delete(resourceUri, ResourceType.USER, callback)
    }


    // Permissions

    // create
    fun createPermission(permissionId: String, permissionMode: Permission.PermissionMode, resource: Resource, userId: String, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) {

        val resourceUri = baseUri.forPermission(databaseId, userId, null)

        val permission = Permission(permissionId, permissionMode, resource.selfLink!!)

        return create(permission, resourceUri, ResourceType.PERMISSION, callback = callback)
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
//        return create(permission, resourceUri, ResourceType.PERMISSION, callback = callback)
//    }

    // list
    fun getPermissions(userId: String, databaseId: String, callback: (ResourceListResponse<Permission>) -> Unit) {

        val resourceUri = baseUri.forPermission(databaseId, userId, null)

        return resources(resourceUri, ResourceType.PERMISSION, callback)
    }

    // list
    fun getPermissions(user: User, callback: (ResourceListResponse<Permission>) -> Unit) {

        val resourceUri = baseUri.forPermission(user.selfLink!!, null)

        return resources(resourceUri, ResourceType.PERMISSION, callback)
    }

    // get
    fun getPermission(permissionId: String, userId: String, databaseId: String, callback: (ResourceResponse<Permission>) -> Unit) {

        val resourceUri = baseUri.forPermission(databaseId, userId, permissionId)

        return resource(resourceUri, ResourceType.PERMISSION, callback)
    }

    // get
    fun getPermission(permissionId: String, user: User, callback: (ResourceResponse<Permission>) -> Unit) {

        val resourceUri = baseUri.forPermission(user.selfLink!!, permissionId)

        return resource(resourceUri, ResourceType.PERMISSION, callback)
    }

    // delete
    fun deletePermission(permissionId: String, userId: String, databaseId: String, callback: (Boolean) -> Unit) {

        val resourceUri = baseUri.forPermission(databaseId, userId, permissionId)

        return delete(resourceUri, ResourceType.PERMISSION, callback)
    }

    // delete
    fun deletePermission(permission: Permission, user: User, callback: (Boolean) -> Unit) {

        val resourceUri = baseUri.forPermission(user.selfLink!!, permission.id)

        return delete(resourceUri, ResourceType.PERMISSION, callback)
    }


    // Offers

    // list
    fun offers(callback: (ResourceListResponse<Offer>) -> Unit) {

        val resourceUri = baseUri.forOffer()

        return resources(resourceUri, ResourceType.OFFER, callback)
    }

    // get
    fun getOffer(offerId: String, callback: (ResourceResponse<Offer>) -> Unit): Any {

        val resourceUri = baseUri.forOffer(offerId)

        return resource(resourceUri, ResourceType.OFFER, callback)
    }


    // Resources

    // create
    private fun <T : Resource> create(resource: T, resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        if (!resource.id.isValidResourceId()) {
            return callback(ResourceResponse(invalidIdError))
        }

        createOrReplace(resource, resourceUri, resourceType, false, additionalHeaders, callback)
    }

    // create
    private fun <T : Resource> create(resourceId: String, resourceUri: UrlLink, resourceType: ResourceType, data: MutableMap<String, String?>? = null, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        if (!resourceId.isValidResourceId()) {
            return callback(ResourceResponse(invalidIdError))
        }

        val map = data ?: mutableMapOf()
        map["id"] = resourceId

        createOrReplace(map, resourceUri, resourceType, false, additionalHeaders, callback)
    }

    // create
    private fun <T : Resource> createAsync(resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers, jsonBody: String? = null): Deferred<ResourceListResponse<T>> {

        val request = createRequest(ApiValues.HttpMethod.POST, resourceUri, resourceType, additionalHeaders, jsonBody)

        return sendAsync(request, resourceType)
    }

    // list
    private fun <T : Resource> resources(resourceUri: UrlLink, resourceType: ResourceType, callback: (ResourceListResponse<T>) -> Unit) {

        val request = createRequest(ApiValues.HttpMethod.GET, resourceUri, resourceType)

        return sendResourceListRequest(request, resourceType, callback)
    }

    // get
    private fun <T : Resource> resource(resourceUri: UrlLink, resourceType: ResourceType, callback: (ResourceResponse<T>) -> Unit) {

        val request = createRequest(ApiValues.HttpMethod.GET, resourceUri, resourceType)

        return sendResourceRequest(request, resourceType, callback)
    }

    // list
    private fun <T : Resource> getResourcesAsync(resourceUri: UrlLink, resourceType: ResourceType): Deferred<ResourceListResponse<T>> {

        val request = createRequest(ApiValues.HttpMethod.GET, resourceUri, resourceType)

        return sendAsync(request, resourceType)
    }

    // delete
    private fun delete(resourceUri: UrlLink, resourceType: ResourceType, callback: (Boolean) -> Unit) {

        val request = createRequest(ApiValues.HttpMethod.DELETE, resourceUri, resourceType)

        return sendRequest(request, callback)
    }

    // create or replace
    private fun <T : Resource, R> createOrReplace(body: R, resourceUri: UrlLink, resourceType: ResourceType, replacing: Boolean = false, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        try {
            val jsonBody = JsonHelper.Gson.toJson(body)

            val request = createRequest(if (replacing) ApiValues.HttpMethod.PUT else ApiValues.HttpMethod.POST, resourceUri, resourceType, additionalHeaders, jsonBody)

            return sendResourceRequest(request, resourceType, callback)

        } catch (e: Exception) {
            callback(ResourceResponse(DataError(e)))
        }
    }

    private fun<T: Resource> createOrReplace(body: Map<String, String?>, resourceUri: UrlLink, resourceType: ResourceType, replacing: Boolean = false, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        try {
            val jsonBody = JsonHelper.Gson.toJson(body)

            val request = createRequest(if (replacing) ApiValues.HttpMethod.PUT else ApiValues.HttpMethod.POST, resourceUri, resourceType, additionalHeaders, jsonBody)

            return sendResourceRequest(request, resourceType, callback)
        } catch (e: Exception) {
            callback(ResourceResponse(DataError(e)))
        }
    }


    //region Network plumbing

    private fun createRequest(method: ApiValues.HttpMethod, resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers? = null, jsonBody: String? = null, forQuery: Boolean = false) : Request {

        try {
            val token = tokenProvider.getToken(method, resourceType, resourceUri.link)

            val builder = Request.Builder()
                    .headers(headers) //base headers
                    .url(resourceUri.url)

            when (method) {
                ApiValues.HttpMethod.GET -> builder.get()
                ApiValues.HttpMethod.POST -> builder.post(RequestBody.create(jsonMediaType, jsonBody!!))
                ApiValues.HttpMethod.HEAD -> builder.head()
                ApiValues.HttpMethod.PUT -> builder.put(RequestBody.create(jsonMediaType, jsonBody!!))
                ApiValues.HttpMethod.DELETE -> builder.delete()
            }

            builder.addHeader(ApiValues.HttpRequestHeader.XMSDATE.value, token.date)
            builder.addHeader(ApiValues.HttpRequestHeader.AUTHORIZATION.value, token.authString)

            if (forQuery) {
                builder.addHeader(ApiValues.HttpRequestHeader.XMSDOCUMENTDBISQUERY.value, "true")
                builder.addHeader(ApiValues.HttpRequestHeader.CONTENTTYPE.value, ApiValues.MediaTypes.QUERY_JSON.value)
            } else if ((method == ApiValues.HttpMethod.POST || method == ApiValues.HttpMethod.PUT) && resourceType != ResourceType.ATTACHMENT) {
                // For POST on query operations, it must be application/query+json
                // For attachments, must be set to the Mime type of the attachment.
                // For all other tasks, must be application/json.
                builder.addHeader(ApiValues.HttpRequestHeader.CONTENTTYPE.value, ApiValues.MediaTypes.JSON.value)
            }

            //if we have additional headers, let's add them in here
            additionalHeaders?.let {
                for (headerName in additionalHeaders.names()) {
                    builder.addHeader(headerName, additionalHeaders[headerName]!!)
                }
            }

            return builder.build()
        }
        catch (e: Exception) {

            e.printStackTrace()

            throw e
        }
    }

    private fun<T: Resource> sendResourceRequest(request: Request, resourceType: ResourceType, callback: (ResourceResponse<T>) -> Unit) {

        try {
            client.newCall(request)
                    .enqueue(object : Callback {

                        override fun onFailure(call: Call, e: IOException) {
                            // Error
                            return callback(ResourceResponse(DataError(e)))
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {

                            return callback(processResponse(request, response, resourceType))
                        }
                    })
        }
        catch (e: Exception) {
            logIfVerbose(e)
            callback(ResourceResponse(DataError(e)))
        }
    }

    private fun sendRequest(request: Request, callback: (Boolean) -> Unit) {

        try {
            client.newCall(request)
                    .enqueue(object : Callback {

                        override fun onFailure(call: Call, e: IOException) {
                            // Error
                            logIfVerbose(e)

                            return callback(false)
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {

                            return callback(true)
                        }
                    })
        }
        catch (e: Exception) {
            logIfVerbose(e)
            callback(false)
        }
    }

    private fun<T: Resource> sendAsync(request: Request, resourceType: ResourceType) : Deferred<ResourceListResponse<T>> {

        return async(CommonPool) {

            val response = client.newCall(request).execute()

            processListResponse<T>(request, response, resourceType)
        }
    }

    private fun<T: Resource> sendResourceListRequest(request: Request, resourceType: ResourceType, callback: (ResourceListResponse<T>) -> Unit) {
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

    private fun<T: Resource> processResponse(request: Request, response: Response, resourceType: ResourceType) : ResourceResponse<T> {

        try {
            val body = response.body() ?: return ResourceResponse(DataError("Empty response body received"))

            val json = body.string()

            logIfVerbose(json)

            //check http return code
            if (response.isSuccessful) {
                val resource = JsonHelper.Gson.fromJson<T>(json, resourceType.type) ?: return ResourceResponse(json.toError())

                val result = Result(resource)

                return ResourceResponse(request, response, json, result)
            }
            else {
                return ResourceResponse(json.toError())
            }
        }
        catch (e: Exception) {
            return ResourceResponse(DataError(e))
        }
    }

    private fun<T: Resource> processListResponse(request: Request, response: Response, resourceType: ResourceType) : ResourceListResponse<T> {

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
            }
            else {
                return ResourceListResponse(json.toError())
            }
        }
        catch (e: Exception) {
            return ResourceListResponse(DataError(e))
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

        val invalidIdError : DataError = DataError("Cosmos DB Resource IDs must not exceed 255 characters and cannot contain whitespace")

        val jsonMediaType = MediaType.parse(ApiValues.MediaTypes.JSON.value)
    }
}