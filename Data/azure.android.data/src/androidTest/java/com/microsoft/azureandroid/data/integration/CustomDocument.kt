package com.microsoft.azureandroid.data.integration

import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.model.User
import java.util.*

/**
 * Created by Nate Rickard on 1/22/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
 */

class CustomDocument(id: String? = null) : Document(id) {

    var customString = "My Custom String"
    var customNumber = 0
    var customDate: Date = Date()
    var customBool = false
    var customArray = arrayOf(1, 2, 3)
    var customObject: User? = null
}