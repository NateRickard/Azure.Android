package com.microsoft.azureandroiddatasample.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

/**
* Created by Nate Rickard on 11/14/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

abstract class FragmentPagerAdapterBase(private val manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val tags: Array<String> by lazy {
        Array(this.count, { _ -> "" })
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {

        val item = super.instantiateItem(container, position)

        try
        {
            // record the fragment tag here
            if (item is Fragment)
            {
                val tag = item.tag
                tags [position] = tag
            }
        }
        catch (e: Exception)
        {
            println(e)
        }

        return item
    }

    fun getFragmentAtPosition(position: Int) : Fragment {

        val tag = tags[position]

        return manager.findFragmentByTag(tag)
    }
}