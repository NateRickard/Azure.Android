package com.microsoft.azureandroiddatasample.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.User
import com.microsoft.azureandroid.data.services.Response
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.activity.UserActivity
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

class UsersFragment : ResourceListFragment<User>() {

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Get, ResourceAction.Create, ResourceAction.Delete)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        databaseId = activity.intent.extras.getString("db_id")
    }

    override fun fetchData(callback: (ResourceListResponse<User>) -> Unit) {

        AzureData.getUsers(databaseId) { response ->
            callback(response)
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<User>) -> Unit) {

        AzureData.getUser(id, databaseId) { response ->
            callback(response)
        }
    }

    override fun createResource(dialogView: View, callback: (ResourceResponse<User>) -> Unit) {

        val editText = dialogView.findViewById<EditText>(R.id.editText)
        val resourceId = editText.text.toString()

        AzureData.createUser(resourceId, databaseId) { response ->
            callback(response)
        }
    }

    override fun deleteItem(resourceId: String, callback: (Response) -> Unit) {

        AzureData.deleteUser(resourceId, databaseId) { result ->
            callback(result)
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