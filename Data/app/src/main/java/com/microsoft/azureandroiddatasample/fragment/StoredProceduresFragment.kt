package com.microsoft.azureandroiddatasample.fragment

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.StoredProcedure
import com.microsoft.azureandroid.data.services.DataResponse
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Created by Nate Rickard on 11/21/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class StoredProceduresFragment : ResourceListFragment<StoredProcedure>() {

    private lateinit var collectionId: String

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Create, ResourceAction.Delete, ResourceAction.CreatePermission)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        databaseId = activity.intent.extras.getString("db_id")
        collectionId = activity.intent.extras.getString("coll_id")
    }

    override fun fetchData(callback: (ResourceListResponse<StoredProcedure>) -> Unit) {

        AzureData.instance.getStoredProcedures(collectionId, databaseId) { response ->
            callback(response)
        }
    }

//    override fun getItem(id: String, callback: (ResourceResponse<StoredProcedure>) -> Unit) {
//
//        AzureData.instance.getStoredProcedure(id, collectionId, databaseId) { response ->
//            callback(response)
//        }
//    }

    override fun createResource(dialogView: View, callback: (ResourceResponse<StoredProcedure>) -> Unit) {

        val editText = dialogView.findViewById<EditText>(R.id.editText)
        val resourceId = editText.text.toString()

        val storedProcedure = """
        function () {
            var context = getContext();
            var r = context.getResponse();

            r.setBody(\"Hello World!\");
        }
        """

        AzureData.instance.createStoredProcedure(resourceId, storedProcedure, collectionId, databaseId) { response ->
            callback(response)
        }
    }

    override fun deleteItem(resourceId: String, callback: (DataResponse) -> Unit) {

        AzureData.instance.deleteStoredProcedure(resourceId, collectionId, databaseId) { result ->
            callback(result)
        }
    }

    override fun onItemClick(view: View, item: StoredProcedure, position: Int) {

        super.onItemClick(view, item, position)

        val sproc = typedAdapter.getItem(position)

//        val intent = Intent(activity.baseContext, CollectionActivity::class.java)
//        intent.putExtra("db_id", databaseId)
//        intent.putExtra("coll_id", coll.id)
//
//        startActivity(intent)
    }
}