package com.microsoft.azureandroid.data.model

/**
* Created by Nate Rickard on 11/7/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class DataError(val message: String?, val code: String? = null) {

    constructor(error: Error) : this(error.message)

    constructor(error: Exception) : this(error.message)

    constructor() : this("")

    override fun toString(): String =
            "\r\nError\r\n\t$message\r\n${if (code != null) "\t$code\r\n" else ""}"
}