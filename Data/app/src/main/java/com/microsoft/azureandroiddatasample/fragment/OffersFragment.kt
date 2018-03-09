package com.microsoft.azureandroiddatasample.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Offer
import com.microsoft.azureandroid.data.services.ResourceListResponse
import com.microsoft.azureandroid.data.services.ResourceResponse
import com.microsoft.azureandroiddatasample.model.ResourceAction
import java.util.*

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

class OffersFragment : ResourceListFragment<Offer>() {

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Get)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun fetchData(callback: (ResourceListResponse<Offer>) -> Unit) {

        AzureData.getOffers { response ->
            callback(response)
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<Offer>) -> Unit) {

        AzureData.getOffer(id) { response ->
            callback(response)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)

        menu.clear() //no "Add" command for Offers r.n.
    }
}