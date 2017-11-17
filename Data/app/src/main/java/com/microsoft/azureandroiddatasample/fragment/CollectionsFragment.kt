package com.microsoft.azureandroiddatasample.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.activity.DocumentsActivity
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class CollectionsFragment : ResourceListFragment<DocumentCollection>() {

    private lateinit var databaseId: String

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Get, ResourceAction.Create, ResourceAction.Delete)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        databaseId = activity.intent.extras.getString("db_id")
    }

    override fun fetchData(callback: (ResourceListResponse<DocumentCollection>) -> Unit) {

        try {
            AzureData.instance.getCollections(databaseId) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {

        try {
            AzureData.instance.getCollection(id, databaseId) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun createResource(resourceId: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {

        try {
            AzureData.instance.createCollection(resourceId, databaseId) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun deleteItem(resourceId: String, callback: (Boolean) -> Unit) {

        try {
            AzureData.instance.deleteCollection(resourceId, databaseId) { result ->
                callback(result)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onItemClick(view: View, item: DocumentCollection, position: Int) {

        super.onItemClick(view, item, position)

        val coll = typedAdapter.getItem(position)

        val intent = Intent(activity.baseContext, DocumentsActivity::class.java)
        intent.putExtra("db_id", databaseId)
        intent.putExtra("coll_id", coll.id)
        startActivity(intent)
    }
}