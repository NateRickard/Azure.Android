package com.microsoft.azureandroiddatasample.fragment

import android.os.Bundle
import android.view.View
import com.microsoft.azureandroid.data.model.Resource
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.RecyclerViewAdapter
import com.microsoft.azureandroiddatasample.adapter.ResourceItemAdapter
import com.microsoft.azureandroiddatasample.viewholder.ResourceViewHolder

import kotlinx.android.synthetic.main.resource_list_fragment.*

/**
 * Created by Nate Rickard on 11/15/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

abstract class ResourceListFragment : RecyclerViewListFragment<Resource, ResourceViewHolder>() {

    override val viewResourceId: Int = R.layout.resource_list_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enablePullToRefresh = false
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
}