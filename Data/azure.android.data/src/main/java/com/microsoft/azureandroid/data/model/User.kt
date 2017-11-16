package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class User : Resource() {

    @SerializedName(permissionsLinkKey)
    var permissionsLink: String? = null

    companion object {

        const val permissionsLinkKey  = "_permissions"
    }
}