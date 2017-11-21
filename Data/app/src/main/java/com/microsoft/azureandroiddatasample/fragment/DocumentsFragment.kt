package com.microsoft.azureandroiddatasample.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.activity.CollectionActivity
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Created by Nate Rickard on 11/21/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class DocumentsFragment : ResourceListFragment<Document>() {

    private lateinit var collId: String

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Get, ResourceAction.Create, ResourceAction.Delete, ResourceAction.CreatePermission)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        databaseId = activity.intent.extras.getString("db_id")
        collId = activity.intent.extras.getString("coll_id")
    }

    override fun fetchData(callback: (ResourceListResponse<Document>) -> Unit) {

        AzureData.instance.getDocumentsAs<Document>(collId, databaseId) { response ->
            callback(response)
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<Document>) -> Unit) {

        AzureData.instance.getDocument<Document>(id, collId, databaseId) { response ->
            callback(response)
        }
    }

//    override fun createResource(dialogView: View, callback: (ResourceResponse<Document>) -> Unit) {
//
//        val editText = dialogView.findViewById<EditText>(R.id.editText)
//        val resourceId = editText.text.toString()
//
//        AzureData.instance.createDocument(resourceId, databaseId) { response ->
//            callback(response)
//        }
//    }

//    override fun deleteItem(resourceId: String, callback: (Boolean) -> Unit) {
//
//        AzureData.instance.deleteDocument(resourceId, databaseId) { result ->
//            callback(result)
//        }
//    }

    override fun onItemClick(view: View, item: Document, position: Int) {

        super.onItemClick(view, item, position)

        val coll = typedAdapter.getItem(position)

        val intent = Intent(activity.baseContext, CollectionActivity::class.java)
        intent.putExtra("db_id", databaseId)
        intent.putExtra("coll_id", coll.id)

        startActivity(intent)
    }
}