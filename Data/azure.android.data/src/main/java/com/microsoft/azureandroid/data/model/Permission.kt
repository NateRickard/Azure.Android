package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class Permission() : Resource() {

    var permissionMode: PermissionMode? = null

    @SerializedName(resourceLinkKey)
    var resourceLink: String? = null

    @SerializedName(tokenKey)
    var token: String? = null

    constructor(id: String, permissionMode: PermissionMode? = null, forResourceId: String) : this() {

        this.id = id
        this.permissionMode = permissionMode
        resourceLink = forResourceId
    }

    companion object {

        const val tokenKey          = "_token"
        const val resourceLinkKey   = "resource"
    }

    enum class PermissionMode {

        Read,
        All
    }
}