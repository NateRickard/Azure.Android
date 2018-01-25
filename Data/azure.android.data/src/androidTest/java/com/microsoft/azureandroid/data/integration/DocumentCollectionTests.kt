package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.*
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroid.data.model.ResourceType
import org.awaitility.Awaitility.await
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Nate Rickard on 12/11/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class DocumentCollectionTests : ResourceTest<DocumentCollection>(ResourceType.Collection, true, false) {

    @Test
    fun createCollection() {

        ensureCollection()
    }

    @Test
    fun createCollectionFromDatabase() {

        database?.createCollection(resourceId) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        assertEquals(resourceId, resourceResponse?.resource?.id)
    }

    @Test
    fun listCollections() {

        ensureCollection()

        AzureData.getCollections(databaseId) {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assertTrue(resourceListResponse?.resource?.count!! > 0)
    }

    @Test
    fun listCollectionsFromDatabase() {

        ensureCollection()

        database?.getCollections {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assertTrue(resourceListResponse?.resource?.count!! > 0)
    }

    @Test
    fun getCollection() {

        ensureCollection()

        AzureData.getCollection(resourceId, databaseId) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        assertEquals(resourceId, resourceResponse?.resource?.id)
    }

    @Test
    fun getCollectionFromDatabase() {

        ensureCollection()

        database?.getCollection(resourceId) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        assertEquals(resourceId, resourceResponse?.resource?.id)
    }

    @Test
    fun refreshCollection() {

        val coll = ensureCollection()

        coll.refresh {
            resourceResponse = it
        }

//        AzureData.refresh(coll) {
//            resourceResponse = it
//        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        assertEquals(collectionId, resourceResponse?.resource?.id)
    }

    @Test
    fun deleteCollection() {

        val coll = ensureCollection()

        coll.delete {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }

    @Test
    fun deleteCollectionByIds() {

        ensureCollection()

        AzureData.deleteCollection(collectionId, databaseId) {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }

    @Test
    fun deleteCollectionFromDatabase() {

        val coll = ensureCollection()

        database?.deleteCollection(coll) {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }

    @Test
    fun deleteCollectionFromDatabaseById() {

        ensureCollection()

        database?.deleteCollection(collectionId) {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }
}