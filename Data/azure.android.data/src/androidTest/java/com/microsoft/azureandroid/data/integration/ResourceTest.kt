package com.microsoft.azureandroid.data.integration

import android.support.test.InstrumentationRegistry
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.constants.TokenType
import com.microsoft.azureandroid.data.model.*
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

open class ResourceTest<TResource : Resource>(resourceType: ResourceType,
                                              private val ensureDatabase : Boolean = true,
                                              private val ensureCollection : Boolean = true,
                                              private val ensureDocument : Boolean = false) {

    val databaseId = "AndroidTest${ResourceType.Database.name}"
    val collectionId = "AndroidTest${ResourceType.Collection.name}"
    val documentId = "AndroidTest${ResourceType.Document.name}"
    val resourceId = "AndroidTest${resourceType.name}"

    var resourceResponse: ResourceResponse<TResource>? = null
    var resourceListResponse: ResourceListResponse<TResource>? = null
    var dataResponse: DataResponse? = null

    var database: Database? = null
    var collection: DocumentCollection? = null
    var document: Document? = null

    val idWith256Chars = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345"
    val idWithWhitespace = "id value with spaces"

    @Before
    open fun setUp() {

        println("********* Begin Test Setup *********")

        if (!AzureData.isConfigured) {
            // Context of the app under test.
            val appContext = InstrumentationRegistry.getTargetContext()

            AzureData.configure(appContext, "mobile", "gioHmSqPP7J7FE5XlqRgBjmqykWLbm0KnP2FCAOl7gu17ZWlvMTRxOvsUYWQ3YUN2Yvmd077O0hyFyBOIftjOg==", TokenType.MASTER, true)
        }

        deleteResources()

        if (ensureDatabase || ensureCollection || ensureDocument) {
            ensureDatabase()
        }

        if (ensureCollection || ensureDocument) {
            ensureCollection()
        }

        if (ensureDocument) {
            ensureDocument()
        }

        println("********* End Test Setup *********")
    }

    @After
    open fun tearDown() {

        println("********* Begin Test Tear Down *********")

        deleteResources()

        println("********* End Test Tear Down *********")
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

        database = dbResponse!!.resource!!

        return database!!
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

    fun ensureDocument() : Document {

        var docResponse: ResourceResponse<CustomDocument>? = null
        val doc = CustomDocument(documentId)

        AzureData.createDocument(doc, collection!!) {
            docResponse = it
        }

        await().until {
            docResponse != null
        }

        document = docResponse!!.resource!!

        return document!!
    }

    private fun deleteResources() {

        var deleteResponse: DataResponse? = null

        //delete the DB - this should delete all attached resources

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
        assertTrue(response!!.isSuccessful)
        assertFalse(response.isErrored)
        assertNotNull(response.resource)
        assertTrue(response.resource?.isPopuated!!)
    }

    fun assertResponseSuccess(response: DataResponse?) {

        assertNotNull(response)
        assertTrue(response!!.isSuccessful)
        assertFalse(response.isErrored)
    }
}