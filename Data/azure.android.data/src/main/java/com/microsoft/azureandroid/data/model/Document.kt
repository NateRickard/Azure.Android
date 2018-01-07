package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
* Created by Nate Rickard on 10/31/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

abstract class Document(id: String? = null) : Resource(id) {

    // Gets the self-link corresponding to attachments of the document from the Azure Cosmos DB service.
    @SerializedName(Keys.attachmentsLinkKey)
    var attachmentsLink: String? = null

    // Gets or sets the time to live in seconds of the document in the Azure Cosmos DB service.
    var timeToLive: Int? = null


    companion object {

        const val resourceName = "Document"
        const val listName = "Documents"

        object Keys {

            const val attachmentsLinkKey = "_attachments"

            val list = mutableListOf(attachmentsLinkKey)

            init {
                list.addAll(Resource.Companion.Keys.list)
            }
        }
    }
}