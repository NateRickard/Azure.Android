package com.microsoft.azureandroiddatasample.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

class CardAdapter<T : Any>(private val _viewLayoutId: Int, private val _onBindViewHolderMethod: Callback<Any>, private val _viewHolderClazz: Class<*>, private val _activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val _mItems: MutableList<T>
    private var _viewHolder: RecyclerView.ViewHolder? = null

    init {

        _mItems = ArrayList()
    }

    fun addData(item: T) {
        _mItems.add(item)

        notifyDataSetChanged()
    }

    fun clear() {
        _mItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder? {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(_viewLayoutId, viewGroup, false)

        _viewHolder = ViewHolderFactory.create(_viewHolderClazz, v)

        return _viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val item = _mItems[i]

        _onBindViewHolderMethod.setResult(item, viewHolder)

        if (item != null) {
            _onBindViewHolderMethod.call()
        }
    }

    override fun getItemCount(): Int {
        return _mItems.size
    }
}