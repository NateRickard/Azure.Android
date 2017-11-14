package com.microsoft.azureandroiddatasample.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.HapticFeedbackConstants

typealias ClickHandler = (View, Int) -> Unit


/**
 * Created by nater on 11/14/17.
 */

abstract class ViewHolder<in TData>(itemView: View) : RecyclerView.ViewHolder(itemView), TypedViewHolder<TData>, View.OnClickListener, View.OnLongClickListener {

    var itemClickHandler: ClickHandler? = null
    var itemLongClickHandler: ClickHandler? = null

    init {
        findViews (itemView)
    }

    override fun setClickHandler(handler: ClickHandler) {

        itemView.setOnClickListener(this)
        itemClickHandler = handler
    }


    fun setLongClickHandler(handler: ClickHandler) {

        itemView.setOnLongClickListener(this)
        itemLongClickHandler = handler
    }


    override fun onLongClick(v: View): Boolean {

        v.performHapticFeedback (HapticFeedbackConstants.LONG_PRESS)
        itemLongClickHandler?.invoke (v, adapterPosition)

        return true
    }

    override fun onClick(v: View) {

        itemClickHandler?.invoke (v, adapterPosition)
    }

    abstract override fun findViews(rootView: View)

    // base behavior is to change the row state to activated if it's selected
    override fun setData (data: TData, selected: Boolean, animateSelection: Boolean) {

        itemView.isActivated = selected
    }
}