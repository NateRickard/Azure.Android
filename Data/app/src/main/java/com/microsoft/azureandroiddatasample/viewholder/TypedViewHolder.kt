package com.microsoft.azureandroiddatasample.viewholder

import android.view.View

/**
* Created by Nate Rickard on 11/14/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

interface TypedViewHolder<in TData> {

    fun findViews(rootView: View)

    fun setData(data: TData, selected: Boolean, animateSelection: Boolean)

    fun setClickHandler(handler: ClickHandler)
}