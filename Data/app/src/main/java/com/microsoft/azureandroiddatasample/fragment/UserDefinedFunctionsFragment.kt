package com.microsoft.azureandroiddatasample.fragment

import android.os.Bundle
import android.view.View
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.UserDefinedFunction
import com.microsoft.azureandroid.data.services.DataResponse
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Created by Nate Rickard on 11/21/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class UserDefinedFunctionsFragment : ResourceListFragment<UserDefinedFunction>() {

    private lateinit var collectionId: String

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Delete, ResourceAction.CreatePermission)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        databaseId = activity.intent.extras.getString("db_id")
        collectionId = activity.intent.extras.getString("coll_id")
    }

    override fun fetchData(callback: (ResourceListResponse<UserDefinedFunction>) -> Unit) {

        AzureData.instance.getUserDefinedFunctions(collectionId, databaseId) { response ->
            callback(response)
        }
    }

//    override fun getItem(id: String, callback: (ResourceResponse<UserDefinedFunction>) -> Unit) {
//
//        AzureData.instance.getStoredProcedure(id, collectionId, databaseId) { response ->
//            callback(response)
//        }
//    }

//    override fun createResource(dialogView: View, callback: (ResourceResponse<UserDefinedFunction>) -> Unit) {
//
//        val editText = dialogView.findViewById<EditText>(R.id.editText)
//        val resourceId = editText.text.toString()
//
//        val storedProcedure = """
//        function () {
//            var context = getContext();
//            var r = context.getResponse();
//
//            r.setBody(\"Hello World!\");
//        }
//        """
//
//        AzureData.instance.createUserDefinedFunction(resourceId, storedProcedure, collectionId, databaseId) { response ->
//            callback(response)
//        }
//    }

    override fun deleteItem(resourceId: String, callback: (DataResponse) -> Unit) {

        AzureData.instance.deleteUserDefinedFunction(resourceId, collectionId, databaseId) { result ->
            callback(result)
        }
    }

    override fun onItemClick(view: View, item: UserDefinedFunction, position: Int) {

        super.onItemClick(view, item, position)

        val udf = typedAdapter.getItem(position)

//        val intent = Intent(activity.baseContext, CollectionActivity::class.java)
//        intent.putExtra("db_id", databaseId)
//        intent.putExtra("coll_id", coll.id)
//
//        startActivity(intent)
    }
}