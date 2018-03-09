package com.microsoft.azureandroid.data.util

import com.microsoft.azureandroid.data.model.DataError
import com.microsoft.azureandroid.data.util.json.gson

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

fun String.toError(): DataError =
        gson.fromJson(this, DataError::class.java)

fun String.isValidResourceId() : Boolean =
        !this.isBlank() && this.matches(RegEx.whitespaceRegex) && this.length <= 255

object RegEx {

    val whitespaceRegex = kotlin.text.Regex("\\S+")
}