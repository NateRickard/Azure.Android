package com.microsoft.azureandroiddatasample.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View

/**
* Created by Nate Rickard on 11/10/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

abstract class ViewHolderBase<in T: Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun setData(item: T)
}