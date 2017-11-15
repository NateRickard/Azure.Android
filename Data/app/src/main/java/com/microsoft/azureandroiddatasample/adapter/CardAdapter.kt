package com.microsoft.azureandroiddatasample.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.microsoft.azureandroiddatasample.viewholder.ViewHolderBase
import com.microsoft.azureandroiddatasample.viewholder.ViewHolderFactory

import java.util.ArrayList

class CardAdapter<T : Any>(private val _viewLayoutId: Int, private val _viewHolderClazz: Class<*>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<T>
    private var _viewHolder: RecyclerView.ViewHolder? = null

    init {
        items = ArrayList()
    }

    fun addData(item: T) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun getData(index: Int): T {
        return items[index]
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder? {

        val v = LayoutInflater.from(viewGroup.context)
                .inflate(_viewLayoutId, viewGroup, false)

        _viewHolder = ViewHolderFactory.create(_viewHolderClazz, v)

        return _viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val item = items[i]

        (viewHolder as ViewHolderBase<T>).setData(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}