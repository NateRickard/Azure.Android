package com.microsoft.azureandroiddatasample.fragment

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.DictionaryDocument
import com.microsoft.azureandroid.data.model.User
import com.microsoft.azureandroid.data.services.Response
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

class DocumentsFragment : ResourceListFragment<DictionaryDocument>() {

    private lateinit var collectionId: String

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Get, ResourceAction.Create, ResourceAction.Delete, ResourceAction.CreatePermission)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        databaseId = activity.intent.extras.getString("db_id")
        collectionId = activity.intent.extras.getString("coll_id")
    }

    override fun fetchData(callback: (ResourceListResponse<DictionaryDocument>) -> Unit) {

        AzureData.getDocuments(collectionId, databaseId, DictionaryDocument::class.java) { response ->
            callback(response)
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<DictionaryDocument>) -> Unit) {

        AzureData.getDocument(id, collectionId, databaseId, DictionaryDocument::class.java) { response ->
            callback(response)

            //test doc properties came back
            if (response.isSuccessful) {
                response.result.let {
                    it.resource?.let {
                        println(it["testNumber"])
                        println(it["testString"])
                        println(it["testDate"])
                        println(it["testArray"])
                        println(it["testObject"])
                    }
                }
            }
        }
    }

    override fun createResource(dialogView: View, callback: (ResourceResponse<DictionaryDocument>) -> Unit) {

        val editText = dialogView.findViewById<EditText>(R.id.editText)
        val resourceId = editText.text.toString()

        val doc = DictionaryDocument(resourceId)

        //set some test doc properties
        doc["testNumber"] = 1_000_000
        doc["testString"] = "Yeah baby\nRock n Roll"
        doc["testDate"]   = Date()
        doc["testArray"]  = arrayOf(1, 2, 3, 4)
        doc["testObject"] = User()

        AzureData.createDocument(doc, collectionId, databaseId) { response ->
            callback(response)
        }
    }

    override fun deleteItem(resourceId: String, callback: (Response) -> Unit) {

        AzureData.deleteDocument(resourceId, collectionId, databaseId) { result ->
            callback(result)
        }
    }

    override fun onItemClick(view: View, item: DictionaryDocument, position: Int) {

        super.onItemClick(view, item, position)

        val doc = typedAdapter.getItem(position)

//        val intent = Intent(activity.baseContext, CollectionActivity::class.java)
//        intent.putExtra("db_id", databaseId)
//        intent.putExtra("coll_id", coll.id)
//
//        startActivity(intent)
    }
}