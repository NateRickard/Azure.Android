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
import android.widget.EditText
import android.widget.TextView
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse

import kotlinx.android.synthetic.main.resource_list_fragment.*

/**
 * Created by Nate Rickard on 11/15/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

abstract class ResourceListFragment<TData: Resource> : RecyclerViewListFragment<TData, ResourceViewHolder>(), ActionMode.Callback {

    private var actionMode: ActionMode? = null

    override val viewResourceId: Int = R.layout.resource_list_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enablePullToRefresh = false
        enableLongClick = true
        setHasOptionsMenu(true)
    }

    override fun createAdapter(): RecyclerViewAdapter<TData, ResourceViewHolder> =
            ResourceItemAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button_fetch.setOnClickListener {

            val dialog = ProgressDialog.show(activity, "", "Loading. Please wait...", true)

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
                    }

                    dialog.cancel()
                }
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                dialog.cancel()
            }
        }

        button_clear.setOnClickListener {
            typedAdapter.clearItems()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_resource, menu)

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

    open fun fetchData(callback: (ResourceListResponse<TData>) -> Unit) {}

    open fun getItem(id: String, callback: (ResourceResponse<TData>) -> Unit) {}

    open fun createResource(resourceId: String, callback: (ResourceResponse<TData>) -> Unit) {}

    private fun beginResourceCreation() {

        val editTextView = layoutInflater.inflate(R.layout.edit_text, null)
        val editText = editTextView.findViewById<EditText>(R.id.editText)
        val messageTextView = editTextView.findViewById<TextView>(R.id.messageText)
        messageTextView.setText(R.string.resource_dialog)

        AlertDialog.Builder(activity)
                .setView(editTextView)
                .setPositiveButton("Create", { dialog, whichButton ->

                    val resourceId = editText.text.toString()
                    val progressDialog = ProgressDialog.show(activity, "", "Creating. Please wait...", true)

                    createResource(resourceId) { response ->

                        if (response.isSuccessful) {

                            val resource = response.resource

                            println("Created DB successfully: ${resource?.id}")

                            activity.runOnUiThread {
                                fetchData {

                                }
                            }
                        } else {
                            println(response.error)
                        }
                    }

                    progressDialog.cancel()
                })
                .setNegativeButton("Cancel", { dialog, whichButton -> }).show()
    }

//    override fun onItemClick(view: View, item: Resource, position: Int) {
//        super.onItemClick(view, item, position)
//    }

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
            actionMode?.title = "$count items"
            actionMode?.invalidate ()
        }
    }

    private fun getSelectedItems() {

        val dialog = ProgressDialog.show(activity, "", "Loading. Please wait...", true)

        try {
            val selectedItems = typedAdapter.getSelectedItems()
            val items = mutableListOf<TData>()
            var count = 0

            for (resourceItem in selectedItems) {

                // let the derived fragments do their own Get implementation here
                getItem(resourceItem.id) { response ->

                    count++

                    if (response.isSuccessful && response.resource != null) {

                        response.resource?.let {
                            items.add(it)
                            println("GET operation succeeded for resource ${it.id}")
                        }
                    }
                    else {
                        println(response.error)
                    }

                    if (count == selectedItems.size) {

                        if (items.size == selectedItems.size) {
                            activity.runOnUiThread {
                                dialog.cancel()
                                Toast.makeText(activity, "GET operation succeeded for ${selectedItems.size} resources", Toast.LENGTH_LONG).show()
                            }
                        }
                        else {
                            throw Exception("Error getting resource with id: ${resourceItem.id}")
                        }
                    }
                }
            }
        }
        catch (e: Exception) {
            println(e)
            activity.runOnUiThread {
                dialog.cancel()
                Toast.makeText(activity, "GET operation failed for 1 or more resources", Toast.LENGTH_LONG).show()
            }
        }
    }

    //region ActionMode.ICallback Members

    override fun onCreateActionMode (mode: ActionMode, menu: Menu) : Boolean {

        mode.menuInflater.inflate (R.menu.menu_resource_actions, menu)

        //disable pull to refresh if action mode is enabled
        swipeRefreshLayout?.isEnabled = false

        return true
    }

    override fun onPrepareActionMode (mode: ActionMode, menu: Menu) : Boolean
            = false

    override fun onActionItemClicked (mode: ActionMode, item: MenuItem) : Boolean =
            when (item.itemId)
            {
                R.id.action_get -> {
                    getSelectedItems()
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