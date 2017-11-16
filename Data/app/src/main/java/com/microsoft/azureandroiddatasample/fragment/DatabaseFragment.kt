package com.microsoft.azureandroiddatasample.fragment

import android.content.Intent
import android.view.View
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.activity.CollectionsActivity

/**
 * Created by Nate Rickard on 11/14/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class DatabaseFragment : ResourceListFragment<Database>() {

    override fun fetchData(callback: (ResourceListResponse<Database>) -> Unit) {

        try {
            AzureData.instance.databases { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<Database>) -> Unit) {

        try {
            AzureData.instance.getDatabase(id) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun createResource(resourceId: String, callback: (ResourceResponse<Database>) -> Unit) {

        try {
            AzureData.instance.createDatabase(resourceId) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onItemClick(view: View, item: Database, position: Int) {

        super.onItemClick(view, item, position)

        val db = typedAdapter.getItem(position)

        val intent = Intent(activity.baseContext, CollectionsActivity::class.java)
        intent.putExtra("db_id", db.id)
        startActivity(intent)
    }
}