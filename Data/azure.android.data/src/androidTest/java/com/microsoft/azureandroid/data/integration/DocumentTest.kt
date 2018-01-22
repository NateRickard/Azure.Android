package com.microsoft.azureandroid.data.integration

import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.delete
import com.microsoft.azureandroid.data.model.*
import com.microsoft.azureandroid.data.refresh
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroid.data.util.json.gson
import junit.framework.Assert.*
import org.awaitility.Awaitility.await
import org.junit.Test
import java.util.*

/**
 * Created by Nate Rickard on 1/17/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
 */

abstract class DocumentTest<TDoc : Document>(private val docType: Class<TDoc>)
    : ResourceTest<TDoc>(ResourceType.Document, true, true) {

    //region Tests

    @Test
    fun testDocumentDateHandling() {

        val newDocument = newDocument()
        newDocument.setValue(customDateKey, customDateValue)

        val json = gson.toJson(newDocument)
        val doc = gson.fromJson(json, docType)

        val date = doc.getValue(customDateKey) as Date

        assertEquals(customDateValue, date)
        assertEquals(customDateValue.time, date.time)
    }

    @Test
    fun testDocumentDataMapHandling() {

        val newDocument = newDocument()

        newDocument.setValue(customStringKey, customStringValue)
        newDocument.setValue(customNumberKey, customNumberValue)
        newDocument.setValue(customBoolKey, true)
        newDocument.setValue(customArrayKey, customArrayValue)
        newDocument.setValue(customDateKey, customDateValue)
        newDocument.setValue(customObjectKey, customObjectValue)

        val json = gson.toJson(newDocument)

        val doc = gson.fromJson(json, docType)

        assertEquals(customStringValue, doc.getValue(customStringKey))
        assertEquals(customNumberValue, (doc.getValue(customNumberKey) as Number).toInt())
        assertEquals(true, doc.getValue(customBoolKey))

        val date = doc.getValue(customDateKey) as Date

        assertEquals(customDateValue, date)
        assertEquals(customDateValue.time, date.time)

        val list = doc.getValue(customArrayKey)

        if (list is ArrayList<*>) {

            assertEquals(customArrayValue.size, list.size)

            list.forEachIndexed { index, any ->
                assertEquals(customArrayValue[index], (any as Number).toInt())
            }
        }
        else if (list is Array<*>) {

            assertEquals(customArrayValue.size, list.size)

            list.forEachIndexed { index, any ->
                assertEquals(customArrayValue[index], (any as Number).toInt())
            }
        }

        val userObj = doc.getValue(customObjectKey)

        if (userObj is User) {
            assertEquals(customObjectValue.id, userObj.id)
        }
        else {
            assertEquals(customObjectValue.id, (userObj as Map<*, *>)["id"])
        }
    }

    @Test
    fun createDocWithInvalidIds() {

        val doc1 = newDocument()
        doc1.id = idWith256Chars //too long

        var doc1Response : ResourceResponse<TDoc>? = null
        var doc2Response : ResourceResponse<TDoc>? = null

        AzureData.createDocument(doc1, collectionId, databaseId) {
            doc1Response = it
        }

        val doc2 = newDocument()
        doc2.id = idWithWhitespace //no spaces allowed!

        AzureData.createDocument(doc2, collectionId, databaseId) {
            doc2Response = it
        }

        await().until {
            doc1Response != null && doc2Response != null
        }

        assertErrorResponse(doc1Response)
        assertErrorResponse(doc2Response)
    }

    @Test
    fun createDocument() {

        createNewDocument()
    }

    @Test
    fun createDocumentInCollection() {

        createNewDocument(collection)
    }

    @Test
    fun listDocuments() {

        //ensure at least 1 doc
        createNewDocument()

        AzureData.getDocuments(collectionId, databaseId, docType) {
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

        AzureData.queryDocuments(collectionId, databaseId, query, docType) {
            resourceListResponse = it
        }

        await().forever().until {
            resourceListResponse != null
        }

        verifyListDocuments()
    }

    @Test
    fun getDocument() {

        createNewDocument()

        AzureData.getDocument(resourceId, collectionId, databaseId, docType) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        val createdDoc = resourceResponse!!.resource!!
        verifyDocument(createdDoc)
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

        val createdDoc = resourceResponse!!.resource!!
        verifyDocument(createdDoc)
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

    //endregion

    private fun newDocument() : TDoc {

        val doc = docType.newInstance()
        doc.id = resourceId
        doc.setValue(customStringKey, customStringValue)
        doc.setValue(customNumberKey, customNumberValue)

        return doc as TDoc
    }

    private fun createNewDocument(coll: DocumentCollection? = null) : TDoc {

        var docResponse: ResourceResponse<TDoc>? = null
        val doc = newDocument()

        if (coll != null) {
            AzureData.createDocument(doc, coll) {
                docResponse = it
            }
        } else {
            AzureData.createDocument(doc, collectionId, databaseId) {
                docResponse = it
            }
        }

        await().until {
            docResponse != null
        }

        assertResponseSuccess(docResponse)
        assertEquals(resourceId, docResponse?.resource?.id)

        val createdDoc = docResponse!!.resource!!

        return verifyDocument(createdDoc)
    }

    private fun verifyDocument(createdDoc: TDoc) : TDoc {

        assertNotNull(createdDoc.getValue(customStringKey))
        assertNotNull(createdDoc.getValue(customNumberKey))
        assertEquals(customStringValue, createdDoc.getValue(customStringKey))
        assertEquals(customNumberValue, (createdDoc.getValue(customNumberKey) as Number).toInt())

        return createdDoc
    }

    private fun verifyListDocuments() {

        assertResponseSuccess(resourceListResponse)
        assertTrue(resourceListResponse?.resource?.count!! > 0)

        resourceListResponse?.resource?.items?.forEach {

            verifyDocument(it)
        }
    }

    fun Document.getValue(key: String) : Any? {

        return when {
            this is DictionaryDocument -> return this[key]
            this is CustomDocument -> return when (key) {
                customStringKey -> this.customString
                customNumberKey -> this.customNumber
                customDateKey -> this.customDate
                customBoolKey -> this.customBool
                customArrayKey -> this.customArray
                customObjectKey -> this.customObject
                else -> null
            }
            else -> null
        }
    }

    fun Document.setValue(key: String, value: Any?) {

        when {
            this is DictionaryDocument -> this[key] = value
            this is CustomDocument -> when (key) {
                customStringKey -> this.customString = value as String
                customNumberKey -> this.customNumber = value as Int
                customDateKey -> this.customDate = value as Date
                customBoolKey -> this.customBool = value as Boolean
                customArrayKey -> this.customArray = value as Array<Int>
                customObjectKey -> this.customObject = value as User
            }
        }
    }

    companion object {

        const val customStringKey = "customString"
        const val customStringValue = "Yeah baby\nRock n Roll"
        const val customNumberKey = "customNumber"
        const val customNumberValue = 86
        const val customDateKey = "customDate"
        val customDateValue : Date
        const val customBoolKey = "customBool"
        const val customArrayKey = "customArray"
        val customArrayValue = arrayOf(1, 2, 3, 4)
        const val customObjectKey = "customObject"
        val customObjectValue = User()

        init {
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, 1988)
            cal.set(Calendar.MONTH, Calendar.JANUARY)
            cal.set(Calendar.DAY_OF_MONTH, 1)
            customDateValue = cal.time
        }
    }
}