package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.*
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroid.data.util.json.gson
import org.awaitility.Awaitility
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by Nate Rickard on 1/8/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class CustomDocumentTests : ResourceTest<CustomDocumentTests.MyDocument>(ResourceType.Document, true, true) {

    private val customStringValue = "Yeah baby\nRock n Roll"
    private val customNumberValue = 86
    private val customDateValue : Date

    init {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, 1988)
        cal.set(Calendar.MONTH, Calendar.JANUARY)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        customDateValue = cal.time
    }

    class MyDocument(id: String) : Document(id) {

        var customString = "My Custom String"
        var customNumber = 0
        var customDate: Date = Date()
    }

    private fun createNewCustomDocument(coll: DocumentCollection? = null) : MyDocument {

        val newDocument = MyDocument(resourceId)
        newDocument.customString = customStringValue
        newDocument.customNumber = customNumberValue

        if (coll != null) {
            AzureData.createDocument(newDocument, coll) {
                resourceResponse = it
            }
        } else {
            AzureData.createDocument(newDocument, collectionId, databaseId) {
                resourceResponse = it
            }
        }

        Awaitility.await().until {
            resourceResponse != null
        }

        return verifyDocument()
    }

    private fun verifyDocument() : MyDocument {

        assertResponseSuccess(resourceResponse)
        Assert.assertEquals(resourceId, resourceResponse?.resource?.id)

        val createdDoc = resourceResponse!!.resource!!

        Assert.assertNotNull(createdDoc.customString)
        Assert.assertNotNull(createdDoc.customNumber)
        Assert.assertEquals(customStringValue, createdDoc.customString)
        Assert.assertEquals(customNumberValue, createdDoc.customNumber)

        return createdDoc
    }

    private fun verifyListDocuments() {

        assertResponseSuccess(resourceListResponse)
        Assert.assertTrue(resourceListResponse?.resource?.count!! > 0)

        resourceListResponse?.resource?.items?.forEach {
            Assert.assertNotNull(it.customString)
            Assert.assertNotNull(it.customNumber)
            Assert.assertEquals(customStringValue, it.customString)
            Assert.assertEquals(customNumberValue, it.customNumber)
        }
    }

    @Test
    fun testDocumentDateHandling() {

        val newDocument = MyDocument(resourceId)
        newDocument.customDate = customDateValue

        val json = gson.toJson(newDocument)
        val doc = gson.fromJson(json, MyDocument::class.java)

        val date = doc.customDate

        Assert.assertEquals(customDateValue, date)
        Assert.assertEquals(customDateValue.time, date.time)
    }

    @Test
    fun createDocWithInvalidIds() {

        val doc1 = MyDocument(idWith256Chars) //too long
        var doc1Response : ResourceResponse<MyDocument>? = null
        var doc2Response : ResourceResponse<MyDocument>? = null

        AzureData.createDocument(doc1, collectionId, databaseId) {
            doc1Response = it
        }

        val doc2 = MyDocument(idWithWhitespace) //no spaces allowed!

        AzureData.createDocument(doc2, collectionId, databaseId) {
            doc2Response = it
        }

        Awaitility.await().until {
            doc1Response != null && doc2Response != null
        }

        assertResponseFailure(doc1Response)
        assertResponseFailure(doc2Response)
    }

    @Test
    fun testCreateCustomDocument() {

        createNewCustomDocument()
    }

    @Test
    fun createDocumentInCollection() {

        createNewCustomDocument(collection)
    }

    @Test
    fun listDocuments() {

        //ensure at least 1 doc
        createNewCustomDocument()

        AzureData.getDocuments(collectionId, databaseId, MyDocument::class.java) {
            resourceListResponse = it
        }

        Awaitility.await().until {
            resourceListResponse != null
        }

        verifyListDocuments()
    }

    @Test
    fun queryDocuments() {

        //ensure at least 1 doc
        createNewCustomDocument()

        val query = Query.select()
                .from(collectionId)
                .where("customString", customStringValue)
                .andWhere("customNumber", customNumberValue)
                .orderBy("_etag", true)

        AzureData.queryDocuments(collectionId, databaseId, query, MyDocument::class.java) {
            resourceListResponse = it
        }

        Awaitility.await().until {
            resourceListResponse != null
        }

        verifyListDocuments()
    }
}