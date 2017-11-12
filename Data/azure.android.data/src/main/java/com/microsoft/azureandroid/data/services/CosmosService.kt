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
 * Created by nater on 10/31/17.
 */

class CosmosService(private val baseUri: ResourceUri, key: String, keyType: TokenType = TokenType.MASTER) {

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
            val appVersion = pInfo?.versionName                         ?: "Unknown"
            val appVersionCode = pInfo?.versionCode                     ?: "Unknown"
            var os = "Android"

            if (pkgManager.hasSystemFeature(PackageManager.FEATURE_WATCH)) {
                os += " Wear"
            }

            val osDetails = "$os ${android.os.Build.VERSION.RELEASE}"
            val azureDataVersion = "AzureMobile.Data/${BuildConfig.VERSION_NAME}"

            val userAgent = "$appName/$appVersion ($pkgName; build:$appVersionCode; $osDetails) $azureDataVersion"

            print(userAgent)

            builder.add(ApiValues.HttpRequestHeader.USERAGENT.value, userAgent)
        }
        catch (e: Exception) {
            builder.add(ApiValues.HttpRequestHeader.USERAGENT.value, "AzureMobile.Data")
        }

        //set the api version
        builder.add(ApiValues.HttpRequestHeader.XMSVERSION.value, ApiValues.HttpRequestHeaderValue.API_VERSION.value)

        builder.build()
    }

    // Database

    // create
    fun createDatabase (databaseId: String, callback: (ResourceResponse<Database>) -> Unit) {

        if (!databaseId.isValidResourceId()) {
            return callback(ResourceResponse(invalidIdError))
        }

        val resourceUri = baseUri.database()
        val jsonBody = JsonHelper.Gson.toJson(mapOf("id" to databaseId))

        return create(resourceUri, ResourceType.DATABASE, jsonBody, callback = callback)
    }

    // list
    fun databases (callback: (ResourceListResponse<Database>) -> Unit) {

        val resourceUri = baseUri.database()

        resources(resourceUri, ResourceType.DATABASE, callback)
    }

    // Collections

    // list
    fun getCollectionsIn(databaseId: String, callback: (ResourceListResponse<DocumentCollection>) -> Unit) {

        val resourceUri = baseUri.forCollection(databaseId)

        return resources(resourceUri, ResourceType.COLLECTION, callback)
    }

    // create
    fun createCollection (collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {

        if (!collectionId.isValidResourceId()) {
            return callback(ResourceResponse(invalidIdError))
        }

        val resourceUri = baseUri.forCollection(databaseId)
        val jsonBody = JsonHelper.Gson.toJson(mapOf("id" to collectionId))

        return create(resourceUri, ResourceType.COLLECTION, jsonBody, callback = callback)
    }

    // Documents

    // list

    fun<T: Document> getDocumentsAs(collectionId: String, databaseId: String, callback: (ResourceListResponse<T>) -> Unit) {

        val resourceUri = baseUri.forDocument(databaseId, collectionId)

        return resources(resourceUri, ResourceType.DOCUMENT, callback)
    }

    fun<T: Document> getDocumentsAs(collection: DocumentCollection, callback: (ResourceListResponse<T>) -> Unit) {

        val resourceUri = baseUri.forDocument(collection.selfLink!!)

        return resources(resourceUri, ResourceType.DOCUMENT, callback)
    }

    // Resources

    // list
    private fun<T: Resource> resources (resourceUri: UrlLink, resourceType: ResourceType, callback: (ResourceListResponse<T>) -> Unit) {

        val request = createRequest(ApiValues.HttpMethod.GET, resourceUri, resourceType)

        sendResourceListRequest(request, resourceType, callback)
    }

    // list
    private fun<T: Resource> getResourcesAsync (resourceUri: UrlLink, resourceType: ResourceType) : Deferred<ResourceListResponse<T>> {

        val request = createRequest(ApiValues.HttpMethod.GET, resourceUri, resourceType)

        return sendAsync(request, resourceType)
    }

    // create
    private fun<T: Resource> create (resourceUri: UrlLink, resourceType: ResourceType, jsonBody: String, additionalHeaders: Headers? = null, callback: (ResourceResponse<T>) -> Unit) {

        val request = createRequest(ApiValues.HttpMethod.POST, resourceUri, resourceType, additionalHeaders, jsonBody)

        sendResourceRequest(request, resourceType, callback)
    }

    // create
    private fun<T: Resource> createAsync (resourceUri: UrlLink, resourceType: ResourceType, additionalHeaders: Headers, jsonBody: String? = null) : Deferred<ResourceListResponse<T>> {

        val request = createRequest(ApiValues.HttpMethod.POST, resourceUri, resourceType, additionalHeaders, jsonBody)

        return sendAsync(request, resourceType)
    }

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

            println(e)

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
            if (ContextProvider.verboseLogging) {
                print(e)
            }

            callback(ResourceResponse(DataError(e)))
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

                        override fun onFailure(call: Call, e: IOException) {
                            // Error
                            return callback(ResourceListResponse(DataError(e)))
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {

                            return callback(processListResponse(request, response, resourceType))
                        }
                    })
        }
        catch (e: Exception) {
            if (ContextProvider.verboseLogging) {
                print(e)
            }

            callback(ResourceListResponse(DataError(e)))
        }
    }

    fun<T: Resource> processResponse(request: Request, response: Response, resourceType: ResourceType) : ResourceResponse<T> {

        try {
            val body = response.body() ?: return ResourceResponse(DataError("Empty response body received"))

            val json = body.string()

            if (ContextProvider.verboseLogging) {
                print(json)
            }

            val resource = JsonHelper.Gson.fromJson<T>(json, resourceType.type) ?: return ResourceResponse(json.toError())

            val result = Result(resource)

            return ResourceResponse(request, response, json, result)
        }
        catch (e: Exception) {
            return ResourceResponse(DataError(e))
        }
    }

    fun<T: Resource> processListResponse(request: Request, response: Response, resourceType: ResourceType) : ResourceListResponse<T> {

        try {
            val body = response.body() ?: return ResourceListResponse(DataError("Empty response body received"))

            val json = body.string()

            if (ContextProvider.verboseLogging) {
                print(json)
            }

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
        catch (e: Exception) {
            return ResourceListResponse(DataError(e))
        }
    }

    companion object {

        val client = OkHttpClient()

        val invalidIdError : DataError = DataError("Cosmos DB Resource IDs must not exceed 255 characters and cannot contain whitespace")

        val jsonMediaType = MediaType.parse(ApiValues.HttpRequestHeaderValue.ACCEPT_JSON.value)
    }
}