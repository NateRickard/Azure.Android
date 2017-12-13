package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Offer
import com.microsoft.azureandroid.data.model.ResourceType
import org.awaitility.Awaitility.await
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Nate Rickard on 12/11/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class OfferTests : ResourceTest<Offer>(ResourceType.Offer, false, false) {

    @Test
    fun listOffers() {

        AzureData.offers {
            resourceListResponse = it
        }

        await().until {
            resourceListResponse != null
        }

        assertResponseSuccess(resourceListResponse)
        assert(resourceListResponse?.resource?.count!! > 0) //can we assume there will always be > 0 offers?
    }

    @Test
    fun getOffer() {

        var offer: Offer? = null

        AzureData.offers {
            offer = it.resource?.items?.first()

            AzureData.getOffer(offer!!.id) {
                resourceResponse = it
            }
        }

        await().until {
            resourceResponse != null
        }

        assertResponseSuccess(resourceResponse)
        assertEquals(offer?.id, resourceResponse?.resource?.id)
    }
}