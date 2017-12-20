package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
* Created by Nate Rickard on 10/31/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

open class Document(id: String? = null) : Resource(id) {

    // Gets the self-link corresponding to attachments of the document from the Azure Cosmos DB service.
    @SerializedName(Keys.attachmentsLinkKey)
    var attachmentsLink: String? = null

    // Gets or sets the time to live in seconds of the document in the Azure Cosmos DB service.
    var timeToLive: Int? = null

    @Transient
    internal var data = DocumentDataMap()

    // will be mapped to indexer, i.e. doc[key]
    operator fun get(key: String) = data[key]

    // will be mapped to indexer, i.e. doc[key] = value
    operator fun set(key: String, value: Any?) {

        if (this.javaClass != Document::class.java) {
            throw Exception("Error: Indexing operation cannot be used on a child type of Document")
        }

        if (Keys.list.contains(key)) {
            throw Exception("Error: Cannot use [key] = value syntax to set the following system generated properties: ${Keys.list.joinToString()}")
        }

        data[key] = value
    }

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