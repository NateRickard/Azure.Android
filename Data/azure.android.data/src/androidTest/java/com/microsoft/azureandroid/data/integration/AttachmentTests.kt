package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.delete
import com.microsoft.azureandroid.data.model.*
import junit.framework.Assert.assertTrue
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

    private fun createNewAttachment() : Attachment {

        AzureData.createAttachment(resourceId, "image/jpeg", url, documentId, collectionId, databaseId) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        Assert.assertEquals(resourceId, resourceResponse?.resource?.id)

        return resourceResponse!!.resource!!
    }

    @Test
    fun createAttachment() {

        createNewAttachment()
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
    fun deleteAttachment() {

        val attachment = createNewAttachment()

        attachment.delete {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }
}