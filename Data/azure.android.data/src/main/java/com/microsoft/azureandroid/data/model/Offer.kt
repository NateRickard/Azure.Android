package com.microsoft.azureandroid.data.model

/**
 * Created by Nate Rickard on 11/14/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class Offer : Resource() {

    var offerType:          String? = null
    var offerVersion:       String? = null
    var resourceLink:       String? = null
    var offerResourceId:    String? = null
    var content:            OfferContent? = null
}

data class OfferContent(var offerThroughput: Int = 1000, var offerIsRUPerMinuteThroughputEnabled: Boolean?)