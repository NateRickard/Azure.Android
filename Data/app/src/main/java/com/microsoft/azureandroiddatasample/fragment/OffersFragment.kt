package com.microsoft.azureandroiddatasample.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Resource
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.RecyclerViewAdapter
import com.microsoft.azureandroiddatasample.adapter.ResourceItemAdapter
import com.microsoft.azureandroiddatasample.viewholder.ResourceViewHolder

import kotlinx.android.synthetic.main.resource_list_fragment.*

/**
 * Created by Nate Rickard on 11/14/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class OffersFragment : RecyclerViewListFragment<Resource, ResourceViewHolder>() {

    override val viewResourceId: Int = R.layout.resource_list_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enablePullToRefresh = false
    }

    override fun createAdapter(): RecyclerViewAdapter<Resource, ResourceViewHolder> =
            ResourceItemAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button_fetch.setOnClickListener {
            fetchOffers()
        }

        button_clear.setOnClickListener {
            typedAdapter.clearItems()
        }
    }

    private fun fetchOffers() {

        try {
            val dialog = ProgressDialog.show(activity, "", "Loading. Please wait...", true)

            AzureData.instance.offers { response ->

                print(response.result)

                if (response.isSuccessful) {

                    val offers = response.resource?.items!!

                    activity.runOnUiThread {

                        typedAdapter.setItems(offers)
                    }
                }
                else {
                    print(response.error)
                }

                dialog.cancel()
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}