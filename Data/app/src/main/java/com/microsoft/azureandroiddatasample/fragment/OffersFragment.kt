package com.microsoft.azureandroiddatasample.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.microsoft.azureandroid.data.AzureData

import kotlinx.android.synthetic.main.resource_list_fragment.*

/**
 * Created by Nate Rickard on 11/14/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class OffersFragment : ResourceListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

    }

    override fun fetchData() {

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