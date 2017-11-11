package com.microsoft.azureandroiddatasample.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by nater on 11/10/17.
 */

abstract class ViewHolderBase<in T: Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun setData(item: T)
}