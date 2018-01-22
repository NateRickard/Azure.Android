package com.microsoft.azureandroiddatasample.fragment

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Permission
import com.microsoft.azureandroid.data.services.Response
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class PermissionsFragment : ResourceListFragment<Permission>() {

    private lateinit var userId: String

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Get, ResourceAction.Delete)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        databaseId = activity.intent.extras.getString("db_id")
        userId = activity.intent.extras.getString("user_id")
    }

    override fun fetchData(callback: (ResourceListResponse<Permission>) -> Unit) {

        AzureData.getPermissions(userId, databaseId) { response ->
            callback(response)
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<Permission>) -> Unit) {

        AzureData.getPermission(id, userId, databaseId) { response ->
            callback(response)
        }
    }

//    override fun getResourceCreationDialog(): View {
//
//        val dialog = layoutInflater.inflate(R.layout.dialog_create_permission, null)
//        val messageTextView = dialog.findViewById<TextView>(R.id.messageText)
//        messageTextView.setText(R.string.resource_dialog)
//
//        return dialog
//    }

//    override fun createResource(dialogView: View, callback: (ResourceResponse<Permission>) -> Unit) {
//
//        val resourceId = dialogView.findViewById<EditText>(R.id.editTextId).text.toString()
//        val modeText = dialogView.findViewById<EditText>(R.id.editTextMode).text.toString()
//        val mode = Permission.PermissionMode.valueOf(modeText)
//
//        AzureData.createPermission(resourceId, mode, res) { response ->
//            callback(response)
//        }
//    }

    override fun deleteItem(resourceId: String, callback: (Response) -> Unit) {

        AzureData.deletePermission(resourceId, userId, databaseId) { result ->
            callback(result)
        }
    }
}