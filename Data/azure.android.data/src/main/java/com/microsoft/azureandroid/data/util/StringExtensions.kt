package com.microsoft.azureandroid.data.util

import com.microsoft.azureandroid.data.model.DataError

/**
* Created by Nate Rickard on 11/11/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

fun String.toError(): DataError =
        gson.fromJson(this, DataError::class.java)

fun String.isValidResourceId() : Boolean =
        !this.isBlank() && this.matches(RegEx.whitespaceRegex) && this.length <= 255

object RegEx {

    val whitespaceRegex = kotlin.text.Regex("\\S+")
}