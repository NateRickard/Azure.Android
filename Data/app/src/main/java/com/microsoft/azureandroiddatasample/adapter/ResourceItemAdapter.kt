package com.microsoft.azureandroiddatasample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.microsoft.azureandroid.data.model.Resource
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.viewholder.ResourceViewHolder

/**
 * Created by Nate Rickard on 11/15/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class ResourceItemAdapter<TData: Resource> : RecyclerViewAdapter<TData, ResourceViewHolder>() {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ResourceViewHolder {

        val itemView = inflater.inflate (R.layout.two_row_viewcell, parent, false)

        return ResourceViewHolder(itemView)
    }
}