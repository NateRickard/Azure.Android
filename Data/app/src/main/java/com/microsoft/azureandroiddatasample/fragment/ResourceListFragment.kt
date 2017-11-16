package com.microsoft.azureandroiddatasample.fragment

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

import kotlinx.android.synthetic.main.resource_list_fragment.*

/**
 * Created by Nate Rickard on 11/15/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

abstract class ResourceListFragment : RecyclerViewListFragment<Resource, ResourceViewHolder>(), ActionMode.Callback {

    private var actionMode: ActionMode? = null

    override val viewResourceId: Int = R.layout.resource_list_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enablePullToRefresh = false
        enableLongClick = true
    }

    override fun createAdapter(): RecyclerViewAdapter<Resource, ResourceViewHolder> =
            ResourceItemAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button_fetch.setOnClickListener {
            fetchData()
        }

        button_clear.setOnClickListener {
            typedAdapter.clearItems()
        }
    }

    abstract fun fetchData()

    open fun getItem(id: String) = Unit

//    override fun onItemClick(view: View, item: Resource, position: Int) {
//        super.onItemClick(view, item, position)
//    }

    override fun onItemLongClick(view: View, item: Resource, position: Int) {

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

            for (resourceItem in selectedItems) {

                // let the derived fragments do their own Get implementation here
                getItem(resourceItem.id)
            }

            Toast.makeText(activity, "GET operation succeeded for ${selectedItems.size} resources", Toast.LENGTH_LONG).show()
        }
        catch (e: Exception) {
            println(e)
            Toast.makeText(activity, "GET operation failed for 1 or more resources", Toast.LENGTH_LONG).show()
        }

        dialog.cancel()
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