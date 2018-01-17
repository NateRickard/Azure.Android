package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroid.data.model.User
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by Nate Rickard on 1/8/18.
 * Copyright © 2018 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class CustomDocumentTests : DocumentTest<CustomDocument>(CustomDocument::class.java)

class CustomDocument(id: String? = null) : Document(id) {

    var customString = "My Custom String"
    var customNumber = 0
    var customDate: Date = Date()
    var customBool = false
    var customArray = arrayOf(1, 2, 3)
    var customObject: User? = null
}