package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.delete
import com.microsoft.azureandroid.data.model.*
import com.microsoft.azureandroid.data.refresh
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroid.data.util.json.gson
import org.awaitility.Awaitility.await
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by Nate Rickard on 12/12/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class DocumentTests : ResourceTest<DictionaryDocument>(ResourceType.Document, true, true) {

    private val customStringKey = "customStringKey"
    private val customStringValue = "Yeah baby\nRock n Roll"
    private val customNumberKey = "customNumberKey"
    private val customNumberValue = 86
    private val customDateKey = "customDateKey"
    private val customDateValue = Date()
    private val customBoolKey = "customBoolKey"
    private val customArrayKey = "customArrayKey"
    private val customArrayValue = arrayOf(1, 2, 3, 4)
    private val customObjectKey = "customObjectKey"
    private val customObjectValue = User()

    @Test
    fun testDocumentDateHandling() {

        val newDocument = DictionaryDocument(resourceId)
        newDocument[customDateKey] = customDateValue

        val json = gson.toJson(newDocument)
        val doc = gson.fromJson(json, DictionaryDocument::class.java)

        val date = doc[customDateKey] as Date

        assertEquals(customDateValue, date)
        assertEquals(customDateValue.time, date.time)
    }

    @Test
    fun testDocumentDataMapHandling() {

        val newDocument = DictionaryDocument(resourceId)

        newDocument[customStringKey] = customStringValue
        newDocument[customNumberKey] = customNumberValue
        newDocument[customBoolKey] = true
        newDocument[customArrayKey] = customArrayValue
        newDocument[customDateKey] = customDateValue
        newDocument[customObjectKey] = customObjectValue

        val json = gson.toJson(newDocument)

        val doc = gson.fromJson(json, DictionaryDocument::class.java)

        assertEquals(customStringValue, doc[customStringKey])
        assertEquals(customNumberValue, (doc[customNumberKey] as Number).toInt())
        assertEquals(true, doc[customBoolKey])

        val date = doc[customDateKey] as Date

        assertEquals(customDateValue, date)
        assertEquals(customDateValue.time, date.time)

        val list = doc.data[customArrayKey] as ArrayList<*>

        assertEquals(customArrayValue.size, list.size)

        list.forEachIndexed { index, any ->
            assertEquals(customArrayValue[index], (any as Number).toInt())
        }

        assertEquals(customObjectValue.id, (doc.data[customObjectKey] as Map<*, *>)["id"])
    }

    @Test
    fun createDocWithInvalidIds() {

        val doc1 = DictionaryDocument(idWith256Chars) //too long
        var doc1Response : ResourceResponse<DictionaryDocument>? = null
        var doc2Response : ResourceResponse<DictionaryDocument>? = null

        AzureData.createDocument(doc1, collectionId, databaseId) {
            doc1Response = it
        }

        val doc2 = DictionaryDocument(idWithWhitespace) //no spaces allowed!

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

    private fun createNewDocument(coll: DocumentCollection? = null) : DictionaryDocument {

        val newDocument = DictionaryDocument(resourceId)

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

        await().until {
            resourceResponse != null
        }

        return verifyDocument()
    }

    private fun verifyDocument() : DictionaryDocument {

        assertResponseSuccess(resourceResponse)
        assertEquals(resourceId, resourceResponse?.resource?.id)

        val createdDoc = resourceResponse!!.resource!!

        assertNotNull(createdDoc[customStringKey])
        assertNotNull(createdDoc[customNumberKey])
        assertEquals(customStringValue, createdDoc[customStringKey])
        assertEquals(customNumberValue, (createdDoc[customNumberKey] as Number).toInt())

        return createdDoc
    }

    private fun verifyListDocuments() {

        assertResponseSuccess(resourceListResponse)
        assertTrue(resourceListResponse?.resource?.count!! > 0)

        resourceListResponse?.resource?.items?.forEach {
            assertNotNull(it[customStringKey])
            assertNotNull(it[customNumberKey])
            assertEquals(customStringValue, it[customStringKey])
            assertEquals(customNumberValue, (it[customNumberKey] as Number).toInt())
        }
    }

    @Test
    fun listDocuments() {

        //ensure at least 1 doc
        createNewDocument()

        AzureData.getDocumentsAs(collectionId, databaseId, DictionaryDocument::class.java) {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        verifyListDocuments()
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

        AzureData.queryDocuments(collectionId, databaseId, query, DictionaryDocument::class.java) {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        verifyListDocuments()
    }

    @Test
    fun getDocument() {

        createNewDocument()

        AzureData.getDocument(resourceId, collectionId, databaseId, DictionaryDocument::class.java) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        verifyDocument()
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

        verifyDocument()
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