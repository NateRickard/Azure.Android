package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.delete
import com.microsoft.azureandroid.data.model.Permission
import com.microsoft.azureandroid.data.model.ResourceType
import com.microsoft.azureandroid.data.model.User
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroid.data.services.Response
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
class PermissionTests : ResourceTest<Permission>(ResourceType.Permission, true, true) {

    private val userId = "AndroidTest${ResourceType.User.name}"
    var user: User? = null

    @Before
    override fun setUp() {
        super.setUp()

        AzureData.deleteUser(userId, databaseId) { response ->
            println("Attempted to delete test user.  Result: ${response.isSuccessful}")

            user = ensureUser()
        }

        await().until {
            user != null
        }
    }

    @After
    override fun tearDown() {

        var deleteResponse: Response? = null

        AzureData.deleteUser(userId, databaseId) { response ->
            println("Attempted to delete test user.  Result: ${response.isSuccessful}")
            deleteResponse = response
        }

        await().until {
            deleteResponse != null
        }

        super.tearDown()
    }

    private fun ensureUser() : User {

        var userResponse: ResourceResponse<User>? = null

        AzureData.createUser(userId, databaseId) {
            userResponse = it
        }

        await().until {
            userResponse != null
        }

        assertResponseSuccess(userResponse)
        Assert.assertEquals(userId, userResponse?.resource?.id)

        return userResponse!!.resource!!
    }

    private fun createNewPermission() : Permission {

        AzureData.createPermission(resourceId, Permission.PermissionMode.Read, collection!!, user!!, databaseId) {
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
    fun createPermission() {

        createNewPermission()
    }

    @Test
    fun listPermissions() {

        AzureData.getPermissions(userId, databaseId) {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assert(resourceListResponse!!.resource!!.isPopuated)
    }

    @Test
    fun getPermission() {

        createNewPermission()

        AzureData.getPermission(resourceId, userId, databaseId) {
            resourceResponse = it
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        Assert.assertEquals(resourceId, resourceResponse?.resource?.id)
    }

    @Test
    fun deletePermission() {

        val permission = createNewPermission()

        permission.delete {
            dataResponse = it
        }

        await().until {
            dataResponse != null
        }

        assertResponseSuccess(dataResponse)
    }
}