package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.delete
import com.microsoft.azureandroid.data.model.Permission
import com.microsoft.azureandroid.data.model.ResourceType
import com.microsoft.azureandroid.data.refresh
import org.awaitility.Awaitility
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Nate Rickard on 12/11/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class PermissionTests : ResourceTest<Permission>(ResourceType.Permission, true, false) {

    @Test
    fun createPermission() {

//        ensureCollection()
    }

    @Test
    fun listPermissions() {

//        AzureData.instance.getPermissions(databaseId) {
//            resourceListResponse = it
//        }
//
//        Awaitility.await().until {
//            resourceListResponse != null
//        }
//
//        assertResponseSuccess(resourceListResponse)
//        assert(resourceListResponse?.resource?.count!! > 0)
    }

    @Test
    fun getPermission() {

//        ensureCollection()

//        AzureData.instance.getCollection(resourceId, databaseId) {
//            resourceResponse = it
//        }
//
//        Awaitility.await().until {
//            resourceResponse != null
//        }
//
//        assertResponseSuccess(resourceResponse)
//        Assert.assertEquals(resourceId, resourceResponse?.resource?.id)
    }

    @Test
    fun refreshPermission() {

//        val coll = ensureCollection()
//
//        coll.refresh {
//            resourceResponse = it
//        }
//
////        AzureData.instance.refresh(coll) {
////            resourceResponse = it
////        }
//
//        Awaitility.await().until {
//            resourceResponse != null
//        }
//
//        assertResponseSuccess(resourceResponse)
//        Assert.assertEquals(collectionId, resourceResponse?.resource?.id)
    }

    @Test
    fun deletePermission() {

//        val coll = ensureCollection()
//
//        coll.delete {
//            dataResponse = it
//        }
//
//        Awaitility.await().until {
//            dataResponse != null
//        }
//
//        assertResponseSuccess(dataResponse)
    }
}