package com.microsoft.azureandroiddatasample.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.microsoft.azureandroid.data.model.Resource
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.RecyclerViewAdapter
import com.microsoft.azureandroiddatasample.adapter.ResourceItemAdapter
import com.microsoft.azureandroiddatasample.viewholder.ResourceViewHolder
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.view.MenuInflater
import android.widget.Spinner
import android.widget.TextView
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*
import android.R.array
import android.widget.ArrayAdapter
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Permission
import com.microsoft.azureandroid.data.model.User

import kotlinx.android.synthetic.main.dialog_create_permission.view.*

/**
 * Created by Nate Rickard on 11/15/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

abstract class ResourceListFragment<TData: Resource> : RecyclerViewListFragment<TData, ResourceViewHolder>(), ActionMode.Callback {

    private var actionMode: ActionMode? = null

    open val actionSupport: EnumSet<ResourceAction> = EnumSet.noneOf(ResourceAction::class.java)
    open var databaseId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableLongClick = true
        setHasOptionsMenu(true)
    }

    override fun createAdapter(): RecyclerViewAdapter<TData, ResourceViewHolder> =
            ResourceItemAdapter()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_resource, menu)

        menu.findItem(R.id.action_create).isVisible = actionSupport.contains(ResourceAction.Create)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_create -> {
                beginResourceCreation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onLoadData() {

        try {
            fetchData { response ->

                if (response.isSuccessful) {

                    val items = response.resource?.items!!

                    activity.runOnUiThread {
                        typedAdapter.setItems(items)
                    }
                }
                else {
                    print(response.error)

                    activity.runOnUiThread {
                        Toast.makeText(activity, "Failed to load resource(s)", Toast.LENGTH_LONG).show()
                    }
                }

                super.onLoadData()
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            Toast.makeText(activity, "Failed to load resource(s)", Toast.LENGTH_LONG).show()
        }
    }

    open fun fetchData(callback: (ResourceListResponse<TData>) -> Unit) {}

    open fun getItem(id: String, callback: (ResourceResponse<TData>) -> Unit) {}

    open fun createResource(dialogView: View, callback: (ResourceResponse<TData>) -> Unit) {}

    open fun deleteItem(resourceId: String, callback: (Boolean) -> Unit) {}

    open fun getResourceCreationDialog() : View {

        val editTextView = layoutInflater.inflate(R.layout.dialog_single_input, null)
        val messageTextView = editTextView.findViewById<TextView>(R.id.messageText)

        messageTextView.setText(R.string.resource_dialog)

        return editTextView
    }

    private fun beginResourceCreation() {

        try {
            val dialogView = getResourceCreationDialog()

            AlertDialog.Builder(activity)
                    .setTitle("Create Resource")
                    .setView(dialogView)
                    .setCancelable(true)
                    .setPositiveButton("Create") { dialog, which ->

                        val progressDialog = ProgressDialog.show(activity, "", "Creating. Please wait...", true)

                        createResource(dialogView) { response ->

                            try {
                                if (response.isSuccessful) {

                                    val resource = response.resource

                                    if (resource != null) {
                                        println("Created resource successfully: ${resource.id}")

                                        activity.runOnUiThread {
                                            typedAdapter.addItem(0, resource)
                                            progressDialog.cancel()
                                            Toast.makeText(activity, "Created resource with Id: ${resource.id}", Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        throw Exception("Unable to successfully create resource: returned resource is null")
                                    }
                                } else {
                                    throw Exception(response.error?.toString())
                                }
                            } catch (e: Exception) {
                                println(e)
                                progressDialog.cancel()
                            }
                        }
                    }
                    .show()
        }
        catch (e: Exception) {
            Toast.makeText(activity, "Error creating resource - please check the inputs", Toast.LENGTH_LONG).show()
        }
    }

    private fun createPermissionsForSelectedItems(callback: (Boolean) -> Unit) {

        if (databaseId == "") {
            throw Exception("No valid Database selected")
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_create_permission, null)
        dialogView.messageText.text = getString(R.string.create_new_permission)

        val progressDialog = ProgressDialog.show(activity, "", "Please wait...", true)

        AzureData.instance.getUsers(databaseId) { response ->

            progressDialog.cancel()

            if (response.isSuccessful) {

                val users = response.resource!!.items
                val userNames = users.map {
                    it.id
                }

                val modes = Permission.PermissionMode.values().map {
                    it.name
                }

                activity.runOnUiThread {

                    // Create an ArrayAdapter using the string array and a default spinner layout
                    var adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, userNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dialogView.spinnerUsers.adapter = adapter

                    adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, modes)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dialogView.spinnerModes.adapter = adapter

                    AlertDialog.Builder(activity)
                            .setTitle("Create Permission")
                            .setView(dialogView)
                            .setCancelable(true)
                            .setPositiveButton("Create") { dialog, which ->

                                progressDialog.setMessage("Creating. Please wait...")
                                progressDialog.show()

                                val permissionId = dialogView.editTextId.text.toString()
                                val user = users[dialogView.spinnerModes.selectedItemPosition]
                                val mode = Permission.PermissionMode.valueOf(dialogView.spinnerModes.selectedItem.toString())

                                val selectedItems = typedAdapter.getSelectedItems()
                                val items = mutableListOf<Permission>()

                                // currently we'll limit this to functioning on ONE resource since we need a unique permission name, but we'll leave loopng code here in case we find a way to support
                                for (resourceItem in selectedItems) {

                                    AzureData.instance.createPermission(permissionId, mode, resourceItem, user, databaseId) { response ->

                                        try {
                                            if (response.isSuccessful) {

                                                val permission = response.resource

                                                permission?.let {
                                                    items.add(it)
                                                    println("Created permission successfully: ${permission.id}")
                                                }
                                            } else {
                                                throw Exception(response.error?.toString())
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        finally {
                                            //are we done looping thru selected items?
                                            val complete = resourceItem == selectedItems.last()

                                            if (complete) {
                                                activity.runOnUiThread {
                                                    progressDialog.cancel()
                                                    //return true if all permissions were created
                                                    callback(items.size == selectedItems.size)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            .show()
                }
            }
            else callback(false)
        }
    }

    override fun onItemLongClick(view: View, item: TData, position: Int) {

        super.onItemLongClick(view, item, position)

        enableActionMode (position)
    }

    private fun enableActionMode(position: Int) {

        if (actionMode == null) {
            actionMode = (activity as AppCompatActivity).startSupportActionMode(this)
        }

        toggleSelection(position)
    }

    private fun toggleSelection (position: Int) {

        typedAdapter.toggleSelection (position)

        val count = typedAdapter.selectedItemCount

        if (count == 0) {
            actionMode?.finish ()
        }
        else {
            actionMode?.title = "$count item${if (count == 1) "" else "s"}"
            actionMode?.invalidate ()
        }
    }

    private fun getSelectedItems() {

        val dialog = ProgressDialog.show(activity, "", "Loading. Please wait...", true)

        val selectedItems = typedAdapter.getSelectedItems()
        val items = mutableListOf<TData>()

        for (resourceItem in selectedItems) {

            // let the derived fragments do their own Get implementation here
            getItem(resourceItem.id) { response ->

                try {
                    if (response.isSuccessful && response.resource != null) {

                        response.resource?.let {
                            items.add(it)
                            println("GET operation succeeded for resource ${it.id}")
                        }
                    } else {
                        println(response.error)
                    }

                    if (resourceItem == selectedItems.last()) {

                        dialog.cancel()

                        activity.runOnUiThread {
                            if (items.size == selectedItems.size) {
                                Toast.makeText(activity, "GET operation succeeded for ${selectedItems.size} resource(s)", Toast.LENGTH_LONG).show()
                            }
                            else {
                                Toast.makeText(activity, "GET operation failed for 1 or more resource(s)", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun deleteSelectedItems() {

        val dialog = ProgressDialog.show(activity, "", "Deleting. Please wait...", true)

        val selectedItems = typedAdapter.getSelectedItems()
        var itemsSucceeded = 0

        for (resourceItem in selectedItems) {

            // let the derived fragments do their own Get implementation here
            deleteItem(resourceItem.id) { result ->

                try {
                    if (result) {
                        itemsSucceeded++
                    }

                    println("Delete result for resource with id ${resourceItem.id} : $result")

                    activity.runOnUiThread {

                        typedAdapter.removeItem(resourceItem)

                        if (resourceItem == selectedItems.last()) {

                            dialog.cancel()

                            if (itemsSucceeded == selectedItems.size) {
                                Toast.makeText(activity, "Delete succeeded for ${selectedItems.size} resource(s)", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(activity, "Delete failed for 1 or more resource(s)", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    //region ActionMode.ICallback Members

    override fun onCreateActionMode (mode: ActionMode, menu: Menu) : Boolean {

        mode.menuInflater.inflate (R.menu.menu_resource_actions, menu)

        menu.findItem(R.id.action_get).isVisible = actionSupport.contains(ResourceAction.Get)
        menu.findItem(R.id.action_new_permission).isVisible = actionSupport.contains(ResourceAction.CreatePermission)
        menu.findItem(R.id.action_delete).isVisible = actionSupport.contains(ResourceAction.Delete)

        //disable pull to refresh if action mode is enabled
        swipeRefreshLayout?.isEnabled = false

        return true
    }

    override fun onPrepareActionMode (mode: ActionMode, menu: Menu) : Boolean {

        val count = typedAdapter.selectedItemCount

        menu.findItem(R.id.action_new_permission).isVisible = count == 1

        return true
    }

    override fun onActionItemClicked (mode: ActionMode, item: MenuItem) : Boolean =
            when (item.itemId)
            {
                R.id.action_get -> {
                    getSelectedItems()
                    mode.finish ()
                    true
                }
                R.id.action_new_permission -> {
                    createPermissionsForSelectedItems { result ->

                        mode.finish()

                        if (result) {
                            Toast.makeText(activity, "Permissions created successfully for the selected resource(s)", Toast.LENGTH_LONG).show()
                        }
                        else {
                            Toast.makeText(activity, "Permission creation failed for 1 or more resource(s)", Toast.LENGTH_LONG).show()
                        }
                    }
                    true
                }
                R.id.action_delete -> {
                    deleteSelectedItems()
                    mode.finish ()
                    true
                }
                else -> false
            }

    override fun onDestroyActionMode (mode: ActionMode) {

        typedAdapter.clearSelectedItems ()
        swipeRefreshLayout?.isEnabled = true
        actionMode = null
    }

    //endregion
}