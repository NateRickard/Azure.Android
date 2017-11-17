package com.microsoft.azureandroiddatasample.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.User
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.activity.UserActivity
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class UsersFragment : ResourceListFragment<User>() {

    private lateinit var databaseId: String

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Get, ResourceAction.Create, ResourceAction.Delete)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        databaseId = activity.intent.extras.getString("db_id")
    }

    override fun fetchData(callback: (ResourceListResponse<User>) -> Unit) {

        try {
            AzureData.instance.getUsers(databaseId) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<User>) -> Unit) {

        try {
            AzureData.instance.getUser(id, databaseId) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun createResource(resourceId: String, callback: (ResourceResponse<User>) -> Unit) {

        try {
            AzureData.instance.createUser(resourceId, databaseId) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun deleteItem(resourceId: String, callback: (Boolean) -> Unit) {

        try {
            AzureData.instance.deleteUser(resourceId, databaseId) { result ->
                callback(result)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onItemClick(view: View, item: User, position: Int) {

        super.onItemClick(view, item, position)

        val user = typedAdapter.getItem(position)

        val intent = Intent(activity.baseContext, UserActivity::class.java)
        intent.putExtra("db_id", databaseId)
        intent.putExtra("user_id", user.id)
        startActivity(intent)
    }
}