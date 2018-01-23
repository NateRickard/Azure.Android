package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
* Created by Nate Rickard on 11/3/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class ResourceList<T: Resource> : ResourceBase() {

    @SerializedName(Keys.countKey)
    var count: Int = 0

    @SerializedName(Document.listName, alternate = [Database.listName, Attachment.listName, DocumentCollection.listName, Offer.listName, Permission.listName, StoredProcedure.listName, Trigger.listName, User.listName, UserDefinedFunction.listName])
    lateinit var items: Array<T>

    val isPopuated: Boolean
            get() = resourceId != null && count > 0

    companion object {

        object Keys {

            const val countKey = "_count"

            val list = listOf(resourceIdKey, countKey)
        }
    }
}