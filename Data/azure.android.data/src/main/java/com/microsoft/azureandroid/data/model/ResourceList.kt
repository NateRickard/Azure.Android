package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
* Created by Nate Rickard on 11/3/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class ResourceList<T: Resource> {

    @SerializedName(Keys.resourceIdKey)
    var resourceId: String? = null

    @SerializedName(Keys.countKey)
    var count: Int? = null

    @SerializedName(Document.listName, alternate = [Database.listName, Attachment.listName, DocumentCollection.listName, Offer.listName, Permission.listName, StoredProcedure.listName, Trigger.listName, User.listName])
    var items: Array<T>? = null

    val isPopuated: Boolean
            get() = resourceId != null && items != null

    companion object {

        object Keys {

            const val resourceIdKey = "_rid"
            const val countKey      = "_count"

            val list = listOf(resourceIdKey, countKey)
        }
    }
}