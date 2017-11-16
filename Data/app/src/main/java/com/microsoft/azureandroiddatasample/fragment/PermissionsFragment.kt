package com.microsoft.azureandroiddatasample.fragment

import android.os.Bundle
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Permission
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class PermissionsFragment : ResourceListFragment<Permission>() {

    private lateinit var databaseId: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        databaseId = activity.intent.extras.getString("db_id")
        userId = activity.intent.extras.getString("user_id")
    }

    override fun fetchData(callback: (ResourceListResponse<Permission>) -> Unit) {

        try {
            AzureData.instance.getPermissions(userId, databaseId) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<Permission>) -> Unit) {

        try {
            AzureData.instance.getPermission(id, userId, databaseId) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}