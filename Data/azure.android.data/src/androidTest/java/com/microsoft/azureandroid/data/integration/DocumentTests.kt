package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.delete
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroid.data.model.Query
import com.microsoft.azureandroid.data.model.ResourceType
import com.microsoft.azureandroid.data.refresh
import com.microsoft.azureandroid.data.services.ResourceResponse
import org.awaitility.Awaitility.await
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Nate Rickard on 12/12/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class DocumentTests : ResourceTest<Document>(ResourceType.Document, true, true) {

    val customStringKey = "customStringKey"
    val customStringValue = "customStringValue"
    val customNumberKey = "customNumberKey"
    val customNumberValue = 86

    @Test
    fun createDocWithInvalidIds() {

        val doc1 = Document(idWith256Chars) //too long
        var doc1Response : ResourceResponse<Document>? = null
        var doc2Response : ResourceResponse<Document>? = null

        AzureData.createDocument(doc1, collectionId, databaseId) {
            doc1Response = it
        }

        val doc2 = Document(idWithWhitespace) //no spaces allowed!

        AzureData.createDocument(doc2, collectionId, databaseId) {
            doc2Response = it
        }

        await().until {
            doc1Response != null && doc2Response != null
        }

        assertResponseFailure(doc1Response)
        assertResponseFailure(doc2Response)
    }

    @Test
    fun createDocument() {

        createNewDocument()
    }

    @Test
    fun createDocumentInCollection() {

        createNewDocument(collection)
    }

    private fun createNewDocument(coll: DocumentCollection? = null) : Document {

        val newDocument = Document(resourceId)

        newDocument[customStringKey] = customStringValue
        newDocument[customNumberKey] = customNumberValue

        if (coll != null) {
            AzureData.createDocument(newDocument, coll) {
                resourceResponse = it
            }
        } else {
            AzureData.createDocument(newDocument, collectionId, databaseId) {
                resourceResponse = it
            }
        }

        await().forever().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        assertEquals(resourceId, resourceResponse?.resource?.id)

        val createdDoc = resourceResponse!!.resource!!

        assertNotNull(createdDoc[customStringKey])
        assertNotNull(createdDoc[customNumberKey])
        assertEquals(customStringValue, createdDoc[customStringKey])
        assertEquals(customNumberValue, (createdDoc[customNumberKey] as Double).toInt())

        return createdDoc
    }

    @Test
    fun listDocuments() {

        //ensure at least 1 doc
        createNewDocument()

        AzureData.getDocumentsAs<Document>(collectionId, databaseId) {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assertTrue(resourceListResponse?.resource?.count!! > 0)
    }

    @Test
    fun queryDocuments() {

        //ensure at least 1 doc
        createNewDocument()

        val query = Query.select()
                .from(collectionId)
                .where(customStringKey, customStringValue)
                .andWhere(customNumberKey, customNumberValue)
        .orderBy("_etag", true)

        AzureData.queryDocuments(collectionId, databaseId, query) {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assertTrue(resourceListResponse?.resource?.count!! > 0)

        resourceListResponse?.resource?.items?.forEach {
            assertEquals(customStringValue, it[customStringKey])
            assertEquals(customNumberValue, (it[customNumberKey] as Double).toInt())
        }
    }

    @Test
    fun getDocument() {

        createNewDocument()

        AzureData.getDocument<Document>(resourceId, collectionId, databaseId) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        assertEquals(resourceId, resourceResponse?.resource?.id)

        val doc = resourceResponse!!.resource!!

        assertNotNull(doc[customStringKey])
        assertNotNull(doc[customNumberKey])
        assertEquals(customStringValue, doc[customStringKey])
        assertEquals(customNumberValue, (doc[customNumberKey] as Double).toInt())
    }

    @Test
    fun refreshDocument() {

        val doc = createNewDocument()

        doc.refresh {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        assertEquals(resourceId, resourceResponse?.resource?.id)

        assertNotNull(doc[customStringKey])
        assertNotNull(doc[customNumberKey])
        assertEquals(customStringValue, doc[customStringKey])
        assertEquals(customNumberValue, (doc[customNumberKey] as Double).toInt())
    }

    @Test
    fun deleteDocument() {

        val doc = createNewDocument()

        doc.delete {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }
}