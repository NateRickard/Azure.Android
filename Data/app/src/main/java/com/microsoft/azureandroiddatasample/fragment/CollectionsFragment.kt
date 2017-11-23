package com.microsoft.azureandroiddatasample.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroid.data.services.DataResponse
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.activity.CollectionActivity
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class CollectionsFragment : ResourceListFragment<DocumentCollection>() {

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Get, ResourceAction.Create, ResourceAction.Delete, ResourceAction.CreatePermission)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        databaseId = activity.intent.extras.getString("db_id")
    }

    override fun fetchData(callback: (ResourceListResponse<DocumentCollection>) -> Unit) {

        AzureData.instance.getCollections(databaseId) { response ->
            callback(response)
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<DocumentCollection>) -> Unit) {

        AzureData.instance.getCollection(id, databaseId) { response ->
            callback(response)
        }
    }

    override fun createResource(dialogView: View, callback: (ResourceResponse<DocumentCollection>) -> Unit) {

        val editText = dialogView.findViewById<EditText>(R.id.editText)
        val resourceId = editText.text.toString()

        AzureData.instance.createCollection(resourceId, databaseId) { response ->
            callback(response)
        }
    }

    override fun deleteItem(resourceId: String, callback: (DataResponse) -> Unit) {

        AzureData.instance.deleteCollection(resourceId, databaseId) { result ->
            callback(result)
        }
    }

    override fun onItemClick(view: View, item: DocumentCollection, position: Int) {

        super.onItemClick(view, item, position)

        val coll = typedAdapter.getItem(position)

        val intent = Intent(activity.baseContext, CollectionActivity::class.java)
        intent.putExtra("db_id", databaseId)
        intent.putExtra("coll_id", coll.id)

        startActivity(intent)
    }
}