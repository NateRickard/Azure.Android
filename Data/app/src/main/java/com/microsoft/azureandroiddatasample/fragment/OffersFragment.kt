package com.microsoft.azureandroiddatasample.fragment

import android.os.Bundle
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Offer
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse

/**
 * Created by Nate Rickard on 11/14/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class OffersFragment : ResourceListFragment<Offer>() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun fetchData(callback: (ResourceListResponse<Offer>) -> Unit) {

        try {
            AzureData.instance.offers { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<Offer>) -> Unit) {

        try {
            AzureData.instance.getOffer(id) { response ->
                callback(response)
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}