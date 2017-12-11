package com.microsoft.azureandroid.data.integration

import android.support.test.InstrumentationRegistry
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.constants.TokenType
import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroid.data.model.Resource
import com.microsoft.azureandroid.data.model.ResourceType
import com.microsoft.azureandroid.data.services.DataResponse
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import org.awaitility.Awaitility
import org.junit.Assert.*
import org.junit.Before

/**
 * Created by Nate Rickard on 12/6/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

open class ResourceTest<TResource : Resource>(val resourceType: ResourceType, val ensureDatabase : Boolean = true, val ensureCollection : Boolean = true) {

    val databaseId = "AndroidTest${ResourceType.DATABASE.name}"
    val resourceId = "AndroidTest${resourceType.name}"

    var resourceResponse: ResourceResponse<TResource>? = null
    var resourceListResponse: ResourceListResponse<TResource>? = null
    var dataResponse: DataResponse? = null

    val idWith256Chars = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345"
    val idWithWhitespace = "id value with spaces"

    @Before
    open fun setUp() {

        if (!AzureData.isSetup) {
            // Context of the app under test.
            val appContext = InstrumentationRegistry.getTargetContext()

            AzureData.init(appContext, "mobile", "gioHmSqPP7J7FE5XlqRgBjmqykWLbm0KnP2FCAOl7gu17ZWlvMTRxOvsUYWQ3YUN2Yvmd077O0hyFyBOIftjOg==", TokenType.MASTER, true)
        }

        var ops = 0

        ops++

        AzureData.instance.deleteDatabase(databaseId) { response ->
            println("Attempted to delete test database.  Result: ${response.isSuccessful}")
            ops--
        }

        if (ensureDatabase) {
            ensureDatabase()
        }

        Awaitility.await().until {
            ops == 0
        }

//        AzureData.instance.getDatabase(resourceId) { response ->
//
//            if (response.isSuccessful && response.resource != null) {
//
//            }
//        }
    }

    fun ensureDatabase() : Database {

        var dbResponse: ResourceResponse<Database>? = null

        AzureData.instance.createDatabase(databaseId) {
            dbResponse = it
        }

        Awaitility.await().until {
            dbResponse != null
        }

        assertResponseSuccess(dbResponse)
        assertEquals(databaseId, dbResponse?.resource?.id)

        return dbResponse!!.resource!!
    }

    fun assertResponseSuccess(response: ResourceResponse<*>?) {

        assertNotNull(response)
        assertNotNull(response!!.resource)
        assertTrue(response.isSuccessful)
        assertFalse(response.isErrored)
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