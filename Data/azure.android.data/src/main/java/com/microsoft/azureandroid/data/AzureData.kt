package com.microsoft.azureandroid.data

import android.content.Context
import com.microsoft.azureandroid.data.constants.TokenType
import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroid.data.model.ResourceUri
import com.microsoft.azureandroid.data.services.CosmosService
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroid.data.util.ContextProvider

/**
 * Created by nater on 10/24/17.
 */

class AzureData {

    init {
    }

    // Databases

    // create
    fun createDatabase ( databaseId: String, callback: (ResourceResponse<Database>) -> Unit) {
        return cosmosService.createDatabase (databaseId, callback)
    }

    // list

    fun databases(callback: (ResourceListResponse<Database>) -> Unit) {
        return cosmosService.databases(callback)
    }


    // Collections

    // create
    fun createCollection (collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {
        return cosmosService.createCollection (collectionId, databaseId, callback)
    }

    // list
    fun getCollectionsIn (databaseId: String, callback: (ResourceListResponse<DocumentCollection>) -> Unit) {
        return cosmosService.getCollectionsIn(databaseId, callback)
    }

    // delete
    fun deleteCollection (collection: DocumentCollection, databaseId: String, callback: (Boolean) -> Unit) {
        return cosmosService.deleteCollection (collection.id, databaseId, callback)
    }

    // delete
    fun deleteCollection (collectionId: String, databaseId: String, callback: (Boolean) -> Unit) {
        return cosmosService.deleteCollection (collectionId, databaseId, callback)
    }


    // Documents

    // list

    fun<T: Document> getDocumentsAs ( collectionId: String, databaseId: String, callback: (ResourceListResponse<T>) -> Unit) {
        return cosmosService.getDocumentsAs(collectionId, databaseId, callback)
    }

    fun<T: Document> getDocumentsAs (collection: DocumentCollection, callback: (ResourceListResponse<T>) -> Unit) {
        return cosmosService.getDocumentsAs(collection, callback)
    }


    companion object {

        val instance: AzureData by lazy {
            AzureData()
        }

        lateinit var baseUri: ResourceUri
        lateinit var cosmosService: CosmosService

        fun init(context: Context, name: String, key: String, keyType: TokenType = TokenType.MASTER, verboseLogging: Boolean = false) {

            ContextProvider.init(context.applicationContext, verboseLogging)

            baseUri = ResourceUri(name)
            cosmosService = CosmosService(baseUri, key, keyType)
        }
    }
}