package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.delete
import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroid.data.model.ResourceType
import com.microsoft.azureandroid.data.refresh
import org.junit.Test
import org.junit.runner.RunWith
import org.awaitility.Awaitility.*
import org.junit.Assert.*

/**
 * Created by Nate Rickard on 12/5/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class DatabaseTests : ResourceTest<Database>(ResourceType.Database, false, false) {

    @Test
    fun createDatabase() {

        ensureDatabase()
    }

    @Test
    fun listDatabases() {

        ensureDatabase()

        AzureData.databases {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assertTrue(resourceListResponse?.resource?.count!! > 0)
    }

    @Test
    fun getDatabase() {

        ensureDatabase()

        AzureData.getDatabase(databaseId) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        assertEquals(databaseId, resourceResponse?.resource?.id)
    }

    @Test
    fun refreshDatabase() {

        val db = ensureDatabase()

        db.refresh {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        assertEquals(databaseId, resourceResponse?.resource?.id)
    }

    @Test
    fun deleteDatabaseById() {

        ensureDatabase()

        AzureData.deleteDatabase(databaseId) {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }

    @Test
    fun deleteDatabase() {

        val db = ensureDatabase()

        AzureData.deleteDatabase(db) {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }

    @Test
    fun deleteDatabaseWithExtension() {

        val db = ensureDatabase()

        db.delete {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }
}