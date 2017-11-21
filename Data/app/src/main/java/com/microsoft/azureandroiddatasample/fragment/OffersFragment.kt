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
 * Created by Nate Rickard on 11/14/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class OffersFragment : ResourceListFragment<Offer>() {

    override val actionSupport: EnumSet<ResourceAction> = EnumSet.of(ResourceAction.Get)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun fetchData(callback: (ResourceListResponse<Offer>) -> Unit) {

        AzureData.instance.offers { response ->
            callback(response)
        }
    }

    override fun getItem(id: String, callback: (ResourceResponse<Offer>) -> Unit) {

        AzureData.instance.getOffer(id) { response ->
            callback(response)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)

        menu.clear() //no "Add" command for Offers r.n.
    }
}