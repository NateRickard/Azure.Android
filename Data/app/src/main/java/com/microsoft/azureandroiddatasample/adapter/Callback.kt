package com.microsoft.azureandroiddatasample.adapter

import android.support.v7.widget.RecyclerView
import java.util.concurrent.Callable

/**
 * Created by nater on 10/27/17.
 */

abstract class Callback<T : Any> : Callable<Unit> {
    var result: T? = null
    var viewHolder: RecyclerView.ViewHolder? = null

    internal fun setResult(result: T, viewHolder: RecyclerView.ViewHolder) {
        this.result = result
        this.viewHolder = viewHolder
    }

    abstract override fun call()
}