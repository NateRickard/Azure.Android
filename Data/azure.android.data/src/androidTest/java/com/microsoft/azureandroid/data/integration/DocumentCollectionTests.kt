package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.delete
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroid.data.model.ResourceType
import com.microsoft.azureandroid.data.refresh
import org.awaitility.Awaitility.await
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Nate Rickard on 12/11/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class DocumentCollectionTests : ResourceTest<DocumentCollection>(ResourceType.COLLECTION, true, false) {

    @Test
    fun createCollection() {

        ensureCollection()
    }

    @Test
    fun listCollections() {

        AzureData.instance.getCollections(databaseId) {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assert(resourceListResponse?.resource?.count!! > 0)
    }

    @Test
    fun getCollection() {

        ensureCollection()

        AzureData.instance.getCollection(resourceId, databaseId) {
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

//        AzureData.instance.refresh(coll) {
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
}
