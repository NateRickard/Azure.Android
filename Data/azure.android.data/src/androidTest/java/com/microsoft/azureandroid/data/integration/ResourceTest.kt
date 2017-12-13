package com.microsoft.azureandroid.data.integration

import android.support.test.InstrumentationRegistry
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.constants.TokenType
import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroid.data.model.Resource
import com.microsoft.azureandroid.data.model.ResourceType
import com.microsoft.azureandroid.data.services.DataResponse
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import org.awaitility.Awaitility.await
import org.junit.After
import org.junit.Assert.*
import org.junit.Before

/**
 * Created by Nate Rickard on 12/6/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

open class ResourceTest<TResource : Resource>(val resourceType: ResourceType, val ensureDatabase : Boolean = true, val ensureCollection : Boolean = true) {

    val databaseId = "AndroidTest${ResourceType.Database.name}"
    val collectionId = "AndroidTest${ResourceType.Collection.name}"
    val resourceId = "AndroidTest${resourceType.name}"

    var resourceResponse: ResourceResponse<TResource>? = null
    var resourceListResponse: ResourceListResponse<TResource>? = null
    var dataResponse: DataResponse? = null

    var collection: DocumentCollection? = null

    val idWith256Chars = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345"
    val idWithWhitespace = "id value with spaces"

    @Before
    open fun setUp() {

        if (!AzureData.isSetup) {
            // Context of the app under test.
            val appContext = InstrumentationRegistry.getTargetContext()

            AzureData.init(appContext, "mobile", "gioHmSqPP7J7FE5XlqRgBjmqykWLbm0KnP2FCAOl7gu17ZWlvMTRxOvsUYWQ3YUN2Yvmd077O0hyFyBOIftjOg==", TokenType.MASTER, true)
        }

        deleteResources()

        if (ensureDatabase) {
            ensureDatabase()
        }

        if (ensureCollection) {
            ensureCollection()
        }
    }

    @After
    open fun tearDown() {

        deleteResources()
    }

    fun ensureDatabase() : Database {

        var dbResponse: ResourceResponse<Database>? = null

        AzureData.createDatabase(databaseId) {
            dbResponse = it
        }

        await().until {
            dbResponse != null
        }

        assertResponseSuccess(dbResponse)
        assertEquals(databaseId, dbResponse?.resource?.id)

        return dbResponse!!.resource!!
    }

    fun ensureCollection() : DocumentCollection {

        var collectionResponse: ResourceResponse<DocumentCollection>? = null

        AzureData.createCollection(collectionId, databaseId) {
            collectionResponse = it
        }

        await().until {
            collectionResponse != null
        }

        assertResponseSuccess(collectionResponse)
        assertEquals(collectionId, collectionResponse?.resource?.id)

        collection = collectionResponse!!.resource!!

        return collection!!
    }

    private fun deleteResources() {

        var deleteResponse: DataResponse? = null

        AzureData.deleteCollection(collectionId, databaseId) { response ->
            println("Attempted to delete test collection.  Result: ${response.isSuccessful}")
            deleteResponse = response
        }

        await().until {
            deleteResponse != null
        }

        deleteResponse = null

        AzureData.deleteDatabase(databaseId) { response ->
            println("Attempted to delete test database.  Result: ${response.isSuccessful}")
            deleteResponse = response
        }

        await().until {
            deleteResponse != null
        }
    }

    fun assertResponseSuccess(response: ResourceResponse<*>?) {

        assertNotNull(response)
        assertNotNull(response!!.resource)
        assertTrue(response.isSuccessful)
        assertFalse(response.isErrored)
    }

    fun assertResponseFailure(response: ResourceResponse<*>?) {

        assertNotNull(response)
        assertNotNull(response!!.error)
        assertFalse(response.isSuccessful)
        assertTrue(response.isErrored)
    }

    fun assertResponseSuccess(response: ResourceListResponse<*>?) {

        assertNotNull(response)
        assertNotNull(response!!.resource)
        assertTrue(response.isSuccessful)
        assertFalse(response.isErrored)
    }

    fun assertResponseSuccess(response: DataResponse?) {

        assertNotNull(response)
        assertTrue(response!!.isSuccessful)
        assertFalse(response.isErrored)
    }
}