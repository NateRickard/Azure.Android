package com.microsoft.azureandroiddatasample.fragment

import android.app.ProgressDialog
import com.microsoft.azureandroid.data.AzureData

/**
 * Created by Nate Rickard on 11/14/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class OffersFragment : ResourceListFragment() {

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

    override fun getItem(id: String) {

        try {
            AzureData.instance.getOffer(id) { response ->

                if (response.isSuccessful) {

                    val offer = response.resource

                    println("GET operation succeeded for resource ${offer?.id}")
                }
                else {
                    println(response.error)
                }
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}