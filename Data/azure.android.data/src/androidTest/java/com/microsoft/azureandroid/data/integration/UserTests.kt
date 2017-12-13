package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.delete
import com.microsoft.azureandroid.data.model.ResourceType
import com.microsoft.azureandroid.data.model.User
import com.microsoft.azureandroid.data.refresh
import com.microsoft.azureandroid.data.services.DataResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import org.awaitility.Awaitility.await
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Nate Rickard on 12/11/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class UserTests : ResourceTest<User>(ResourceType.User, true, false) {

    @Before
    override fun setUp() {
        super.setUp()

        deleteTestUser()
    }

    @After
    override fun tearDown() {

        deleteTestUser()

        super.tearDown()
    }

    private fun deleteTestUser(id: String = resourceId) {

        var deleteResponse: DataResponse? = null

        AzureData.deleteUser(id, databaseId) { response ->
            println("Attempted to delete test user.  Result: ${response.isSuccessful}")
            deleteResponse = response
        }

        await().until {
            deleteResponse != null
        }
    }

    private fun createNewUser() : User {

        var userResponse: ResourceResponse<User>? = null

        AzureData.createUser(resourceId, databaseId) {
            userResponse = it
        }

        await().until {
            userResponse != null
        }

        assertResponseSuccess(userResponse)
        Assert.assertEquals(resourceId, userResponse?.resource?.id)

        return userResponse!!.resource!!
    }

    @Test
    fun createUser() {

        createNewUser()
    }

    @Test
    fun listUsers() {

        //make sure we have at least one user
        createNewUser()

        AzureData.getUsers(databaseId) {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assert(resourceListResponse!!.resource!!.isPopuated)
    }

    @Test
    fun getUser() {

        createNewUser()

        AzureData.getUser(resourceId, databaseId) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        Assert.assertEquals(resourceId, resourceResponse?.resource?.id)
    }

    @Test
    fun refreshUser() {

        val user = createNewUser()

        user.refresh {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        Assert.assertEquals(resourceId, resourceResponse?.resource?.id)
    }

    @Test
    fun replaceUser() {

        val replaceUserId = "Updated_$resourceId"
        val user = createNewUser()

        AzureData.replaceUser(user.id, replaceUserId, databaseId) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        Assert.assertEquals(replaceUserId, resourceResponse?.resource?.id)
        Assert.assertNotEquals(resourceId, resourceResponse?.resource?.id)

        deleteTestUser(replaceUserId)
    }

    @Test
    fun deleteUser() {

        val user = createNewUser()

        user.delete {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }
}