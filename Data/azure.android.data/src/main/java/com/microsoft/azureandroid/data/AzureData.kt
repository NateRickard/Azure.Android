package com.microsoft.azureandroid.data

import android.content.Context
import com.microsoft.azureandroid.data.constants.TokenType
import com.microsoft.azureandroid.data.model.*
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
    fun createDatabase ( databaseId: String, callback: (ResourceResponse<Database>) -> Unit) =
            cosmosService.createDatabase (databaseId, callback)

    // list
    fun databases(callback: (ResourceListResponse<Database>) -> Unit) =
            cosmosService.databases(callback)

    fun getDatabase(databaseId: String, callback: (ResourceResponse<Database>) -> Unit) =
            cosmosService.getDatabase (databaseId, callback)

    // delete
    fun deleteDatabase (database: Database, callback: (Boolean) -> Unit) =
            cosmosService.deleteDatabase (database.id, callback)

    // delete
    fun deleteDatabase (databaseId: String, callback: (Boolean) -> Unit) =
            cosmosService.deleteDatabase (databaseId, callback)


    // Collections

    // create
    fun createCollection (collectionId: String, databaseId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) =
            cosmosService.createCollection (collectionId, databaseId, callback)

    // list
    fun getCollectionsIn (databaseId: String, callback: (ResourceListResponse<DocumentCollection>) -> Unit) =
            cosmosService.getCollectionsIn(databaseId, callback)

    // delete
    fun deleteCollection (collection: DocumentCollection, databaseId: String, callback: (Boolean) -> Unit) =
            cosmosService.deleteCollection (collection.id, databaseId, callback)

    // delete
    fun deleteCollection (collectionId: String, databaseId: String, callback: (Boolean) -> Unit) =
            cosmosService.deleteCollection (collectionId, databaseId, callback)


    // Documents

    // list
    fun<T: Document> getDocumentsAs ( collectionId: String, databaseId: String, callback: (ResourceListResponse<T>) -> Unit) =
            cosmosService.getDocumentsAs(collectionId, databaseId, callback)

    // list
    fun<T: Document> getDocumentsAs (collection: DocumentCollection, callback: (ResourceListResponse<T>) -> Unit) =
            cosmosService.getDocumentsAs(collection, callback)


    // Offers

    // list
    fun offers(callback: (ResourceListResponse<Offer>) -> Unit) =
            cosmosService.offers(callback)

    // get
    fun getOffer (offerId: String, callback: (ResourceResponse<Offer>) -> Unit) =
            cosmosService.getOffer(offerId, callback)


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