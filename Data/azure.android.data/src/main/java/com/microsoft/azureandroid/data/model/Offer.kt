package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Nate Rickard on 11/14/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class Offer : Resource() {

    var offerType:          String? = null

    var offerVersion:       String? = null

    @SerializedName(resourceLinkKey)
    var resourceLink:       String? = null

    var offerResourceId:    String? = null

    var content:            OfferContent? = null

    companion object {

        const val resourceLinkKey = "resource"
    }
}

data class OfferContent(var offerThroughput: Int = 1000, var offerIsRUPerMinuteThroughputEnabled: Boolean?)