package com.microsoft.azureandroiddatasample.viewholder

import android.view.View

/**
 * Created by nater on 11/14/17.
 */

interface TypedViewHolder<in TData> {

    fun findViews(rootView: View)

    fun setData(data: TData, selected: Boolean, animateSelection: Boolean)

    fun setClickHandler(handler: ClickHandler)
}