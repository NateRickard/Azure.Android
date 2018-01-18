package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.*
import com.microsoft.azureandroid.data.model.*
import com.microsoft.azureandroid.data.services.ResourceResponse
import junit.framework.Assert.assertTrue
import okhttp3.OkHttpClient
import okhttp3.Request
import org.awaitility.Awaitility.await
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

/**
 * Created by Nate Rickard on 1/16/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class AttachmentTests : ResourceTest<Attachment>(ResourceType.Attachment, true, true, true) {

    private val url: URL = URL("https", "azuredatatests.blob.core.windows.net", "/attachment-tests/youre%20welcome.jpeg?st=2017-11-07T14%3A00%3A00Z&se=2020-11-08T14%3A00%3A00Z&sp=rl&sv=2017-04-17&sr=c&sig=RAHr6Mee%2Bt7RrDnGHyjgSX3HSqJgj8guhy0IrEMh3KQ%3D")

    fun getImageData() : ByteArray {

        val client = OkHttpClient()

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val response = client.newCall(request).execute()

        return response.body()!!.bytes()
    }

    private fun createNewAttachment(imageData: ByteArray? = null, doc: Document? = null) : Attachment {

        var response: ResourceResponse<Attachment>? = null

        if (imageData != null) {
            if (doc != null) {
                document?.createAttachment(resourceId, "image/jpeg", imageData) {
                    response = it
                }
            }
            else {
                AzureData.createAttachment(resourceId, "image/jpeg", resourceId, imageData, documentId, collectionId, databaseId) {
                    response = it
                }
            }
        }
        else {
            if (doc != null) {
                document?.createAttachment(resourceId, "image/jpeg", url) {
                    response = it
                }
            }
            else {
                AzureData.createAttachment(resourceId, "image/jpeg", url, documentId, collectionId, databaseId) {
                    response = it
                }
            }
        }

        await().forever().until {
            response != null
        }

        assertResponseSuccess(response)
        Assert.assertEquals(resourceId, response?.resource?.id)

        return response!!.resource!!
    }

    @Test
    fun createAttachment() {

        createNewAttachment()
    }

    @Test
    fun createAttachmentForDocument() {

        createNewAttachment(null, document)
    }

    @Test
    fun createBlobAttachment() {

        val bytes = getImageData()

        createNewAttachment(bytes)
    }

    @Test
    fun createBlobAttachmentForDocument() {

        val bytes = getImageData()

        createNewAttachment(bytes, document)
    }

    @Test
    fun listAttachments() {

        //ensure at least 1 attachment
        createNewAttachment()

        AzureData.getAttachments(documentId, collectionId, databaseId) {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assertTrue(resourceListResponse?.resource?.count!! > 0)
    }

    @Test
    fun listAttachmentsForDocument() {

        //ensure at least 1 attachment
        createNewAttachment(null, document)

        document?.getAttachments {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assertTrue(resourceListResponse?.resource?.count!! > 0)
    }

    @Test
    fun replaceAttachment() {

        createNewAttachment()

        AzureData.replaceAttachment(resourceId, "image/jpeg", url, documentId, collectionId, databaseId) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        Assert.assertEquals(resourceId, resourceResponse?.resource?.id)
    }

    @Test
    fun replaceAttachmentForDocument() {

        val attachment = createNewAttachment(null, document)

        document?.replaceAttachment(resourceId, attachment.resourceId, "image/jpeg", url) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        Assert.assertEquals(resourceId, resourceResponse?.resource?.id)
    }

    @Test
    fun deleteAttachmentById() {

        createNewAttachment()

        AzureData.deleteAttachment(resourceId, documentId, collectionId, databaseId) {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }

    @Test
    fun deleteAttachmentFromDocument() {

        val attachment = createNewAttachment(null, document)

        document?.deleteAttachment(attachment) {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }

    @Test
    fun deleteAttachmentFromDocumentByRId() {

        val attachment = createNewAttachment(null, document)

        document?.deleteAttachment(attachment.resourceId) {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }
}