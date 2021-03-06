package com.microsoft.azureandroiddatasample.fragment

import android.content.Intent
import android.view.View
import android.widget.EditText
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroid.data.services.Response
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.activity.DatabaseActivity
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

class DatabasesFragment : ResourceListFragment<Database>() {

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Get, ResourceAction.Create, ResourceAction.Delete)

    override fun fetchData(callback: (ResourceListResponse<Database>) -> Unit) {

        AzureData.getDatabases { response ->
            callback(response)
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<Database>) -> Unit) {

        AzureData.getDatabase(id) { response ->
            callback(response)
        }
    }

    override fun createResource(dialogView: View, callback: (ResourceResponse<Database>) -> Unit) {

        val editText = dialogView.findViewById<EditText>(R.id.editText)
        val resourceId = editText.text.toString()

        AzureData.createDatabase(resourceId) { response ->
            callback(response)
        }
    }

    override fun deleteItem(resourceId: String, callback: (Response) -> Unit) {

        AzureData.deleteDatabase(resourceId) { result ->
            callback(result)
        }
    }

    override fun onItemClick(view: View, item: Database, position: Int) {

        super.onItemClick(view, item, position)

        val db = typedAdapter.getItem(position)

        val intent = Intent(activity.baseContext, DatabaseActivity::class.java)
        intent.putExtra("db_id", db.id)
        startActivity(intent)
    }
}