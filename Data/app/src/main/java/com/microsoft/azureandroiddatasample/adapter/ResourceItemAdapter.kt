package com.microsoft.azureandroiddatasample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.microsoft.azureandroid.data.model.Resource
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.viewholder.ResourceViewHolder

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

class ResourceItemAdapter<TData: Resource> : RecyclerViewAdapter<TData, ResourceViewHolder>() {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ResourceViewHolder {

        val itemView = inflater.inflate (R.layout.two_row_viewcell, parent, false)

        return ResourceViewHolder(itemView)
    }
}