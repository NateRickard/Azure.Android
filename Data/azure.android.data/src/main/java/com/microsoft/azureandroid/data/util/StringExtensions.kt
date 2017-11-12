package com.microsoft.azureandroid.data.util

import com.microsoft.azureandroid.data.model.DataError

/**
 * Created by nater on 11/11/17.
 */

fun String.toError(): DataError {
    return JsonHelper.Gson.fromJson(this, DataError::class.java)
}

fun String.isValidResourceId() : Boolean {

    return !this.isBlank() && this.matches(RegEx.whitespaceRegex) && this.length <= 255
}

object RegEx {

    val whitespaceRegex = kotlin.text.Regex("\\S+")
}